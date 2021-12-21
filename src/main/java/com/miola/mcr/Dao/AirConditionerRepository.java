package com.miola.mcr.Dao;

import com.miola.mcr.Entities.AirConditioner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirConditionerRepository extends JpaRepository<AirConditioner, Long> {
}