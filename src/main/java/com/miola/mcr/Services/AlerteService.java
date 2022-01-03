
package com.miola.mcr.Services;

import com.miola.mcr.Entities.Alerte;
import com.miola.mcr.Entities.User;
import com.miola.mcr.Entities.Zone;

import java.util.List;

public interface AlerteService {
    List<Alerte> getAllAlertes();
    boolean deleteAlerteById(Long Id);
    boolean saveAlerte(Alerte alerte);
    boolean editAlerte(Alerte alerte);
}
