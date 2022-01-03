package com.miola.mcr.Services;

import com.miola.mcr.Dao.AlerteRepository;
import com.miola.mcr.Entities.Alerte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlerteServiceImp implements AlerteService{
    private final AlerteRepository alerteRepository;

    @Autowired
    public AlerteServiceImp(AlerteRepository alerteRepository) {
        this.alerteRepository = alerteRepository;
    }

    @Override
    public List<Alerte> getAllAlertes() {
        return alerteRepository.findAll();
    }

    @Override
    public boolean deleteAlerteById(Long Id) {
        if (alerteRepository.existsById(Id)){
            alerteRepository.deleteById(Id);
            return true;
        }
        return false;
    }

    @Override
    public boolean saveAlerte(Alerte alerte) {
        try{
            alerteRepository.save(alerte);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean editAlerte(Alerte alerte) {
        try {
            if (alerteRepository.existsById(alerte.getId())) {
                alerteRepository.save(alerte);
                return true;
            }else return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
