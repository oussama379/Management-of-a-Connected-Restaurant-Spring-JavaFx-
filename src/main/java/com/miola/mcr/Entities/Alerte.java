package com.miola.mcr.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Time;
import java.util.*;

@Data
@NoArgsConstructor
@Entity
@ToString
public class Alerte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String severity;
    private Double value;
    private Character operator;
    //private java.sql.Time time;
    @ManyToOne
    private Category category;





}