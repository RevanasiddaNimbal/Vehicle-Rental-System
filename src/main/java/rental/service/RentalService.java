package rental.service;

import customer.model.Customer;
import customer.service.CustomerService;
import exception.ResourceNotFoundException;
import exception.VehicleNotAvailableException;
import rental.billing.RentalPriceCalculator;
import rental.billing.RentalTimeCalculator;
import rental.model.Rental;
import rental.model.RentalStatus;
import rental.repository.RentalRepo;
import vehicle.models.Status;
import vehicle.models.Vehicle;
import vehicle.service.VehicleService;
import vehicleowner.models.VehicleOwner;
import vehicleowner.service.VehicleOwnerService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RentalService {
    private final RentalRepo repository;
    private final VehicleService vehicleService;
    private final VehicleOwnerService vehicleOwnerService;
    private final CustomerService customerService;
    private final RentalPriceCalculator rentalPriceCalculator;
    private final RentalTimeCalculator rentalTimeCalculator;
    private final ExecutorService asyncExecutor;

    public RentalService(RentalRepo repository, RentalPriceCalculator rentalPriceCalculator, VehicleService vehicleService, VehicleOwnerService vehicleOwnerService, CustomerService customerService, RentalTimeCalculator rentalTimeCalculator) {
        this.repository = repository;
        this.rentalPriceCalculator = rentalPriceCalculator;
        this.vehicleService = vehicleService;
        this.vehicleOwnerService = vehicleOwnerService;
        this.customerService = customerService;
        this.rentalTimeCalculator = rentalTimeCalculator;
        this.asyncExecutor = Executors.newFixedThreadPool(10);
    }

    public CompletableFuture<Void> bookRentalsAsync(List<Rental> rentals) {
        if (rentals == null || rentals.isEmpty()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Rental list cannot be null or empty."));
        }

        return CompletableFuture.runAsync(() -> {
            List<Vehicle> lockedVehicles = new ArrayList<>();
            try {
                for (Rental rental : rentals) {
                    Vehicle vehicle = vehicleService.getVehiclesById(rental.getVehicleId());
                    if (vehicle == null) {
                        throw new ResourceNotFoundException("Vehicle not found: " + rental.getVehicleId());
                    }
                    if (vehicle.getStatus() != Status.AVAILABLE) {
                        throw new VehicleNotAvailableException("Vehicle " + vehicle.getId() + " is no longer available.");
                    }

                    boolean isLocked = vehicleService.updateStatusById(vehicle.getId(), Status.RESERVED);
                    if (!isLocked) {
                        throw new VehicleNotAvailableException("Failed to lock vehicle " + vehicle.getId());
                    }
                    lockedVehicles.add(vehicle);

                    rental.setStatus(RentalStatus.BOOKED);
                    boolean isSaved = repository.save(rental);
                    if (!isSaved) {
                        throw new RuntimeException("Database error: Could not save rental for vehicle " + vehicle.getId());
                    }

                    vehicleService.updateStatusById(vehicle.getId(), Status.RENTED);
                }
            } catch (Exception e) {
                for (Vehicle lockedVehicle : lockedVehicles) {
                    vehicleService.updateStatusById(lockedVehicle.getId(), Status.AVAILABLE);
                }
                throw new RuntimeException(e.getMessage(), e);
            }
        }, asyncExecutor);
    }

    public void shutdownAsyncExecutor() {
        if (!asyncExecutor.isShutdown()) {
            asyncExecutor.shutdown();
        }
    }

    public Rental processReturnValidation(int rentalId, String customerId) {
        Rental rental = repository.findById(rentalId);
        if (rental == null) {
            throw new ResourceNotFoundException("Rental ID " + rentalId + " not found.");
        }
        if (!rental.getCustomerId().equals(customerId)) {
            throw new IllegalArgumentException("This rental does not belong to you.");
        }
        if (rental.getStatus() != RentalStatus.BOOKED) {
            throw new IllegalStateException("Rental is already completed or cancelled.");
        }
        if (rental.getStartDate().isAfter(LocalDate.now()) ||
                (rental.getStartDate().isEqual(LocalDate.now()) && rental.getStartTime().isAfter(LocalTime.now()))) {
            throw new IllegalStateException("Cannot return a vehicle before the pickup time.");
        }
        return rental;
    }

    public Rental processCancelValidation(int rentalId, String customerId) {
        Rental rental = repository.findById(rentalId);
        if (rental == null) {
            throw new ResourceNotFoundException("Rental ID " + rentalId + " not found.");
        }
        if (!rental.getCustomerId().equals(customerId)) {
            throw new IllegalArgumentException("This rental does not belong to you.");
        }
        if (rental.getStatus() == RentalStatus.CANCELLED) {
            throw new IllegalStateException("Rental is already cancelled.");
        }
        if (rental.getStatus() == RentalStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed rental.");
        }
        if (rental.getStartDate().isBefore(LocalDate.now()) ||
                (rental.getStartDate().isEqual(LocalDate.now()) && rental.getStartTime().isBefore(LocalTime.now()))) {
            throw new IllegalStateException("Cannot cancel after the rental period has started.");
        }
        return rental;
    }

    public void completeRentals(List<Rental> rentals) {
        for (Rental rental : rentals) {
            rental.setStatus(RentalStatus.COMPLETED);
            repository.update(rental);
        }
    }

    public void updateRentalStatus(int rentalId, RentalStatus status) {
        Rental rental = repository.findById(rentalId);
        if (rental == null) {
            throw new ResourceNotFoundException("Rental ID " + rentalId + " not found.");
        }
        rental.setStatus(status);
        repository.update(rental);
    }

    public void calculateTotalRent(Rental rental) {
        rentalPriceCalculator.calculateTotalRent(rental);
    }

    public int getDays(Rental rental) {
        return rentalTimeCalculator.calculateRentalDays(rental.getStartDate(), rental.getEndDate());
    }

    public Rental getRentalById(int id) {
        return repository.findById(id);
    }

    public List<Rental> getAllRentals() {
        return repository.findAll();
    }

    public List<Rental> getActiveRentals() {
        List<Rental> result = new ArrayList<>();
        for (Rental rental : repository.findAll()) {
            if (rental.getStatus() == RentalStatus.BOOKED) result.add(rental);
        }
        return result;
    }

    public List<Rental> getRentalsByCustomerId(String customerId) {
        return repository.findAllByCustomerId(customerId);
    }

    public List<Rental> getActiveRentalsByCustomerId(String customerId) {
        List<Rental> result = new ArrayList<>();
        for (Rental rental : repository.findAllByCustomerId(customerId)) {
            if (rental.getStatus() == RentalStatus.BOOKED) result.add(rental);
        }
        return result;
    }

    public List<Rental> getRentalsByOwnerId(String ownerId) {
        List<Rental> result = new ArrayList<>();
        for (Vehicle vehicle : vehicleService.getVehiclesByOwnerId(ownerId)) {
            List<Rental> rentals = repository.findAllByVehicleId(vehicle.getId());
            if (rentals != null) result.addAll(rentals);
        }
        return result;
    }

    public List<Rental> getActiveRentalsByOwnerId(String ownerId) {
        List<Rental> result = new ArrayList<>();
        for (Vehicle vehicle : vehicleService.getVehiclesByOwnerId(ownerId)) {
            List<Rental> rentals = repository.findAllByVehicleId(vehicle.getId());
            if (rentals != null) {
                for (Rental rental : rentals) {
                    if (rental.getStatus() == RentalStatus.BOOKED) result.add(rental);
                }
            }
        }
        return result;
    }

    public List<VehicleOwner> getActiveOwnersByCustomerId(String customerId) {
        return getVehicleOwners(getActiveRentalsByCustomerId(customerId));
    }

    public List<VehicleOwner> getAllOwnersByCustomerId(String customerId) {
        return getVehicleOwners(getRentalsByCustomerId(customerId));
    }

    public List<Customer> getActiveCustomersByOwnerId(String ownerId) {
        List<Customer> customers = new ArrayList<>();
        for (Rental rental : getActiveRentalsByOwnerId(ownerId)) {
            Customer customer = customerService.getCustomerById(rental.getCustomerId());
            if (customer != null && !customers.contains(customer)) customers.add(customer);
        }
        return customers;
    }

    public List<Customer> getAllCustomersByOwnerId(String ownerId) {
        List<Customer> customers = new ArrayList<>();
        for (Rental rental : getRentalsByOwnerId(ownerId)) {
            Customer customer = customerService.getCustomerById(rental.getCustomerId());
            if (customer != null && !customers.contains(customer)) customers.add(customer);
        }
        return customers;
    }

    public List<Vehicle> getActiveVehiclesByCustomerId(String customerId) {
        List<Vehicle> vehicles = new ArrayList<>();
        for (Rental rental : getActiveRentalsByCustomerId(customerId)) {
            Vehicle vehicle = vehicleService.getVehiclesById(rental.getVehicleId());
            if (vehicle != null && !vehicles.contains(vehicle)) vehicles.add(vehicle);
        }
        return vehicles;
    }

    public List<Vehicle> getVehiclesByCustomerId(String customerId) {
        List<Vehicle> vehicles = new ArrayList<>();
        for (Rental rental : getRentalsByCustomerId(customerId)) {
            Vehicle vehicle = vehicleService.getVehiclesById(rental.getVehicleId());
            if (vehicle != null && !vehicles.contains(vehicle)) vehicles.add(vehicle);
        }
        return vehicles;
    }

    private List<VehicleOwner> getVehicleOwners(List<Rental> rentals) {
        List<VehicleOwner> owners = new ArrayList<>();
        for (Rental rental : rentals) {
            Vehicle vehicle = vehicleService.getVehiclesById(rental.getVehicleId());
            if (vehicle == null) continue;
            VehicleOwner owner = vehicleOwnerService.getVehicleOwnerById(vehicle.getOwnerId());
            if (owner != null && !owners.contains(owner)) owners.add(owner);
        }
        return owners;
    }
}