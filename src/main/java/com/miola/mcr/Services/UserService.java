package com.miola.mcr.Services;

import com.miola.mcr.Entities.User;

import java.util.List;

public interface UserService {
 // this interface was developped during the conception stage, to help during the programming of the class UserService
    User authenticate(String username, String password);
    String getUserRole(User user);
    User getUserById(Long Id);
    boolean saveUser(User user);
     //boolean registerUser(User user);
    boolean deleteUserById(Long Id);
    boolean deleteUserByUsername(String username);
    boolean editUser(User user);
    User getUser ();
    List<User> getAllUsers();
    String encryptionMd5(String passwordToHash);
   List<String> getAllUsersNames();



}
