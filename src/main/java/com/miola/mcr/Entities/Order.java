package com.miola.mcr.Entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="Orders")
public class Order extends RecursiveTreeObject<Order> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date_time;
    private double totalPrice;
    private String special_request;
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    private DiningTable diningTable;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    /*@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "order_item", joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "menuitem_id"))*/
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private Set<Order_MenuItem> order_menuItem;


    public Order(String date_time, double totalPrice, String special_request, String comment) {
        this.date_time = date_time;
        this.totalPrice = totalPrice;
        this.special_request = special_request;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date_time='" + date_time + '\'' +
                ", totalPrice=" + totalPrice +
                ", special_request='" + special_request + '\'' +
                ", comment='" + comment;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Order(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getSpecial_request() {
        return special_request;
    }

    public void setSpecial_request(String special_request) {
        this.special_request = special_request;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Order_MenuItem> getOrder_menuItem() {
        return order_menuItem;
    }

    public void setOrder_menuItem(Set<Order_MenuItem> order_menuItem) {
        this.order_menuItem = order_menuItem;
    }

    public DiningTable getDiningTable() {
        return diningTable;
    }
    public String getDiningTableName() {
        return String.valueOf(diningTable.getNumber());
    }

    public void setDiningTable(DiningTable diningTable) {
        this.diningTable = diningTable;
    }
}
