package com.ubibot.temperaturedata.model.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "building")
//@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BuildingData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    private List<UnitData> unitData;
}
