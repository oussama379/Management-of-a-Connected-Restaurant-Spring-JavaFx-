package com.miola.mcr.Services;


import com.miola.mcr.Dao.DeviceRepository;
import com.miola.mcr.Entities.Device;
import com.miola.mcr.Entities.DevicePower;
import com.miola.mcr.Entities.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceServiceImp implements DeviceService{


    private final DeviceRepository<Device> deviceRepository;

    @Autowired
    public DeviceServiceImp(DeviceRepository<Device> deviceRepository) {
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
    public boolean changePower(DevicePower devicePower, Long id) {

        int i = deviceRepository.UpdateDevicePower(devicePower,id);
        if(i > 1)
            return true;
        else return false;
    }


    @Override
    public Device getDeviceByName(String name) {
        return deviceRepository.findByName(name);
    }

    @Override
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @Override
    public List<String> getDeviceSensorsNames(Device device) {
        List<String> sensorsNames = new ArrayList<>();
        for(Sensor s : device.getSensors())
            sensorsNames.add(s.getName());
        return sensorsNames;
    }

    @Override
    public boolean deleteDeviceById(Long Id) {
        if (deviceRepository.existsById(Id)){
            deviceRepository.deleteById(Id);
            return true;
        }
        return false;
    }

    @Override
    public boolean editDevice(Device device) {
        try {
            if (deviceRepository.existsById(device.getId())) {
                deviceRepository.save(device);
                return true;
            }else return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveDevice(Device device) {
        try{
            deviceRepository.save(device);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
