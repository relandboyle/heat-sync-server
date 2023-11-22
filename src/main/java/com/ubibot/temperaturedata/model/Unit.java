package com.ubibot.temperaturedata.model;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "unit")
public class Unit {
    @Id
    @Column(name = "unit_id")
    private String unitId;
    @Column(name = "unit_number")
    private String unitNumber;
    @Column(name = "tenant_name")
    private String tenantName;
    @OneToMany(mappedBy = "unit")
    @Column(name = "city")
    private List<SensorDataToPersist> sensors;
    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;
}
