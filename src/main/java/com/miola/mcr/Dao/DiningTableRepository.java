package com.miola.mcr.Dao;

import com.miola.mcr.Entities.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {

    DiningTable findByNumber(int number);

}