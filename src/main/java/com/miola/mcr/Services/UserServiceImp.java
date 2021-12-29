package com.miola.mcr.Services;


import com.miola.mcr.Dao.UserRepository;
import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImp implements UserService{

    @Autowired
    private UserRepository userRepository;
    private User currentUser;

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
    public Role getUserRole(User user) {
        return getUserById(user.getId()).getRole();
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
    public boolean editUser(User user) {
        try {
            if (userRepository.existsById(user.getId())) {
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

}
