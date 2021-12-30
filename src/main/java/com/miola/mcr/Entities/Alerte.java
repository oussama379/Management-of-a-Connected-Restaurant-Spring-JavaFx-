package com.miola.mcr.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Time;
import java.util.*;

//@Data
//@NoArgsConstructor
@Entity
//@ToString
@Table(name="Alertes")
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


    public Alerte(Long id, String type, String severity, Double value, Character operator, Category category) {
        this.id = id;
        this.type = type;
        this.severity = severity;
        this.value = value;
        this.operator = operator;
        this.category = category;
    }

    public Alerte() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Character getOperator() {
        return operator;
    }

    public void setOperator(Character operator) {
        this.operator = operator;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}