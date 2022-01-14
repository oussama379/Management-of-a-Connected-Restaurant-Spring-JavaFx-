package com.miola.mcr.Dao;

import com.miola.mcr.Entities.Device;
import com.miola.mcr.Entities.DevicePower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface DeviceRepository<T extends Device> extends JpaRepository<T, Long> {
    long count();
    Device findByName(String name);
    List<Device> findByPower(DevicePower power);

    //SELECT * FROM device WHERE power = 'ON';

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Device d SET d.power = :devicePower WHERE d.id = :deviceId")
    int UpdateDevicePower(@Param("devicePower") DevicePower devicePower, @Param("deviceId") Long deviceId);

}