package com.miola.mcr.Services;

import com.miola.mcr.Dao.RoleRepository;
import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class RoleServiceImp implements RoleService{

    private final RoleRepository roleRepository;
    @Autowired
    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<String> getAllRolesNames() {
        List<String> roleNames = new ArrayList<>();
        //roleNames.add("fake role");
        for (Role role : roleRepository.findAll()) {
            roleNames.add(role.getTitle());
        }
        return roleNames;
    }

    @Override
    public Role findRoleByTitle(String title) {
        return roleRepository.findByTitle(title);
    }

    @Override
    public boolean saveRole(Role role) {
        try{
            roleRepository.save(role);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
