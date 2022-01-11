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
@DiscriminatorValue("AirConditioner")
// works with DeviceRepositoy
public class AirConditioner extends Device {


//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    private Double temperature;

    public AirConditioner(Long id, String name, DevicePower power, Collection<Sensor> sensors, Double temperature) {
        super(id, name, power, sensors);
        this.temperature = temperature;
    }


    public AirConditioner() {

    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "AirConditioner{" +
                "temperature=" + temperature +
                '}';
    }
}