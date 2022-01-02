package com.miola.mcr.Services;

import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.Zone;

import java.util.List;

public interface RoleService {
    List<String> getAllRolesNames();
    Role findRoleByTitle(String title);
    boolean saveRole(Role role);
    List<Role> getAllRoles();

    List<String> getRoleUsersNames(Role role);
    List<String> getRolePermissionsNames(Role role);
    List<String> getRoleZonesNames(Role role);
    boolean deleteRoleById(Long Id);
    boolean editRole(Role role);
}

