package com.miola.mcr.Dao;

import com.miola.mcr.Entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
}