package com.miola.mcr.Services;

import com.miola.mcr.Dao.SensorRepository;
import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SensorServiceImp implements SensorService{


    private final SensorRepository sensorRepository;

    @Autowired
    public SensorServiceImp(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Override
    public List<String> getAllSensorsNames() {
            List<String> sensorsNames = new ArrayList<>();
            sensorsNames.add("fake sensor");
            for (Sensor sensor : sensorRepository.findAll()) {
                sensorsNames.add(sensor.getName());
            }
            return sensorsNames;

    }

    @Override
    public void testAlerte() {

    }

    @Override
    public Sensor findSensorByName(String name) {
        return sensorRepository.findByName(name);
    }
}
