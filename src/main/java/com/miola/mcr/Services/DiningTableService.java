package com.miola.mcr.Services;

import com.miola.mcr.Entities.DiningTable;
import com.miola.mcr.Entities.Zone;

import java.util.List;

public interface DiningTableService {
    List<String> getAllTablesNumbers();
    public void modifyState();
    DiningTable getDiningTableByNumber(int number);
}
