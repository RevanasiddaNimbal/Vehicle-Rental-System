package rental.repository;

import rental.model.Rental;

import java.util.List;

public interface RentalRepo {
    boolean save(Rental rental);

    boolean update(Rental rental);

    Rental findById(int id);

    List<Rental> findAll();

    List<Rental> findAllByCustomerId(String customerId);

    List<Rental> findAllByVehicleId(String vehicleId);


}
