package com.miola.mcr.Services;

import com.miola.mcr.Dao.PermissionRepository;
import com.miola.mcr.Entities.Permission;
import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImp implements PermissionService{
    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionServiceImp(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public List<String> getPermissionRolesNames(Permission permission) {
        List<String> rolesNames = new ArrayList<>();
        for(Role r : permission.getRoles())
            rolesNames.add(r.getTitle());
        return rolesNames;
    }

    @Override
    public boolean deletePermissionById(Long id) {
        if (permissionRepository.existsById(id)){
            permissionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean editPermission(Permission permission) {
        try {
            if (permissionRepository.existsById(permission.getId())) {
                permissionRepository.save(permission);
                return true;
            }else return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean savePermission(Permission permission) {
        try{
            permissionRepository.save(permission);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
