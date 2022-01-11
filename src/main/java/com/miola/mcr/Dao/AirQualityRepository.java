package com.miola.mcr.Dao;

import com.miola.mcr.Entities.AirConditioner;
import com.miola.mcr.Entities.AirQuality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirQualityRepository extends DeviceRepository<AirQuality>{
    long count();
}