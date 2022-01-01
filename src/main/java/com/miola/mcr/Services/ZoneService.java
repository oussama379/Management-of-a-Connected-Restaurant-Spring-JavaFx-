
package com.miola.mcr.Services;

import com.miola.mcr.Entities.*;

import java.util.List;

public interface ZoneService {
    boolean saveZone (Zone zone);
    List<Zone> getAllZones();
    boolean editZone(Zone zone);
    boolean deleteZoneById(Long Id);
    Zone getZoneById(Long Id);
    List<Device> getZoneDevices(Zone zone);
    List<Sensor> getZoneSensors(Zone zone);
    List<Role> getZoneRoles(Zone zone);

    List<String> getZoneDevicesNames(Zone zone);
    List<String> getZoneSensorsNames(Zone zone);
    List<String> getZoneRolesNames(Zone zone);
}
