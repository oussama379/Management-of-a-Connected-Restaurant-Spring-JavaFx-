package com.miola.mcr.Dao;

import com.miola.mcr.Entities.Order;
import com.miola.mcr.Entities.User;
import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    //Order findByUser(User user);
    Order findById(long id);
}