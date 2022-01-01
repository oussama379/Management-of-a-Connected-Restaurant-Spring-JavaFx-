package com.miola.mcr.Services;

import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.Sensor;

import java.util.List;

public interface SensorService {

    List<String> getAllSensorsNames();
    public void testAlerte() ;
    Sensor findSensorByName(String name);
}
