package com.miola.mcr.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.miola.mcr.Dao.SensorRepository;
import com.miola.mcr.Entities.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class DBEnergyService {

    // static variable to hold the message, and be used in all the functions in this service
    //{"date":"2022-01-08 12:16:20","energyConsumption":6.735382944183295,"idSensor":3}
    private final SensorRepository sensorRepository;
    private final SensorService sensorService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private JsonNode jsonNode;
    private final double costForKwH = 1.17;
    private static double consumptionToday;
    public static double consumptionCostYesterday;
    private Map<String, Double> consumptionDevices = new HashMap<>();
    private static Map<Long, Double> consumptionSensors = new LinkedHashMap<>();
    private static List<Map.Entry<String, Double> > deviceConsumptionList = new ArrayList<>();


    @Autowired
    public DBEnergyService(SensorRepository sensorRepository, SensorService sensorService) throws ParseException, JsonProcessingException {
        this.sensorRepository = sensorRepository;
        this.sensorService = sensorService;

        // TODO handle exceptions
        getCostYesterday();

    }



    @ServiceActivator(inputChannel="mqttInputChannel")
    public void handleHere(@Payload Object payload) throws IOException {
        System.out.println("payload :  "+payload);
        jsonNode = objectMapper.readTree((String) payload);

        // TODO initialise consumptionToday from Data base
        consumptionToday = consumptionToday + Double.parseDouble(String.valueOf(jsonNode.get("energyConsumption")));

        updateSensorDate(jsonNode);
        sortDevicesByConsumption();

        System.out.println(consumptionSensors);
        System.out.println(consumptionDevices);
        System.out.println(deviceConsumptionList);


    }

    public void getCostYesterday() throws JsonProcessingException, ParseException {
        double consumptionYesterday = 0;
        ArrayNode superArray = objectMapper.createArrayNode();
        for (Sensor S: sensorRepository.findSensorByTopic("EnergyDB")) {
            String sensorData = sensorRepository.selectSensorDataJSON(S.getId());
            ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(sensorData);
            superArray.addAll(arrayNode);
        }

        Calendar c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday

        Calendar c3 = Calendar.getInstance();// today

        for (JsonNode jsonNode : superArray){
            //System.out.println(jsonNode);
            Calendar c2 = Calendar.getInstance();
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonNode.get("date").asText());
            c2.setTime(date);

            if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                    && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                consumptionYesterday = consumptionYesterday + Double.parseDouble(String.valueOf(jsonNode.get("energyConsumption")));

            }

            if (c3.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                    && c3.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                // TODO To be checked
                consumptionToday = consumptionToday + Double.parseDouble(String.valueOf(jsonNode.get("energyConsumption")));

            }

        }

        consumptionCostYesterday =  consumptionYesterday*costForKwH;
    }

    public void sortDevicesByConsumption() throws JsonProcessingException {
        // BEGIN : sort devices by the consumption of each one : device -> Energy Sensor
        for (Sensor S: sensorRepository.findSensorByTopic("EnergyDB")) {
            double C = 0;
            String sensorData = sensorRepository.selectSensorDataJSON(S.getId());
            ArrayNode sensorArray = (ArrayNode) objectMapper.readTree(sensorData);

            for (JsonNode jsonNode : sensorArray) {
                C = C + Double.parseDouble(String.valueOf(jsonNode.get("energyConsumption")));
            }
            consumptionSensors.put(S.getId(), C);

        }
        for (Map.Entry<Long, Double> entry : consumptionSensors.entrySet()) {
            Sensor S = sensorService.getSensorById(entry.getKey()).orElse(null);
            consumptionDevices.put(S.getDeviceName(), entry.getValue());

        }
        deviceConsumptionList = new ArrayList<Map.Entry<String, Double> >(
                consumptionDevices.entrySet());


        Collections.sort(deviceConsumptionList, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return (int) (o2.getValue() - o1.getValue());
            }
        });
        // End : sort devices by the consumption of each one
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

