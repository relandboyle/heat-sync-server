package com.ubibot.temperaturedata.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity(name = "building")
public class Building {
    @Id
    @Column(name = "building_id")
    private String buildingId;
    @Column(name = "street_number")
    private String streetNumber;
    @Column(name = "street_name")
    private String streetName;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "country")
    private String country;
    @OneToMany(mappedBy = "building")
    @Column(name = "units")
    private List<Unit> units;
}
