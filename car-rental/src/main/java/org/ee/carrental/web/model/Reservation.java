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
    private boolean reservation_status;

    private BigDecimal price;

    public Reservation() {}

    public Reservation(Date reservation_start, Date reservation_end, Date reservation_date,
                       long reserved_vehicle_id, long reserved_user_id, boolean payment_status, BigDecimal price, boolean reservation_status) {
        this.reservation_start = reservation_start;
        this.reservation_end = reservation_end;
        this.reservation_date = reservation_date;
        this.reserved_vehicle_id = reserved_vehicle_id;
        this.reserved_user_id = reserved_user_id;
        this.payment_status = payment_status;
        this.price = price;
        this.reservation_status = reservation_status;
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

    public boolean getPayment_status() {
        return payment_status;
    }

    public BigDecimal getPrice() { return price; }

    public boolean getReservation_status() { return reservation_status; }

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

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setReservation_status(boolean status) { this.reservation_status = status; }
}
