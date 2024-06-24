package org.ee.carrental.web.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.ee.carrental.web.model.Reservation;

import java.util.List;
import java.util.Optional;

@Stateless
public class ReservationDaoImpl implements ReservationDao {

    @PersistenceContext(unitName = "pu")
    private EntityManager entityManager;

    // Setter for testing
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Reservation saveOrUpdate(Reservation reservation) {
        if (reservation.getId() == 0) { // Assuming 0 indicates a new reservation
            entityManager.persist(reservation);
        } else {
            reservation = entityManager.merge(reservation);
        }
        return reservation;
    }

    @Override
    public void remove(long id) {
        Reservation reservation = entityManager.getReference(Reservation.class, id);
        if (reservation != null) {
            entityManager.remove(reservation);
        }
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        Reservation reservation = entityManager.find(Reservation.class, id);
        return Optional.ofNullable(reservation);
    }

    @Override
    public List<Reservation> findAll() {
        TypedQuery<Reservation> query = entityManager.createNamedQuery("Reservation.findAll", Reservation.class);
        return query.getResultList();
    }

    @Override
    public void cancelReservation(long id) {
        Reservation reservation = entityManager.find(Reservation.class, id);
        if (reservation != null) {
            reservation.setPayment_status(false); // Example of canceling
            entityManager.merge(reservation);
        }
    }
}
