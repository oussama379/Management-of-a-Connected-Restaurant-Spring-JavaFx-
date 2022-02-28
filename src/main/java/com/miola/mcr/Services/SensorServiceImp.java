package com.miola.mcr.Services;

import com.miola.mcr.Dao.SensorRepository;
import com.miola.mcr.Entities.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
//            sensorsNames.add("fake sensor");
            for (Sensor sensor : sensorRepository.findAll()) {
                sensorsNames.add(sensor.getName());
            }
            return sensorsNames;

    }

    @Override
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    @Override
    public void testAlerte() {

    }

    @Override
    public Sensor findSensorByName(String name) {
        return sensorRepository.findByName(name);
    }

    @Override
    public Optional<Sensor> getSensorById(Long Id) {
        return sensorRepository.findById(Id);
    }

    @Override
    public boolean deleteSensorById(Long Id) {
        if (sensorRepository.existsById(Id)){
            sensorRepository.deleteById(Id);
            return true;
        }
        return false;
    }

    @Override
    public boolean saveSensor(Sensor sensor) {
        try{
            sensorRepository.save(sensor);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean editSensor(Sensor sensor) {
        try {
            if (sensorRepository.existsById(sensor.getId())) {
                sensorRepository.save(sensor);
                return true;
            }else return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
