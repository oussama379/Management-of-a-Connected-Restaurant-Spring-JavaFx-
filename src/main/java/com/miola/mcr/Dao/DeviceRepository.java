package com.miola.mcr.Dao;

import com.miola.mcr.Entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}