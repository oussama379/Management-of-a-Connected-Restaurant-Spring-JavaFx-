package com.miola.mcr.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.miola.mcr.Dao.SensorRepository;
import com.miola.mcr.Entities.Sensor;
import com.miola.mcr.Entities.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DBAirService {
    // {"date":"2022-01-14 09:20:33","humidity":60.155631968437994,"idSensor":8,"temperature":33.466944974342724}
    private final SensorRepository sensorRepository;
    private final SensorService sensorService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private JsonNode jsonNode;
    private static Map<String, ArrayList> ZoneData_Temp_Humi = new LinkedHashMap<>();
    private static Map<String, ArrayList> ZoneData_co2_voc = new LinkedHashMap<>();

    @Autowired
    public DBAirService(SensorRepository sensorRepository, SensorService sensorService) throws ParseException, JsonProcessingException {
        this.sensorRepository = sensorRepository;
        this.sensorService = sensorService;
    }


    @ServiceActivator(inputChannel="mqttInputChannel2")
    public void handleHere2(@Payload Object payload) throws JsonProcessingException {
        System.out.println("payload 2 : "+payload);

        jsonNode = objectMapper.readTree((String) payload);
        //updateSensorDate(jsonNode);

        Sensor S = sensorService.getSensorById(Long.parseLong(jsonNode.get("idSensor").asText())).orElse(null);



        if(jsonNode.get("typeAir").asText().equals("AirQuality"))
            ZoneData_co2_voc.put(S.getZone().getTitle(),
                    new ArrayList<>(Arrays.asList(jsonNode.get("co2").asText(), jsonNode.get("voc").asText())));
        if(jsonNode.get("typeAir").asText().equals("AirCondionner"))
            ZoneData_Temp_Humi.put(S.getZone().getTitle(),
                    new ArrayList<>(Arrays.asList(jsonNode.get("temperature").asText(), jsonNode.get("humidity").asText())));


        System.out.println(ZoneData_co2_voc);
        System.out.println(ZoneData_Temp_Humi);
    }

    public void updateSensorDate(JsonNode jsonNode) throws JsonProcessingException {
        String sensorOldData = sensorRepository.selectSensorDataJSON(Long.parseLong(jsonNode.get("idSensor").asText()));
        ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(sensorOldData);
        arrayNode.add(jsonNode);
        String sensorNewData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
        sensorRepository.UpdateSensor(Long.parseLong(jsonNode.get("idSensor").asText()), sensorNewData);
    }

    // To be used to passe the sensor to the testealertes method
    public Sensor getSensorFromJson(JsonNode jsonNode){
        return sensorService.getSensorById(Long.parseLong(jsonNode.get("idSensor").asText())).orElse(null);
    }

}