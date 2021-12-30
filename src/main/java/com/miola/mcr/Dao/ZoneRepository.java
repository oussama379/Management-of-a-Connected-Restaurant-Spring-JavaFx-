package com.miola.mcr.Dao;

import com.miola.mcr.Entities.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ZoneRepository extends JpaRepository<Zone, Long> {

    @Query("select z from Zone z")
    public List<Zone> getAllZones();


}