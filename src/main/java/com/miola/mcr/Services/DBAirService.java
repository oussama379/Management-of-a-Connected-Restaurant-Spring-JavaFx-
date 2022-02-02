package com.miola.mcr.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.miola.mcr.Controllers.NotificationsLauncher;
import com.miola.mcr.Dao.SensorRepository;
import com.miola.mcr.Entities.Alerte;
import com.miola.mcr.Entities.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

@Component
public class DBAirService {
    // {\"date\":\"2022-01-14 09:20:33\",\"humidity\":60.155631968437994,\"idSensor\"4,\"temperature\":33.466944974342724, \"typeAir\":\"AirCondionner\"}
    private final AlertesService alertesService;
    private final ZoneService zoneService;
    private final SensorRepository sensorRepository;
    private final SensorService sensorService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private JsonNode jsonNode;
    public static Map<String, ArrayList<String>> ZoneData_Temp_Humi = new LinkedHashMap<>();
    public static Map<String, ArrayList<String>> ZoneData_co2_voc = new LinkedHashMap<>();

    @Autowired
    public DBAirService(AlertesService alertesService, ZoneService zoneService, SensorRepository sensorRepository, SensorService sensorService) throws ParseException, JsonProcessingException {
        this.alertesService = alertesService;
        this.zoneService = zoneService;
        this.sensorRepository = sensorRepository;
        this.sensorService = sensorService;

        initCollections();
    }


    @ServiceActivator(inputChannel="mqttInputChannel2")
    public void handleHere2(@Payload Object payload) throws JsonProcessingException {
        System.out.println("payload 2 : "+payload);
        System.out.println("=================================================================================");
        jsonNode = objectMapper.readTree((String) payload);
        //updateSensorDate(jsonNode);
        System.out.println("=================================================================================");
        Sensor S = sensorService.getSensorById(Long.parseLong(jsonNode.get("idSensor").asText())).orElse(null);
        System.out.println("=================================================================================");
        Set<Alerte> triggeredAlertes = new HashSet<>();
        System.out.println("=================================================================================");
        if(jsonNode.get("typeAir").asText().equals("AirQuality"))
            triggeredAlertes.addAll(alertesService.TestAlerts(S, jsonNode.get("temperature").asDouble(), NotificationsLauncher.AlertType.Temperature));
            ZoneData_co2_voc.put(S.getZone().getTitle(),
                    new ArrayList<>(Arrays.asList(jsonNode.get("co2").asText(), jsonNode.get("voc").asText())));
        if(jsonNode.get("typeAir").asText().equals("AirCondionner")){
            System.out.println("=================================================================================");
            triggeredAlertes.addAll(alertesService.TestAlerts(S, jsonNode.get("co2").asDouble(), NotificationsLauncher.AlertType.CO2));
            System.out.println("=================================================================================");
            triggeredAlertes.addAll(alertesService.TestAlerts(S, jsonNode.get("voc").asDouble(), NotificationsLauncher.AlertType.VOC));
            System.out.println("=================================================================================");
            ZoneData_Temp_Humi.put(S.getZone().getTitle(),
                    new ArrayList<>(Arrays.asList(jsonNode.get("temperature").asText(), jsonNode.get("humidity").asText())));
            System.out.println("=================================================================================");
        }
        for (Alerte a: triggeredAlertes) {
            new NotificationsLauncher(NotificationsLauncher.AlertType.valueOf(a.getType()), NotificationsLauncher.Severity.valueOf(a.getSeverity()), S.getName(), a.getOperator(), a.getValue()).showTopRight();
        }

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

    public void initCollections(){
        for (String s: zoneService.getAllZonesNames()) {
            ArrayList<String> array = new ArrayList<>();
            array.add("0");array.add("0");
            ZoneData_Temp_Humi.put(s, array);
            ZoneData_co2_voc.put(s, array);
        }

    }
}