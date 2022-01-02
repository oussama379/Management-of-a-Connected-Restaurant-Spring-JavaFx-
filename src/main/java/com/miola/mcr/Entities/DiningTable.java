package com.miola.mcr.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;


@Entity
@Table(name="DiningTables")
public class DiningTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;
    private String state;

    // TODO EAGER VS LAZY
    @OneToMany(mappedBy="diningTable")
    private Collection<Sensor> sensors=new ArrayList<>();

    public DiningTable(Long id, int number, String state, Collection<Sensor> sensors) {
        this.id = id;
        this.number = number;
        this.state = state;
        this.sensors = sensors;
    }

    public DiningTable() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Collection<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Collection<Sensor> sensors) {
        this.sensors = sensors;
    }
}