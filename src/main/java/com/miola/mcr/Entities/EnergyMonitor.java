package com.miola.mcr.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

//@Data
//@NoArgsConstructor
@Entity
//@ToString
@DiscriminatorValue("EnergyMonitor")
// works with DeviceRepositoy
public class EnergyMonitor extends Device {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    private Boolean state;


    public EnergyMonitor(Long id, String name, DevicePower power, Collection<Sensor> sensors, Boolean state) {
        super(id, name, power, sensors);
        this.state = state;
    }

    public EnergyMonitor() {

    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return  "EnergyMonitor{" +
                "state=" + state +
                '}';
    }
}