package com.miola.mcr.Services;

import com.miola.mcr.Entities.Device;
import com.miola.mcr.Entities.DiningTable;
import com.miola.mcr.Entities.Zone;

import java.util.List;

public interface DeviceService {

    List<String> getAllDevicesNames();
    public void changePower();
    Device getDeviceByName(String name);
    List<Device> getAllDevices();
    List<String> getDeviceSensorsNames(Device device);
    boolean deleteDeviceById(Long Id);
    boolean editDevice(Device device);
    boolean saveDevice (Device device);
}
