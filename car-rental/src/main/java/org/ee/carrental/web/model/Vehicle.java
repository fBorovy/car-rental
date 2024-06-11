package org.ee.carrental.web.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@NamedQuery(name = "Vehicle.findAll", query = "select v from Vehicle v")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String brand;
    private String model;
    private String production_year;
    private BigDecimal price_per_day;
    private boolean reserved;
    private boolean rented;
    private long last_reservation_time;
    private long last_rental_time;

    public Vehicle() {}

    public Vehicle(String brand, String model, String production_year, BigDecimal price_per_day) {
        this.brand = brand;
        this.model = model;
        this.price_per_day = price_per_day;
        this.reserved = false;
        this.rented = false;
        this.last_reservation_time = 0; // 0 oznacza brak rezerwacji na ten moment
        this.last_rental_time = 0; // jak wyżej
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }
    public void setPrice_per_day(BigDecimal price) { this.price_per_day = price; }

    public String getBrand() {
        return this.brand;
    }
    public String getModel() { return this.model; }
    public BigDecimal getPrice_per_day() { return this.price_per_day; }
}
