package com.miola.mcr.Services;

import com.miola.mcr.Dao.CategorieRepository;
import com.miola.mcr.Dao.DiningTableRepository;
import com.miola.mcr.Entities.Category;
import com.miola.mcr.Entities.DiningTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService{


    private final CategorieRepository categorieRepository;

    @Autowired
    public CategoryServiceImp(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @Override
    public List<String> getAllCategoriesNames() {
        List<String> CategoriesNames = new ArrayList<>();
        CategoriesNames.add("fake device");
        for (Category category : categorieRepository.findAll()) {
            CategoriesNames.add(category.getTitle());
        }
        return CategoriesNames;
    }

    @Override
    public Category getCategoryByName(String title) {
        return categorieRepository.findByTitle(title);
    }
}
