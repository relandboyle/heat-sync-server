package com.ubibot.temperaturedata.model.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity(name = "building")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BuildingData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

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

    @Column(name = "full_address")
    private String fullAddress;

    @OneToMany(mappedBy = "buildingId", fetch = FetchType.LAZY)
    private List<UnitData> units;
}
