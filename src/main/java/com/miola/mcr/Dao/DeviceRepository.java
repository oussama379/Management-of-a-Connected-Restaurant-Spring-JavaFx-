package com.miola.mcr.Dao;

import com.miola.mcr.Entities.Device;
import com.miola.mcr.Entities.DevicePower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository<T extends Device> extends JpaRepository<T, Long> {
    long count();
    Device findByName(String name);
    List<Device> findByPower(DevicePower power);

    //SELECT * FROM device WHERE power = 'ON';

}