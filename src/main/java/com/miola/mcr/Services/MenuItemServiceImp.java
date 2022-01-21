package com.miola.mcr.Services;

import com.miola.mcr.Dao.MenuItemRepository;
import com.miola.mcr.Entities.Device;
import com.miola.mcr.Entities.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuItemServiceImp implements MenuItemService{

    @Autowired
    private MenuItemRepository menuItemRepository;
    @Override
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Override
    public boolean deleteMenuItemById(Long Id) {
        if (menuItemRepository.existsById(Id)){
            menuItemRepository.deleteById(Id);
            return true;
        }
        return false;
    }

    @Override
    public boolean saveMenuItem(MenuItem menuItem) {
        try{
            menuItemRepository.save(menuItem);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean editMenuItem(MenuItem menuItem) {
        try {
            if (menuItemRepository.existsById(menuItem.getId())) {
                menuItemRepository.save(menuItem);
                return true;
            }else return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> getAllMenuItemsNames() {
        List<String> menuItemsNames = new ArrayList<>();
        for (MenuItem menuItem : menuItemRepository.findAll()) {
            menuItemsNames.add(menuItem.getTitle());
        }
        return menuItemsNames;
    }

    @Override
    public MenuItem getMenuItemByTitle(String title) {
        return menuItemRepository.findByTitle(title);
    }
}
