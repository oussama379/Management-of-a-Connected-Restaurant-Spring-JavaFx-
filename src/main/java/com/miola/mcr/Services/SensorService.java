package com.miola.mcr.Services;

import com.miola.mcr.Entities.Sensor;
import com.miola.mcr.Entities.User;

import java.util.List;
import java.util.Optional;

public interface SensorService {

    List<String> getAllSensorsNames();
    List<Sensor> getAllSensors();
    public void testAlerte() ;
    Sensor findSensorByName(String name);
    Optional<Sensor> getSensorById(Long Id);
    boolean deleteSensorById(Long Id);
    boolean saveSensor(Sensor sensor);
    boolean editSensor(Sensor sensor);


}
