package com.miola.mcr.Services;

import com.miola.mcr.Entities.Category;
import com.miola.mcr.Entities.Zone;

import java.util.List;

public interface CategoryService {

    List<String> getAllCategoriesNames();
    Category getCategoryByName(String title);
    boolean saveCategory (Category category);
    List<Category> getAllCategories();
    boolean editCategory(Category category);
    boolean deleteCategoryId(Long Id);
    List<String> getCategoryAlertsNames(Category category);
    List<String> getCategorySensorsNames(Category category);
}
