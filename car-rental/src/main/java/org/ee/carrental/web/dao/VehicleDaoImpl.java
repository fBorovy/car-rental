package org.ee.carrental.web.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.ee.carrental.web.model.Vehicle;

import java.util.List;
import java.util.Optional;

@Stateless
public class VehicleDaoImpl implements VehicleDao {

    @PersistenceContext(unitName = "pu")
    private EntityManager entityManager;

    // setter for testing
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Vehicle saveOrUpdate(Vehicle vehicle) {
        if (vehicle.getId() == null) {
            entityManager.persist(vehicle);
        } else {
            vehicle = entityManager.merge(vehicle);
        }
        return vehicle;
    }

    @Override
    public void remove(long id) {
        entityManager.remove(entityManager.getReference(Vehicle.class, id));
    }

    @Override
    public Optional<Vehicle> findById(Long id) {
        Vehicle vehicle = entityManager.find(Vehicle.class, id);
        return Optional.ofNullable(vehicle);
    }

    @Override
    public List<Vehicle> findAll() {
        TypedQuery<Vehicle> vehicleQuery = entityManager.createNamedQuery("Vehicle.findAll", Vehicle.class);
        return vehicleQuery.getResultList();
    }
}
