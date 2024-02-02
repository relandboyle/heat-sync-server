package com.ubibot.temperaturedata.model.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity(name = "unit")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UnitData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "unit_number")
    private String unitNumber;

    @Column(name = "tenant_name")
    private String tenantName;

    @Column(name = "full_unit")
    private String fullUnit;

    @JoinColumn(name = "building_id", insertable = false, updatable = false)
    @ManyToOne(targetEntity = BuildingData.class, fetch = FetchType.LAZY)
    private BuildingData buildingData;

    @Column(name = "building_id")
    private String buildingId;

    @OneToMany(mappedBy = "unitData", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(name = "sensor_entries")
    private List<SensorData> sensorEntries;
}
