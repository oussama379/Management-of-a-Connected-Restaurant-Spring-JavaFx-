package com.miola.mcr.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

//@Data
//@NoArgsConstructor
//@ToString
@Entity
// More fast because all date is in one table, and null is not prblm because we dont have too many fields
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="device_type",
        discriminatorType = DiscriminatorType.STRING)
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private DevicePower power;
    @OneToMany(mappedBy="device", fetch = FetchType.EAGER)
    private Collection<Sensor> sensors=new ArrayList<>();

    public Device(Long id, String name, DevicePower power, Collection<Sensor> sensors) {
        this.id = id;
        this.name = name;
        this.power = power;
        this.sensors = sensors;
    }

    public Device() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DevicePower getPower() {
        return power;
    }

    public void setPower(DevicePower power) {
        this.power = power;
    }

    public Collection<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Collection<Sensor> sensors) {
        this.sensors = sensors;
    }

    public String getZone(){
        return sensors.stream().toList().get(0).getZoneName();
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", power=" + power +
                '}';
    }
}