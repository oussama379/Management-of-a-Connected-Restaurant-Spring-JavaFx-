package com.miola.mcr.Services;


import com.miola.mcr.Dao.DiningTableRepository;
import com.miola.mcr.Entities.DiningTable;
import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.Sensor;
import com.miola.mcr.Entities.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiningTableServiceImp implements DiningTableService{

    private final DiningTableRepository diningTableRepository;

    @Autowired
    public DiningTableServiceImp(DiningTableRepository diningTableRepository) {
        this.diningTableRepository = diningTableRepository;
    }



    @Override
    public List<String> getAllTablesNumbers() {
        List<String> TablesNames = new ArrayList<>();
        for (DiningTable diningTable : diningTableRepository.findAll()) {
            TablesNames.add(String.valueOf(diningTable.getNumber()));
        }
        return TablesNames;
    }

    @Override
    public void modifyState() {

    }

    @Override
    public DiningTable getDiningTableByNumber(int number) {
        return diningTableRepository.findByNumber(number);
    }

    @Override
    public List<DiningTable> getAllDiningTables() {
        return diningTableRepository.findAll();
    }

    @Override
    public List<String> getDiningTableSensorsNames(DiningTable diningTable) {
        List<String> sensorsNames = new ArrayList<>();
        for(Sensor s : diningTable.getSensors())
            sensorsNames.add(s.getName());
        return sensorsNames;
    }

    @Override
    public boolean deleteDiningTableById(Long Id) {
        if (diningTableRepository.existsById(Id)){
            diningTableRepository.deleteById(Id);
            return true;
        }
        return false;

    }

    @Override
    public boolean saveDiningTable(DiningTable diningTable) {
        try{
            diningTableRepository.save(diningTable);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean editDiningTable(DiningTable diningTable) {
        try {
            if (diningTableRepository.existsById(diningTable.getId())) {
                diningTableRepository.save(diningTable);
                return true;
            }else return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
