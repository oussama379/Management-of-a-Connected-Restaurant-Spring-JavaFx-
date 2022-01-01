package com.miola.mcr.Services;


import com.miola.mcr.Dao.DeviceRepository;
import com.miola.mcr.Entities.Device;
import com.miola.mcr.Entities.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceServiceImp implements DeviceService{

    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceServiceImp(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }


    @Override
    public List<String> getAllDevicesNames() {
        List<String> devicesNames = new ArrayList<>();
        devicesNames.add("fake device");
        for (Device device : deviceRepository.findAll()) {
            devicesNames.add(device.getName());
        }
        return devicesNames;
    }

    @Override
    public void changePower() {

    }
}
