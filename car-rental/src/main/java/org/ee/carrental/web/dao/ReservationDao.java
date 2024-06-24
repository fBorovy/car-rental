package org.ee.carrental.web.dao;

import org.ee.carrental.web.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationDao {

    Reservation saveOrUpdate(Reservation reservation);

    void remove(long id);

    Optional<Reservation> findById(Long id);

    List<Reservation> findAll();

    void cancelReservation(long id);
}
