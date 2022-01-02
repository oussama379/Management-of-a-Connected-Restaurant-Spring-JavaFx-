package com.miola.mcr.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

//@Data
//@NoArgsConstructor
@Entity
//@ToString
@Table(name="Categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    private String description;

    @OneToMany(mappedBy="category", fetch = FetchType.EAGER)
    private Set<Sensor> sensors;

    @OneToMany(mappedBy="category", fetch = FetchType.EAGER)
    private Set<Alerte> alerts;

    public Category(Long id, String title, String description, Set<Sensor> sensors, Set<Alerte> alerts) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.sensors = sensors;
        this.alerts = alerts;
    }

    public Category() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }

    public Set<Alerte> getAlerts() {
        return alerts;
    }

    public void setAlerts(Set<Alerte> alerts) {
        this.alerts = alerts;
    }
}