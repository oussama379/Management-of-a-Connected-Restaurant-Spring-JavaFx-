package com.miola.mcr.Dao;

import com.miola.mcr.Entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository<T extends Device> extends JpaRepository<Device, Long> {

    Device findByName(String name);
}