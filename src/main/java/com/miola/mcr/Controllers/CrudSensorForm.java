package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.Sensor;
import com.miola.mcr.Services.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.BindingUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;

@Component
@FxmlView
public class CrudSensorForm implements Initializable {

    private final FxWeaver fxWeaver;
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
    public CrudSensorForm(FxWeaver fxWeaver, SensorService sensorService, DeviceService deviceService, CategoryService categoryService, ZoneService zoneService, DiningTableService diningTableService) {
        this.fxWeaver = fxWeaver;
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
        // Set Validation
        this.setFieldsValidators();
    }

    @FXML
    void cancel(ActionEvent event) {
        this.closeWindow();
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

        boolean fieldsValidation = tfName.isValid() && tfTopic.isValid() && cbCategory.isValid() && cbZone.isValid();
        if (fieldsValidation){
            if (isAnEdit){
                sensorToEditOrAdd.setId(this.idSensor);
                if (sensorService.editSensor(sensorToEditOrAdd)){
                    fxWeaver.getBean(CrudSensor.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudSensor.class).showAlter(0);
                }
            }else{
                if (sensorService.saveSensor(sensorToEditOrAdd)){
                    fxWeaver.getBean(CrudSensor.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudSensor.class).showAlter(0);
                }
            }
        }else{
            fxWeaver.getBean(CrudSensor.class).showAlter(4);
        }
    }

    public void fillData(String Name, Long IdSensor, String Topic, String Category, String Device, String DiningTableName, String Zone){
        this.idSensor = IdSensor;
        tfName.setText(Name);
        tfTopic.setText(Topic);

        cbDevice.getSelectionModel().selectItem(Device);

        cbCategory.getSelectionModel().selectItem(Category);

        cbDTable.getSelectionModel().selectItem(DiningTableName);

        cbZone.getSelectionModel().selectItem(Zone);

        isAnEdit = true;
    }

    public void closeWindow(){
        this.clearFields();
this.isAnEdit = false;
        Stage formWindow = (Stage) (tfName.getScene().getWindow());
        formWindow.fireEvent(new WindowEvent(formWindow, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void setFieldsValidators(){
        // Name TextField : Required
        tfName.setValidated(true);
        tfName.getValidator().add(
                BindingUtils.toProperty(tfName.textProperty().isNotEmpty()),
                "Required"
        );

        // Topic TextField : Required
        tfTopic.setValidated(true);
        tfTopic.getValidator().add(
                BindingUtils.toProperty(tfTopic.textProperty().isNotEmpty()),
                "Required"
        );

        // Role ComboBox : Required
        cbCategory.setValidated(true);
        cbCategory.getValidator().add(
                BindingUtils.toProperty(cbCategory.getSelectionModel().selectedIndexProperty().isNotEqualTo(-1)),
                "A value must be selected"
        );

        // Role ComboBox : Required
        cbZone.setValidated(true);
        cbZone.getValidator().add(
                BindingUtils.toProperty(cbZone.getSelectionModel().selectedIndexProperty().isNotEqualTo(-1)),
                "A value must be selected"
        );
    }

    public void clearFields(){
        tfName.clear();
        tfTopic.clear();
        cbDevice.getSelectionModel().clearSelection();
        cbCategory.getSelectionModel().clearSelection();
        cbDTable.getSelectionModel().clearSelection();
        cbZone.getSelectionModel().clearSelection();
    }

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
