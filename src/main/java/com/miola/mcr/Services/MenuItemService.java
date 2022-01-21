package com.miola.mcr.Services;

import com.miola.mcr.Entities.DiningTable;
import com.miola.mcr.Entities.MenuItem;

import java.util.List;

public interface MenuItemService {
    List<MenuItem> getAllMenuItems();
    boolean deleteMenuItemById(Long Id);
    boolean saveMenuItem (MenuItem menuItem);
    boolean editMenuItem(MenuItem menuItem);
    List<String> getAllMenuItemsNames();
    MenuItem getMenuItemByTitle(String title);
}
