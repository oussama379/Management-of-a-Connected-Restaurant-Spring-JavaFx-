package com.miola.mcr.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;


@Entity
@Table(name="DiningTables")
public class DiningTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;
    private String state;

    @OneToMany(mappedBy="diningTable", fetch = FetchType.EAGER)
    private Set<Sensor> sensors;

    public DiningTable(Long id, int number, String state, Set<Sensor> sensors) {
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

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }
}