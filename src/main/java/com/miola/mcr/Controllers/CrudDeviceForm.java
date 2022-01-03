package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.*;
import com.miola.mcr.Services.DeviceService;
import com.miola.mcr.Services.RoleService;
import com.miola.mcr.Services.UserService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.BindingUtils;
import javafx.beans.property.BooleanProperty;
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
public class CrudDeviceForm implements Initializable {

    private final FxWeaver fxWeaver;
    private final DeviceService deviceService;

    @FXML
    private MFXButton btnCancel;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXComboBox<String> cbDeviceType;

    @FXML
    private MFXTextField tfName;


    private Boolean isAnEdit = false ; // to know difference in save function

    private Long idDevice;

    @Autowired
    public CrudDeviceForm(FxWeaver fxWeaver, DeviceService deviceService) {
        this.fxWeaver = fxWeaver;
        this.deviceService = deviceService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbDeviceType.getItems().addAll("Air Conditioner", "Air Quality", "Energy Monitor");
    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {
        Device deviceToEditOrSave = null;
        if(getDeviceType().equals("Air Conditioner")) deviceToEditOrSave = new AirConditioner();
        if(getDeviceType().equals("Air Quality"))  deviceToEditOrSave = new AirQuality();
        if(getDeviceType().equals("Energy Monitor")) deviceToEditOrSave = new EnergyMonitor();
        deviceToEditOrSave.setName(getName());

        if (isAnEdit){
            try {
                //TODO TALK ABOUT EDIT DEVICE
                //System.out.println(this.idDevice);
                deviceToEditOrSave.setId(this.idDevice);
                deviceService.editDevice(deviceToEditOrSave);
            }catch (Exception e){
                // TODO show Error Msg
            }
        }else{
            try {
                deviceService.saveDevice(deviceToEditOrSave);
                // TODO show Confirmation Msg
            }catch (Exception e){
                // TODO show Error Msg
            }
        }

    }

    public void fillData(String name, Long idDevice, String deviceType){
        //System.out.println(idDevice);
        this.idDevice = idDevice;
        tfName.setText(name);
        if(deviceType.equals("AirConditioner")) {cbDeviceType.setSelectedValue("Air Conditioner"); cbDeviceType.setPromptText("Air Conditioner");}
        if(deviceType.equals("AirQuality"))  {cbDeviceType.setSelectedValue("Air Quality"); cbDeviceType.setPromptText("Air Quality");}
        if(deviceType.equals("EnergyMonitor")) {cbDeviceType.setSelectedValue("Energy Monitor"); cbDeviceType.setPromptText("Energy Monitor");}
        cbDeviceType.setDisable(true);
        isAnEdit = true;
    }


    public String getName() {
        return tfName.getText();
    }
    public String getDeviceType() {
        return cbDeviceType.getSelectedValue();
    }



}

