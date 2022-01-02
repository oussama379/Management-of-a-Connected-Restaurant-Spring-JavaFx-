package com.miola.mcr.Dao;

import com.miola.mcr.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Category, Long> {


    Category findByTitle(String title);
}