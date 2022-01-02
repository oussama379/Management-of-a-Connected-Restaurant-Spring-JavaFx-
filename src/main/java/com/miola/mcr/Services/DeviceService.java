package com.miola.mcr.Services;

import com.miola.mcr.Entities.Device;
import com.miola.mcr.Entities.DiningTable;

import java.util.List;

public interface DeviceService {

    List<String> getAllDevicesNames();
    public void changePower();
    Device getDeviceByName(String name);
}
