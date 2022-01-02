package com.miola.mcr.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

//@Data
//@NoArgsConstructor
@Entity
//@ToString
@Table(name="Roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;



    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<Permission> permissions;


    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<Zone> zones;

    @OneToMany(mappedBy="role", fetch = FetchType.EAGER)
    private Set<User> users;


    public Role(Long id, String title, String description, Set<Permission> permissions, Set<Zone> zones, Set<User> users) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.permissions = permissions;
        this.zones = zones;
        this.users = users;
    }

    public Role(Long id, Set<Zone> zones) {
        this.id = id;
        this.zones = zones;
    }

    public Role() {

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

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Zone> getZones() {
        return zones;
    }

    public void setZones(Set<Zone> zones) {
        this.zones = zones;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", Title='" + title + '\'' +
                ", Description='" + description + '\'' +
                '}';
    }
}