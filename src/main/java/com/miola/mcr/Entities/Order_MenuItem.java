package com.miola.mcr.Entities;


import javax.persistence.*;

@Entity
public class Order_MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_menu_item_id", nullable = false)
    private Long order_menuItem_id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    private MenuItem menuItem;

    private Long Quantity;

    public Long getOrder_menuItem_id() {
        return order_menuItem_id;
    }

    public void setOrder_menuItem_id(Long order_menuItem_id) {
        this.order_menuItem_id = order_menuItem_id;
    }


    public Order_MenuItem(Order order, MenuItem menuItem, Long quantity) {
        this.order = order;
        this.menuItem = menuItem;
        Quantity = quantity;
    }

    public Order_MenuItem(){}


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public Long getQuantity() {
        return Quantity;
    }

    public void setQuantity(Long quantity) {
        Quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order_MenuItem{" +
                "order_menuItem_id=" + order_menuItem_id +
                ", order=" + order +
                ", menuItem=" + menuItem +
                ", Quantity=" + Quantity +
                '}';
    }
}
