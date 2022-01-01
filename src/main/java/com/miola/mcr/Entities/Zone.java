package com.miola.mcr.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.*;
//@Data
//@NoArgsConstructor
@Entity
//@ToString
@Table(name="Zones")
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    //, cascade = CascadeType.PERSIST
    @JoinTable(name = "zone_role", joinColumns = @JoinColumn(name = "zone_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy="zone", fetch = FetchType.EAGER)
    private Set<Sensor> sensors;

    public Zone(Long id, String title, Set<Role> roles, Set<Sensor> sensors) {
        this.id = id;
        this.title = title;
        this.roles = roles;
        this.sensors = sensors;
    }

    public Zone(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Zone() {

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}