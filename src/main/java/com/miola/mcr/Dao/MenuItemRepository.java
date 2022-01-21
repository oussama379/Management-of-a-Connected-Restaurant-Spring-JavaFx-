package com.miola.mcr.Dao;

import com.miola.mcr.Entities.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    MenuItem findByTitle(String title);
}