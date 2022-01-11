package com.miola.mcr.Dao;

import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Sensor findByName(String name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Sensor s SET s.sensorDataJSON = :data WHERE s.id = :sensorId")
    int UpdateSensor(@Param("sensorId") Long sensorId, @Param("data") String data);

    @Query("SELECT s.sensorDataJSON FROM Sensor s WHERE s.id = :sensorId")
    String selectSensorDataJSON(@Param("sensorId") Long sensorId);

    @Query("SELECT COUNT(s.id) FROM Sensor s WHERE s.topic = :sensorTopic")
    long count(@Param("sensorTopic") String sensorTopic);

    List<Sensor> findSensorByTopic(String topic);




}


