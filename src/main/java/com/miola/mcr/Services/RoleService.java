package com.miola.mcr.Services;

import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.Zone;

import java.util.List;

public interface RoleService {
    List<String> getAllRolesNames();
    Role findRoleByTitle(String title);
    boolean saveRole (Role role);
}

