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

    @ManyToOne()
    @JoinColumn(name = "building_data", foreignKey = @ForeignKey(name = "unit_building"))
    private BuildingData buildingData;

    @OneToMany(mappedBy = "unitData", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(name = "sensor_entries")
    private List<SensorData> sensorEntries;
}
