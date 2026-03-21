package rental.service;

import customer.model.Customer;
import customer.service.CustomerService;
import rental.billing.RentalPriceCalculator;
import rental.billing.RentalTimeCalculator;
import rental.model.Rental;
import rental.model.RentalStatus;
import rental.repository.RentalRepo;
import util.InputUtil;
import vehicle.models.Vehicle;
import vehicle.service.VehicleService;
import vehicleowner.models.VehicleOwner;
import vehicleowner.service.VehicleOwnerService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RentalService {
    private final RentalRepo repository;
    private final VehicleService vehicleService;
    private final VehicleOwnerService vehicleOwnerService;
    private final CustomerService customerService;
    private final RentalPriceCalculator rentalPriceCalculator;
    private final RentalTimeCalculator rentalTimeCalculator;

    public RentalService(RentalRepo repository, RentalPriceCalculator rentalPriceCalculator, VehicleService vehicleService, VehicleOwnerService vehicleOwnerService, CustomerService customerService, RentalTimeCalculator rentalTimeCalculator) {
        this.repository = repository;
        this.rentalPriceCalculator = rentalPriceCalculator;
        this.vehicleService = vehicleService;
        this.vehicleOwnerService = vehicleOwnerService;
        this.customerService = customerService;
        this.rentalTimeCalculator = rentalTimeCalculator;
    }

    public boolean addRental(Rental rental) {
        if (rental == null) return false;

        List<Rental> activeRentals = repository.findAllByVehicleId(rental.getVehicleId());
        for (Rental r : activeRentals) {
            if (r.getStatus() == RentalStatus.BOOKED) return false;
        }
        return repository.save(rental);
    }

    public void updateRentalStatus(int rentalId, RentalStatus status) {
        Rental rental = repository.findById(rentalId);
        if (rental == null) return;
        rental.setStatus(status);
        repository.update(rental);
    }

    public Rental getRentalById(int id) {
        return repository.findById(id);
    }

    public List<Rental> getAllRentals() {
        return repository.findAll();
    }

    public List<Rental> getActiveRentals() {
        List<Rental> result = new ArrayList<>();
        List<Rental> rentals = repository.findAll();
        for (Rental rental : rentals) {
            if (rental.getStatus() == RentalStatus.BOOKED) result.add(rental);
        }
        return result;
    }

    public List<Rental> getRentalsByCustomerId(String customerId) {
        return repository.findAllByCustomerId(customerId);
    }

    public List<Rental> getActiveRentalsByCustomerId(String customerId) {
        List<Rental> result = new ArrayList<>();
        List<Rental> rentals = repository.findAllByCustomerId(customerId);
        for (Rental rental : rentals) {
            if (rental.getStatus() == RentalStatus.BOOKED) result.add(rental);
        }
        return result;
    }

    public List<Rental> getRentalsByOwnerId(String ownerId) {
        List<Rental> result = new ArrayList<>();
        List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(ownerId);
        for (Vehicle vehicle : vehicles) {
            List<Rental> rentals = repository.findAllByVehicleId(vehicle.getId());
            if (rentals != null) result.addAll(rentals);
        }
        return result;
    }

    public List<Rental> getActiveRentalsByOwnerId(String ownerId) {
        List<Rental> result = new ArrayList<>();
        List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(ownerId);
        for (Vehicle vehicle : vehicles) {
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
        List<Rental> rentals = getActiveRentalsByCustomerId(customerId);
        return getVehicleOwners(rentals);
    }

    public List<VehicleOwner> getAllOwnersByCustomerId(String customerId) {
        List<Rental> rentals = getRentalsByCustomerId(customerId);
        return getVehicleOwners(rentals);
    }

    public List<Customer> getActiveCustomersByOwnerId(String ownerId) {
        List<Customer> customers = new ArrayList<>();
        List<Rental> activeRentals = getActiveRentalsByOwnerId(ownerId);
        for (Rental rental : activeRentals) {
            Customer customer = customerService.getCustomerById(rental.getCustomerId());
            if (customer != null && !customers.contains(customer)) customers.add(customer);
        }
        return customers;
    }

    public List<Customer> getAllCustomersByOwnerId(String ownerId) {
        List<Customer> customers = new ArrayList<>();
        List<Rental> rentals = getRentalsByOwnerId(ownerId);
        for (Rental rental : rentals) {
            Customer customer = customerService.getCustomerById(rental.getCustomerId());
            if (customer != null && !customers.contains(customer)) customers.add(customer);
        }
        return customers;
    }

    public List<Vehicle> getActiveVehiclesByCustomerId(String customerId) {
        List<Vehicle> vehicles = new ArrayList<>();
        List<Rental> rentals = getActiveRentalsByCustomerId(customerId);
        for (Rental rental : rentals) {
            Vehicle vehicle = vehicleService.getVehiclesById(rental.getVehicleId());
            if (vehicle != null && !vehicles.contains(vehicle)) vehicles.add(vehicle);
        }
        return vehicles;
    }

    public List<Vehicle> getVehiclesByCustomerId(String customerId) {
        List<Vehicle> vehicles = new ArrayList<>();
        List<Rental> rentals = getRentalsByCustomerId(customerId);
        for (Rental rental : rentals) {
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

    public List<Rental> collectRentalsForReturn(Scanner input, String customerId) {
        List<Rental> rentalsToReturn = new ArrayList<>();
        boolean addMore = true;
        while (addMore) {
            int rentalId = InputUtil.readPositiveInt(input, "Enter Rental ID to return");
            Rental rental = getRentalById(rentalId);
            if (rental == null || !rental.getCustomerId().equals(customerId)) {
                System.out.println("Invalid rental or does not belong to you.");
                continue;
            }

            if (rental.getStartTime().isAfter(LocalTime.now()) || rental.getStartDate().isAfter(LocalDate.now())) {
                System.out.println("Can't return vehicle before pickup time and date.");
                break;
            }
            rentalsToReturn.add(rental);
            System.out.println("Added Rental for return.");
            String choice = InputUtil.readString(input, "Return another vehicle? (Y/N)");
            addMore = choice.equalsIgnoreCase("Y");
        }
        return rentalsToReturn;
    }

    public void completeRentals(List<Rental> rentals) {
        for (Rental rental : rentals) {
            rental.setStatus(RentalStatus.COMPLETED);
            repository.update(rental);
        }
    }

    public int getDays(Rental rental) {
        return rentalTimeCalculator.calculateRentalDays(rental.getStartDate(), rental.getEndDate());
    }

    public void calculateTotalRent(Rental rental) {
        rentalPriceCalculator.calculateTotalRent(rental);
    }
}