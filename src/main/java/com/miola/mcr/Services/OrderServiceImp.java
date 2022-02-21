package com.miola.mcr.Services;


import com.miola.mcr.Dao.OrderRepository;
import com.miola.mcr.Entities.MenuItem;
import com.miola.mcr.Entities.Order;
import com.miola.mcr.Entities.Order_MenuItem;
import com.miola.mcr.Dao.Order_MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImp implements OrderService{

    private final OrderRepository orderRepository;
    private final Order_MenuItemRepository order_menuItemRepository;

    @Autowired
    public OrderServiceImp(OrderRepository orderRepository, Order_MenuItemRepository order_menuItemRepository) {
        this.orderRepository = orderRepository;
        this.order_menuItemRepository = order_menuItemRepository;
    }


    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public boolean deleteOrderById(Long Id) {
        if (orderRepository.existsById(Id)){
            orderRepository.deleteById(Id);
            return true;
        }
        return false;
    }

    @Override
    public boolean saveOrder(Order order) {
        try{
            orderRepository.save(order);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean editOrder(Order order) {
        try {
            if (orderRepository.existsById(order.getId())) {
                orderRepository.save(order);
                return true;
            }else return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveOrderItems(Order order, ArrayList<MenuItem> menuItems) {
        this.saveOrder(order);
        //System.out.println(menuItems);
        if(menuItems.isEmpty() | !order_menuItemRepository.findByOrder(order).isEmpty()) {
            //For orders with no items, and only special requests
            order_menuItemRepository.deleteByOrder(order);
        }
        for (MenuItem menuItem : menuItems) {
            /*if (order_menuItemRepository.findByOrderAndMenuItem(order, menuItem) != null) {
                order_menuItemRepository.deleteByOrder(order);
            }*/
            if (order_menuItemRepository.findByOrderAndMenuItem(order, menuItem) == null) {
                //System.out.println("No "+menuItem+"In Order"+order);
                Order_MenuItem order_menuItem = new Order_MenuItem();
                order_menuItem.setOrder(order);
                order_menuItem.setMenuItem(menuItem);
                int i = Collections.frequency(menuItems, menuItem);
                //System.out.println(i);
                order_menuItem.setQuantity((long) i);
                order_menuItemRepository.save(order_menuItem);
            }
        }


        return true;
    }

    @Override
    public Order getOrderById(long id) {
        return orderRepository.findById(id);
    }

}
