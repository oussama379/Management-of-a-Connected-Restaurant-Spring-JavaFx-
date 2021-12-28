package com.miola.mcr.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;
@Data
@NoArgsConstructor
@Entity
@ToString
@Table(name="Zones")
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToMany
    @JoinTable(name = "zone_role", joinColumns = @JoinColumn(name = "zone_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles=new ArrayList<>();

    @OneToMany(mappedBy="zone")
    private Collection<Sensor> sensors=new ArrayList<>();

}