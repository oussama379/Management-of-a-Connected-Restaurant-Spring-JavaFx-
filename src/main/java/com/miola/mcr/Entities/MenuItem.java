package com.miola.mcr.Entities;


import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class MenuItem extends RecursiveTreeObject<MenuItem> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private double price;
    private String Description;

    @OneToMany(mappedBy = "menuItem", fetch = FetchType.EAGER)
    private Set<Order_MenuItem> order_menuItem;

    public Set<Order_MenuItem> getOrder_menuItem() {
        return order_menuItem;
    }

    public void setOrder_menuItem(Set<Order_MenuItem> order_menuItem) {
        this.order_menuItem = order_menuItem;
    }

    public MenuItem(String title, double price, String description) {
        this.title = title;
        this.price = price;
        Description = description;
    }

    public MenuItem(){}

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", Description='" + Description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return id.equals(menuItem.id);
    }

}
