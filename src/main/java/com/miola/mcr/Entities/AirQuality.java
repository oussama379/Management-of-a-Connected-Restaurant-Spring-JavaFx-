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
@DiscriminatorValue("AirQuality")
// works with DeviceRepositoy
public class AirQuality extends Device {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    private String type;
    private Boolean alarm;

    public AirQuality(Long id, String name, Boolean power, Collection<Sensor> sensors, Boolean state, String type, Boolean alarm) {
        super(id, name, power, sensors);
        this.type = type;
        this.alarm = alarm;
    }


    public AirQuality() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getAlarm() {
        return alarm;
    }

    public void setAlarm(Boolean alarm) {
        this.alarm = alarm;
    }
}