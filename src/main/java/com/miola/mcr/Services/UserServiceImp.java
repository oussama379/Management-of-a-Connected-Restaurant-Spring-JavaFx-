package com.miola.mcr.Services;


import com.miola.mcr.Dao.UserRepository;
import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService{

    @Autowired
    private UserRepository userRepository;


    @Override
    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if(user == null){
            return false;
        }else{
            if(password.equals(user.getPassword())) return true;
            else return false;
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
        if (!userRepository.existsById(user.getId())){
            userRepository.save(user);
            return true;
        }
        else
        return false;
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
            User u = userRepository.findById(user.getId()).orElse(null);
            user.setId(u.getId());
            userRepository.save(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
