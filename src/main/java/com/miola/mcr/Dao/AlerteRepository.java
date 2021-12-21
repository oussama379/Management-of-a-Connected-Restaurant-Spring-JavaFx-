package com.miola.mcr.Dao;

import com.miola.mcr.Entities.Alerte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlerteRepository extends JpaRepository<Alerte, Long> {
}