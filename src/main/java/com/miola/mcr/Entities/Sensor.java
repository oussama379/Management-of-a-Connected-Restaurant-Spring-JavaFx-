package com.miola.mcr.Entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//@Data
//@NoArgsConstructor
@Entity
//@ToString
@Table(name="Sensors")
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String topic;
    private String sensorDataJSON = "[]";
    @ManyToOne
    private Zone zone;
    @ManyToOne(fetch = FetchType.EAGER)
    private DiningTable diningTable;

    @ManyToOne(fetch = FetchType.EAGER)
    private Device device;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    public Sensor(Long id, String name, String topic, Zone zone, DiningTable diningTable, Device device, Category category) {
        this.id = id;
        this.name = name;
        this.topic = topic;
        this.zone = zone;
        this.diningTable = diningTable;
        this.device = device;
        this.category = category;
    }

    public Sensor() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Zone getZone() {
        return zone;
    }

    public String getZoneName() {
        return zone.getTitle();
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public DiningTable getDiningTable() {
        return diningTable;
    }

    public String getDiningTableName() {
        if (this.diningTable != null)
            return String.valueOf(diningTable.getNumber());
        else
            return "";
    }

    public void setDiningTable(DiningTable diningTable) {
        this.diningTable = diningTable;
    }

    public Device getDevice() {
        return device;
    }

    public String getDeviceName() {
        if (this.device != null)
            return device.getName();
        else
            return "";
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCategoryName() {
        try {
            return category.getTitle();
        } catch (NullPointerException e) {
            return "";
        }
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }

/*
    @Autowired
    private ObjectMapper objectMapper;
    public void serializeCustomerAttributes() throws JsonProcessingException {
        this.sensorDataJSON = objectMapper.writeValueAsString(sensorData);
    }
    public void deserializeCustomerAttributes() throws IOException {
        this.sensorData = objectMapper.readValue(sensorDataJSON, HashMap.class);
    }
*/

}