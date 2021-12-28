package com.miola.mcr.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@Entity
@ToString
@Table(name="Categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    private String description;

    @OneToMany(mappedBy="category")
    private Collection<Sensor> sensors=new ArrayList<>();

    @OneToMany(mappedBy="category")
    private Collection<Alerte> alerts=new ArrayList<>();



}