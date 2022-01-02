package com.miola.mcr.Services;


import com.miola.mcr.Dao.DiningTableRepository;
import com.miola.mcr.Entities.DiningTable;
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
        TablesNames.add("fake device");
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
}
