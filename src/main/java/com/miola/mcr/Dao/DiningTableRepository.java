package com.miola.mcr.Dao;

import com.miola.mcr.Entities.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {

    DiningTable findByNumber(int number);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE DiningTable d SET d.state = :state WHERE d.id = :tableId")
    int UpdateTableState(@Param("tableId") Long tableId, @Param("state") String state);

}