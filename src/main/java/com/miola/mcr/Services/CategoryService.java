package com.miola.mcr.Services;

import com.miola.mcr.Entities.Category;

import java.util.List;

public interface CategoryService {

    List<String> getAllCategoriesNames();
    Category getCategoryByName(String title);

}
