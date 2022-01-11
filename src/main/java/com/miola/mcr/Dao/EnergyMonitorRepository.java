package com.miola.mcr.Dao;

import com.miola.mcr.Entities.EnergyMonitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnergyMonitorRepository extends DeviceRepository<EnergyMonitor> {
    long count();
}