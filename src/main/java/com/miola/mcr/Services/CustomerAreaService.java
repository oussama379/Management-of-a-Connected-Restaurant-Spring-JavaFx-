package com.miola.mcr.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.miola.mcr.Controllers.Orders;
import com.miola.mcr.Dao.SensorRepository;
import com.miola.mcr.Entities.DiningTable;
import com.miola.mcr.Entities.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

@Component
public class CustomerAreaService {

    private final SensorRepository sensorRepository;
    private final SensorService sensorService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private JsonNode jsonNode;
    private static List<DiningTable> Tables = new ArrayList<>();


    @Autowired
    public CustomerAreaService(SensorRepository sensorRepository, SensorService sensorService) throws ParseException, JsonProcessingException {
        this.sensorRepository = sensorRepository;
        this.sensorService = sensorService;
    }


    @ServiceActivator(inputChannel="mqttInputChannel3")
    public void handleHere2(@Payload Object payload) throws JsonProcessingException {
        System.out.println("payload 3 : "+payload);

        //{"idSensor" : 2,"date": "2022-01-15 15:43:02","force":true}
        // "{\"idSensor\" : 2,\"date\": \"2022-01-15 15:43:02\",\"force\":true}"
        jsonNode = objectMapper.readTree((String) payload);
        Orders.nextStatePayload(jsonNode.get("idSensor").asLong(), jsonNode.get("force").asBoolean());
        //Sensor S = sensorService.getSensorById(Long.parseLong(jsonNode.get("idSensor").asText())).orElse(null);

        //String State = jsonNode.get("force").asText();

        //DiningTable D = S.getDiningTable();
        // To update table state : use diningTableRepository.UpdateTableState(1L, "Empty");
    }



}