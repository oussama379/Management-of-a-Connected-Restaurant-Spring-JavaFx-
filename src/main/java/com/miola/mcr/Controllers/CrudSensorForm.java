package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.Sensor;
import com.miola.mcr.Entities.User;
import com.miola.mcr.Entities.Zone;
import com.miola.mcr.Services.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;

@Component
@FxmlView
public class CrudSensorForm implements Initializable {


    private final SensorService sensorService;
    private final DeviceService deviceService;
    private final CategoryService categoryService;
    private final ZoneService zoneService;
    private final DiningTableService diningTableService;


    @FXML
    private MFXButton btnCancel;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXTextField tfName;

    @FXML
    private MFXTextField tfTopic;

    @FXML
    private MFXComboBox<String> cbDevice;
    @FXML
    private MFXComboBox<String> cbCategory;
    @FXML
    private MFXComboBox<String> cbDTable;
    @FXML
    private MFXComboBox<String> cbZone;

    private Boolean isAnEdit = false ; // to know difference in save function

    private Long idSensor;


    @Autowired
    public CrudSensorForm(SensorService sensorService, DeviceService deviceService, CategoryService categoryService, ZoneService zoneService, DiningTableService diningTableService) {
        this.sensorService = sensorService;
        this.deviceService = deviceService;
        this.categoryService = categoryService;
        this.zoneService = zoneService;
        this.diningTableService = diningTableService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbDevice.getItems().addAll(deviceService.getAllDevicesNames());
        cbCategory.getItems().addAll(categoryService.getAllCategoriesNames());
        cbDTable.getItems().addAll(diningTableService.getAllTablesNumbers());
        cbZone.getItems().addAll(zoneService.getAllZonesNames());
        cbDevice.setPromptText("See Devices");
        cbCategory.setPromptText("See Categories");
        cbDTable.setPromptText("See Dinning Tables");
        cbZone.setPromptText("See Zones");
    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {
        Sensor sensorToEditOrAdd = new Sensor();
        sensorToEditOrAdd.setName(getName());
        sensorToEditOrAdd.setTopic(getTopic());
        if(getCategory() != null) sensorToEditOrAdd.setCategory(categoryService.getCategoryByName(getCategory()));
        if(getTable() != null) sensorToEditOrAdd.setDiningTable(diningTableService.getDiningTableByNumber(Integer.parseInt(getTable())));
        if(getDevice() != null) sensorToEditOrAdd.setDevice(deviceService.getDeviceByName(getDevice()));
        if(getZone() != null) sensorToEditOrAdd.setZone(zoneService.getZoneByName(getZone()));

        if (isAnEdit){
            sensorToEditOrAdd.setId(this.idSensor);
            try {
                sensorService.editSensor(sensorToEditOrAdd);
                // TODO show Confirmation Msg
            }catch (Exception e){
                // TODO show Error Msg
            }
        }else{
            try {
                sensorService.saveSensor(sensorToEditOrAdd);
                // TODO show Confirmation Msg
            }catch (Exception e){
                // TODO show Error Msg
            }
        }

    }


    public void fillData(String Name, Long IdSensor, String Topic, String Category, String Device, String DiningTableName, String Zone){
        this.idSensor = IdSensor;
        tfName.setText(Name);
        tfTopic.setText(Topic);

        cbDevice.setSelectedValue(Device);
        cbDevice.setPromptText(Device);

        cbCategory.setSelectedValue(Category);
        cbCategory.setPromptText(Category);

        cbDTable.setSelectedValue(DiningTableName);
        cbDTable.setPromptText(DiningTableName);

        cbZone.setSelectedValue(Zone);
        cbZone.setPromptText(Zone);

        isAnEdit = true;
    }

    // TODO : ADD NONE VALUE TO COMBOBOX

    public String getName() {
        return tfName.getText();
    }
    public String getTopic() {
        return tfTopic.getText();
    }
    public String getDevice() {
        return cbDevice.getSelectedValue();
    }
    public String getCategory() {
        return cbCategory.getSelectedValue();
    }
    public String getTable() {
        return cbDTable.getSelectedValue();
    }
    public String getZone() {
        return cbZone.getSelectedValue();
    }





}
