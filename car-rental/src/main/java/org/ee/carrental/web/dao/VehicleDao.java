package org.ee.carrental.web.dao;

import org.ee.carrental.web.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleDao {

    Vehicle saveOrUpdate(Vehicle vehicle);

    void remove(long id);

    Optional<Vehicle> findById(Long id);

    List<Vehicle> findAll();

    void reserveVehicle(long id);
}