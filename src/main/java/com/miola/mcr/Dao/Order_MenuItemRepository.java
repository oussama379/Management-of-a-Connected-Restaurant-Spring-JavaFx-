package com.miola.mcr.Dao;

import com.miola.mcr.Entities.MenuItem;
import com.miola.mcr.Entities.Order;
import com.miola.mcr.Entities.Order_MenuItem;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface Order_MenuItemRepository extends JpaRepository<Order_MenuItem, Long> {

    Order_MenuItem findByOrderAndMenuItem(Order order, MenuItem menuItem);
    List<Order_MenuItem> findByOrder(Order order);
    @Transactional
    void deleteByOrder(Order order);
    @Transactional
    void deleteByOrderAndMenuItem(Order order, MenuItem menuItem);
    //int removeByOrder(Order order);
}