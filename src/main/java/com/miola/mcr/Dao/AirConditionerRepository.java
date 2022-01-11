package com.miola.mcr.Dao;

import com.miola.mcr.Entities.AirConditioner;
import com.miola.mcr.Entities.EnergyMonitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirConditionerRepository extends DeviceRepository<AirConditioner>{
    long count();
}