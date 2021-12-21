package com.miola.mcr.Dao;

import com.miola.mcr.Entities.AirQuality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirQualityRepository extends JpaRepository<AirQuality, Long> {
}