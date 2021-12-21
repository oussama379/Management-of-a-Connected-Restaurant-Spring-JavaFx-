package com.miola.mcr.Dao;


import com.miola.mcr.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String email);
}
