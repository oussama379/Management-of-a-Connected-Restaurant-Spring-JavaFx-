package com.miola.mcr.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@ToString
@Table(name="Sensors")
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String topic;
    //private JsonObject data;
    @ManyToOne
    private Zone zone;
    @ManyToOne
    private DiningTable diningTable;
    @ManyToOne
    private Device device;
    @ManyToOne
    private Category category;




}