package org.ee.carrental.web.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@NamedQuery(name = "Reservation.findAll", query = "SELECT r FROM Reservation r")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private Date reservation_start;
    private Date reservation_end;
    private Date reservation_date;
    private long reserved_vehicle_id;
    private long reserved_user_id;
    private boolean payment_status;
    private int price;

    public Reservation() {}

    public Reservation(Date reservation_start, Date reservation_end, Date reservation_date,
                       long reserved_vehicle_id, long reserved_user_id, boolean payment_status, int price) {
        this.reservation_start = reservation_start;
        this.reservation_end = reservation_end;
        this.reservation_date = reservation_date;
        this.reserved_vehicle_id = reserved_vehicle_id;
        this.reserved_user_id = reserved_user_id;
        this.payment_status = payment_status;
        this.price = price;
    }

    // Gettery
    public long getId() {
        return id;
    }

    public Date getReservation_start() {
        return reservation_start;
    }

    public Date getReservation_end() {
        return reservation_end;
    }

    public Date getReservation_date() {
        return reservation_date;
    }

    public long getReserved_vehicle_id() {
        return reserved_vehicle_id;
    }

    public long getReserved_user_id() {
        return reserved_user_id;
    }

    public boolean isPayment_status() {
        return payment_status;
    }

    public int getPrice() {
        return price;
    }

    // Settery
    public void setId(long id) {
        this.id = id;
    }

    public void setReservation_start(Date reservation_start) {
        this.reservation_start = reservation_start;
    }

    public void setReservation_end(Date reservation_end) {
        this.reservation_end = reservation_end;
    }

    public void setReservation_date(Date reservation_date) {
        this.reservation_date = reservation_date;
    }

    public void setReserved_vehicle_id(long reserved_vehicle_id) {
        this.reserved_vehicle_id = reserved_vehicle_id;
    }

    public void setReserved_user_id(long reserved_user_id) {
        this.reserved_user_id = reserved_user_id;
    }

    public void setPayment_status(boolean payment_status) {
        this.payment_status = payment_status;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
