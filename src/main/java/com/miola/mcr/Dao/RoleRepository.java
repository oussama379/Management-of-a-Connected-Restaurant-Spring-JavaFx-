package com.miola.mcr.Dao;

import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByTitle(String title);
}