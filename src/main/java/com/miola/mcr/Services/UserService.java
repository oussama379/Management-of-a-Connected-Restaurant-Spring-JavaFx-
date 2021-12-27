package com.miola.mcr.Services;

import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.User;

public interface UserService {
 // this interface was developped during the conception stage, to help during the programming of the class UserService
    User authenticate(String username, String password);
    Role getUserRole(User user);
    User getUserById(Long Id);
    boolean saveUser(User user);
    boolean deleteUserById(Long Id);
    boolean editUser(User user);
    User getUser ();



}
