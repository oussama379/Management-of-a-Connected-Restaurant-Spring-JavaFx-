package com.miola.mcr.Services;

import com.miola.mcr.Dao.CategorieRepository;
import com.miola.mcr.Dao.DiningTableRepository;
import com.miola.mcr.Entities.*;
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

    @Override
    public boolean saveCategory(Category category) {
        try{
            categorieRepository.save(category);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Category> getAllCategories() {
        return categorieRepository.findAll();
    }

    @Override
    public boolean editCategory(Category category) {
        try {
            if (categorieRepository.existsById(category.getId())) {
                categorieRepository.save(category);
                return true;
            }else return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCategoryId(Long Id) {
        if (categorieRepository.existsById(Id)){
            categorieRepository.deleteById(Id);
            return true;
        }
        return false;
    }

    @Override
    public List<String> getCategoryAlertsNames(Category category) {
        List<String> alertsNames = new ArrayList<>();
        for(Alerte a : category.getAlerts())
            alertsNames.add(a.getType());
        return alertsNames;
    }

    @Override
    public List<String> getCategorySensorsNames(Category category) {
            List<String> sensorsNames = new ArrayList<>();
            for(Sensor s : category.getSensors())
                sensorsNames.add(s.getName());
            return sensorsNames;
        }
    }

