package com.miola.mcr.Services;

import com.miola.mcr.Entities.Permission;
import com.miola.mcr.Entities.Zone;

import java.util.List;

public interface PermissionService {

    List<Permission> getAllPermissions();
    List<String> getPermissionRolesNames(Permission permission);
    boolean deletePermissionById(Long id);
    boolean editPermission(Permission permission);
    boolean savePermission (Permission permission);
}
