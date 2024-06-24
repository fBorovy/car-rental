package org.ee.carrental.web.service;

import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.ee.carrental.web.model.Reservation;
import org.ee.carrental.web.model.User;
import org.ee.carrental.web.model.Vehicle;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Stateless
public class UserRegistry {

    @PersistenceContext(unitName = "pu")
    private EntityManager entityManager;

    @Schedule(hour = "*", minute = "*", second = "*/30", info ="Every minute timer",persistent=true)
    public void getReservations() {

        System.out.println(" test1");
       List<Reservation> reservations = findAll();
        System.out.println(reservations);

        for (Reservation reservation : reservations) {
            System.out.println("test2 ------------ " + reservation);
            System.out.println("test3 ------------ " + reservation.getReservation_date());
            System.out.println("test4 ------------ " + reservation.getReservation_status());

            if (reservation.getReservation_date().before(new Date()) && reservation.getReservation_status()) {
                System.out.println("test5 ------------ ");
                performAction(reservation);
                setStatus(reservation.getId(), false);
            }
        }
    }

    private void performAction(Reservation reservation) {
        try {
            // Pobierz użytkownika na podstawie ID
            User user = findUserById(reservation.getReserved_user_id());
            String username = (user != null) ? user.getLogin() : "Unknown";

            // Pobierz pojazd na podstawie ID
            Vehicle vehicle = findVehicleById(reservation.getReserved_vehicle_id()).orElse(null);
            String vehicleBrand = (vehicle != null) ? vehicle.getBrand() : "Unknown";
            String vehicleModel = (vehicle != null) ? vehicle.getModel() : "Unknown";

            // Wywołaj metodę sendEmail z pobranymi danymi
            System.out.println("TEST WYSYLANIA WIADOMOSCI 1");
            new GMailer().sendEmail(username, vehicleBrand, vehicleModel, String.valueOf(reservation.getId()), "canceledReservation");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Reservation> findAll() {
        TypedQuery<Reservation> query = entityManager.createNamedQuery("Reservation.findAll", Reservation.class);
        return query.getResultList();
    }

    public User findUserById(Long id) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u LEFT JOIN FETCH u.userGroups WHERE u.id = :id", User.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Optional<Vehicle> findVehicleById(Long id) {
        Vehicle vehicle = entityManager.find(Vehicle.class, id);
        return Optional.ofNullable(vehicle);
    }

    public void setStatus(long id, boolean status) {
        Reservation reservation = entityManager.getReference(Reservation.class, id);
        if (reservation != null) {
            reservation.setReservation_status(false);
            entityManager.merge(reservation);
        }
    }
}
