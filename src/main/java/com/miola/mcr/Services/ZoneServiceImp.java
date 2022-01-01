package com.miola.mcr.Services;

import com.miola.mcr.Dao.ZoneRepository;
import com.miola.mcr.Entities.Device;
import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.Sensor;
import com.miola.mcr.Entities.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ZoneServiceImp implements ZoneService{

    private final ZoneRepository zoneRepository;

    @Autowired
    public ZoneServiceImp(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }


    @Override
    public boolean saveZone(Zone zone) {
        try{
            zoneRepository.save(zone);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    @Override
    public boolean editZone(Zone zone) {
        try {
            if (zoneRepository.existsById(zone.getId())) {
                zoneRepository.save(zone);
                return true;
            }else return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteZoneById(Long Id) {
        if (zoneRepository.existsById(Id)){
            zoneRepository.deleteById(Id);
            return true;
        }
        return false;
    }

    @Override
    public Zone getZoneById(Long Id) {
        return zoneRepository.findById(Id).orElse(null);
    }

    @Override
    public List<Device> getZoneDevices(Zone zone) {
        return null;
    }

    @Override
    public List<Sensor> getZoneSensors(Zone zone) {
        return null;
    }

    @Override
    public List<Role> getZoneRoles(Zone zone) {
        return null;
    }

    @Override
    public List<String> getZoneDevicesNames(Zone zone) {
        /*List<String> DevicesNames = new ArrayList<>();
        for(Zone Z : zones) {
            System.out.println(Z.toString());
            for(Role R : Z.getRoles())
                System.out.println(R.toString());
            for(Sensor S : Z.getSensors())
                System.out.println(S.toString());*/
            return null;

    }


    @Override
    public List<String> getZoneSensorsNames(Zone zone) {
        List<String> sensorsNames = new ArrayList<>();
        for(Sensor s : zone.getSensors())
            sensorsNames.add(s.getName());
        return sensorsNames;
    }

    @Override
    public List<String> getZoneRolesNames(Zone zone) {
        List<String> rolesNames = new ArrayList<>();
        for(Role r : zone.getRoles())
            rolesNames.add(r.getTitle());
        return rolesNames;
    }
}

