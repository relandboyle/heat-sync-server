package com.ubibot.temperaturedata.model.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "unit")
//@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UnitData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "unit_id")
    private String unitId;

    @Column(name = "unit_number")
    private String unitNumber;

    @Column(name = "tenant_name")
    private String tenantName;

    @OneToMany(mappedBy = "unit")
    @Column(name = "sensors")
    private List<SensorData> sensors;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private BuildingData building;
}
