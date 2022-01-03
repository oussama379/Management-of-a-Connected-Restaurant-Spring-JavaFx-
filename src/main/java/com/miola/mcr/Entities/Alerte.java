package com.miola.mcr.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;


@Entity
@Table(name="Alertes")
public class Alerte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String severity;
    private Double value;
    private String operator;
    @Basic
    private java.sql.Timestamp time;
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;


    public Alerte(Long id, String type, String severity, Double value, String operator, java.sql.Timestamp time, Category category) {
        this.id = id;
        this.type = type;
        this.severity = severity;
        this.value = value;
        this.operator = operator;
        this.time = time;
        this.category = category;
    }

    public Alerte() {

    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Category getCategory() {
        return category;
    }
    public String getCategoryName() {
        return category.getTitle();
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}