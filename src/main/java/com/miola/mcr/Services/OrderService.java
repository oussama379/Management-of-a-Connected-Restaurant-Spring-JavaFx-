package com.miola.mcr.Services;

import com.miola.mcr.Entities.MenuItem;
import com.miola.mcr.Entities.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface OrderService {

    List<Order> getAllOrders();
    boolean deleteOrderById(Long Id);
    boolean saveOrder (Order order);
    boolean editOrder(Order order);
    boolean saveOrderItems(Order order, ArrayList<MenuItem> menuItems);
}
