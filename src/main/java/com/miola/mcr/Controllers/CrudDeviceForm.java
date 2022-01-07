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
        // Set Validation
        this.setFieldsValidators();
    }

    @FXML
    void cancel(ActionEvent event) {
        this.closeWindow();
    }

    @FXML
    void save(ActionEvent event) {
        boolean fieldsValidation = tfName.isValid() && cbDeviceType.isValid();
        if (fieldsValidation){
            Device deviceToEditOrSave = null;
            if(getDeviceType().equals("Air Conditioner")) deviceToEditOrSave = new AirConditioner();
            if(getDeviceType().equals("Air Quality"))  deviceToEditOrSave = new AirQuality();
            if(getDeviceType().equals("Energy Monitor")) deviceToEditOrSave = new EnergyMonitor();
            deviceToEditOrSave.setName(getName());

            if (isAnEdit){
                deviceToEditOrSave.setId(this.idDevice);
                if (deviceService.editDevice(deviceToEditOrSave)){
                    fxWeaver.getBean(CrudDevice.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudDevice.class).showAlter(0);
                }
            }else{
                System.out.println(deviceToEditOrSave);
                if (deviceService.saveDevice(deviceToEditOrSave)){
                    fxWeaver.getBean(CrudDevice.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudDevice.class).showAlter(0);
                }
            }
        }else{
            fxWeaver.getBean(CrudDevice.class).showAlter(4);
        }

    }

    public void fillData(String name, Long idDevice, String deviceType){
        //System.out.println(idDevice);
        this.idDevice = idDevice;
        tfName.setText(name);
        if(deviceType.equals("AirConditioner")) {cbDeviceType.getSelectionModel().selectItem("Air Conditioner"); }
        if(deviceType.equals("AirQuality"))  {cbDeviceType.getSelectionModel().selectItem("Air Quality"); }
        if(deviceType.equals("EnergyMonitor")) {cbDeviceType.getSelectionModel().selectItem("Energy Monitor"); }
        cbDeviceType.setDisable(true);
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

        // Type ComboBox : Required
        cbDeviceType.setValidated(true);
        cbDeviceType.getValidator().add(
                BindingUtils.toProperty(cbDeviceType.getSelectionModel().selectedIndexProperty().isNotEqualTo(-1)),
                "A value must be selected"
        );
    }

    public void clearFields(){
        tfName.clear();
        cbDeviceType.getSelectionModel().clearSelection();
    }


    public String getName() {
        return tfName.getText();
    }
    public String getDeviceType() {
        return cbDeviceType.getSelectedValue();
    }

}

