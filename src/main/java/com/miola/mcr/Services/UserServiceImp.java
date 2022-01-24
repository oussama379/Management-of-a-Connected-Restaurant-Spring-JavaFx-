package com.miola.mcr.Services;


import com.miola.mcr.Dao.UserRepository;
import com.miola.mcr.Entities.MenuItem;
import com.miola.mcr.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImp implements UserService{

    private final UserRepository userRepository;
    private User currentUser;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if(user == null){
            return null;
        }else{
            if(encryptionMd5(password).equals(user.getPassword())) {
                currentUser = user;
                return user;
            }
            else return null;
        }

    }



    @Override
    public String getUserRole(User user) {
        return getUserById(user.getId()).getRole().getTitle();
    }

    @Override
    public User getUserById(Long Id) {
        return userRepository.findById(Id).orElse(null);
    }

    @Override
    public boolean saveUser(User user) {
//        if (!userRepository.existsById(user.getId())){
        try{
            String password = user.getPassword();
            user.setPassword(encryptionMd5(password));
            userRepository.save(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
       // }
       // else
      //  return false;
    }



    @Override
    public boolean deleteUserById(Long Id) {
        if (userRepository.existsById(Id)){
            userRepository.deleteById(Id);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUserByUsername(String username) {
        if (userRepository.existsByUsername(username)){
            return true;
        }
        return false;
    }

    @Override
    public boolean editUser(User user) {
        try {
            if (userRepository.existsById(user.getId())) {
                String password = user.getPassword();
                user.setPassword(encryptionMd5(password));
                userRepository.save(user);
                return true;
            }else return false;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUser() {
        return getCurrentUser();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public String encryptionMd5(String passwordToHash){
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(passwordToHash.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    @Override
    public List<String> getAllUsersNames() {
        List<String> usersNames = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            usersNames.add(user.getUsername());
        }
        return usersNames;
    }

}
