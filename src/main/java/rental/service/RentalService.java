package rental.service;

import customer.model.Customer;
import customer.service.CustomerService;
import rental.billing.RentalPriceCalculator;
import rental.model.Rental;
import rental.model.RentalStatus;
import rental.repository.RentalRepo;
import vehicle.models.Vehicle;
import vehicle.service.VehicleService;
import vehicleowner.models.VehicleOwner;
import vehicleowner.service.VehicleOwnerService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalService {
    private final RentalRepo repository;
    private final VehicleService vehicleService;
    private final VehicleOwnerService vehicleOwnerService;
    private final CustomerService customerService;

    private final RentalPriceCalculator rentalPriceCalculator;

    public RentalService(RentalRepo repository, RentalPriceCalculator rentalPriceCalculator, VehicleService vehicleService, VehicleOwnerService vehicleOwnerService, CustomerService customerService) {
        this.repository = repository;
        this.rentalPriceCalculator = rentalPriceCalculator;
        this.vehicleService = vehicleService;
        this.vehicleOwnerService = vehicleOwnerService;
        this.customerService = customerService;
    }

    public boolean addRental(Rental rental) {
        if (rental == null) return false;
        List<Rental> activeRentals = repository.findAllByVehicleId(rental.getVehicleId());

        for (Rental r : activeRentals) {
            if (r.getStatus() == RentalStatus.BOOKED) {
                System.out.println("Vehicle is already rented.");
                return false;
            }
        }
        return repository.save(rental);
    }

    public boolean updateRental(Rental rental) {
        if (rental == null) return false;

        if (repository.findById(rental.getId()) == null) {
            System.out.println("Rental not found.");
            return false;
        }
        return repository.update(rental);
    }


    public boolean updateRentalStatus(int rentalId, RentalStatus status) {
        Rental rental = repository.findById(rentalId);
        if (rental == null) {
            System.out.println("Rental not found.");
            return false;
        }
        rental.setStatus(status);
        return repository.update(rental);
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
            if (rental.getStatus().equals(RentalStatus.BOOKED)) {
                result.add(rental);
            }
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
            if (rental.getStatus().equals(RentalStatus.BOOKED)) {
                result.add(rental);
            }
        }
        return result;
    }

    public List<Rental> getRentalsByOwnerId(String ownerId) {
        List<Rental> result = new ArrayList<>();
        List<Vehicle> vehicles = vehicleService.getVehiclesByOwnerId(ownerId);
        for (Vehicle vehicle : vehicles) {
            List<Rental> rentals = repository.findAllByVehicleId(vehicle.getId());
            if (rentals != null)
                result.addAll(rentals);
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
                    if (rental.getStatus().equals(RentalStatus.BOOKED)) {
                        result.add(rental);
                    }
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

            if (customer != null && !customers.contains(customer)) {
                customers.add(customer);
            }
        }
        return customers;
    }

    public List<Customer> getAllCustomersByOwnerId(String ownerId) {
        List<Customer> customers = new ArrayList<>();
        List<Rental> activeRentals = getRentalsByOwnerId(ownerId);
        for (Rental rental : activeRentals) {
            Customer customer = customerService.getCustomerById(rental.getCustomerId());
            if (customer != null && !customers.contains(customer)) {
                customers.add(customer);
            }
        }
        return customers;
    }

    public int getDays(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            System.out.println("Invalid date.");
            return 0;
        }
        if (startDate.equals(endDate)) {
            return 1;
        }
        return (int) rentalPriceCalculator.getDays(startDate, endDate);
    }

    public void calculateTotalRent(Rental rental) {
        if (rental == null) return;
        rentalPriceCalculator.calculateTotalRent(rental);
    }

    private List<VehicleOwner> getVehicleOwners(List<Rental> rentals) {
        List<VehicleOwner> owners = new ArrayList<>();
        for (Rental rental : rentals) {
            Vehicle vehicle = vehicleService.getVehiclesById(rental.getVehicleId());
            if (vehicle == null) continue;
            VehicleOwner owner = vehicleOwnerService.getVehicleOwnerById(vehicle.getOwnerId());
            if (owner != null && !owners.contains(owner)) {
                owners.add(owner);
            }
        }
        return owners;
    }
}