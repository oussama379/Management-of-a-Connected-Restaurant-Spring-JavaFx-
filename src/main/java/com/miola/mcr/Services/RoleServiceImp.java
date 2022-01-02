package com.miola.mcr.Services;

import com.miola.mcr.Dao.RoleRepository;
import com.miola.mcr.Entities.*;
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

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<String> getRoleUsersNames(Role role) {
        List<String> UsersNames = new ArrayList<>();
        for(User U : role.getUsers())
            UsersNames.add(U.getName());
        return UsersNames;
    }

    @Override
    public List<String> getRolePermissionsNames(Role role) {
        List<String> PermissionsNames = new ArrayList<>();
        for(Permission P : role.getPermissions())
            PermissionsNames.add(P.getTitle());
        return PermissionsNames;
    }

    @Override
    public List<String> getRoleZonesNames(Role role) {
        List<String> ZonesNames = new ArrayList<>();
        for(Zone Z : role.getZones())
            ZonesNames.add(Z.getTitle());
        return ZonesNames;
    }

    @Override
    public boolean deleteRoleById(Long Id) {
        if (roleRepository.existsById(Id)){
            roleRepository.deleteById(Id);
            return true;
        }
        return false;
    }

    @Override
    public boolean editRole(Role role) {
        try {
            if (roleRepository.existsById(role.getId())) {
                roleRepository.save(role);
                return true;
            }else return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
