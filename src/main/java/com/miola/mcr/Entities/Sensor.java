package com.miola.mcr.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

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
    //private JsonObject data;
    @ManyToOne
    private Zone zone;
    @ManyToOne
    private DiningTable diningTable;

    @ManyToOne(fetch = FetchType.EAGER)
    private Device device;

    @ManyToOne
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
            // TODO TO BE FIXED
            return null;
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
            return " ";
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
        return category.getTitle();
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }
}