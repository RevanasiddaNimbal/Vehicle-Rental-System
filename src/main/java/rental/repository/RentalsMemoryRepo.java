package rental.repository;

import rental.model.Rental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RentalsMemoryRepo implements RentalRepo {

    private final Map<Integer, Rental> storage = new HashMap<>();

    @Override
    public boolean save(Rental rental) {
        if (rental == null) return false;
        storage.put(rental.getId(), rental);
        return true;
    }

    @Override
    public boolean update(Rental rental) {
        if (rental == null || !storage.containsKey(rental.getId())) {
            return false;
        }
        storage.put(rental.getId(), rental);
        return true;
    }

    @Override
    public Rental findById(int id) {
        return storage.get(id);
    }

    @Override
    public List<Rental> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Rental> findAllByCustomerId(String customerId) {
        List<Rental> result = new ArrayList<>();

        if (customerId == null) return result;

        for (Rental rental : storage.values()) {
            if (customerId.equals(rental.getCustomerId())) {
                result.add(rental);
            }
        }

        return result;
    }

    @Override
    public List<Rental> findAllByVehicleId(String vehicleId) {
        List<Rental> result = new ArrayList<>();

        if (vehicleId == null) return result;

        for (Rental rental : storage.values()) {
            if (vehicleId.equals(rental.getVehicleId())) {
                result.add(rental);
            }
        }

        return result;
    }


}