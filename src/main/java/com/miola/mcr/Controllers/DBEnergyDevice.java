package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.DevicePower;
import com.miola.mcr.Services.DeviceService;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Component
@FxmlView
public class DBEnergyDevice {

    @Autowired
    private DeviceService deviceService;

    @FXML
    private MFXLabel lblDeviceName;

    @FXML
    private MFXLabel lblKwh;

    @FXML
    private MFXToggleButton tbPower;

    @FXML
    private AnchorPane rootPane;

    private static Map<MFXToggleButton, String> views = new HashMap<>();

    @FXML
    void changePower(ActionEvent event) {
        MFXToggleButton e = (MFXToggleButton)event.getSource();
        long id = deviceService.getDeviceByName(views.get(e)).getId();

        e.setText((e.isSelected()) ? "ON":"OFF");
        deviceService.changePower((e.isSelected()) ? DevicePower.ON:DevicePower.OFF, id);
    }

    void fillData(String lblDeviceName, String lblKwh, DevicePower tbPower){
        this.lblDeviceName.setText(lblDeviceName);
        this.lblKwh.setText(lblKwh);
        this.tbPower.setSelected((tbPower==DevicePower.ON) ? true:false);
        this.tbPower.setText(tbPower.toString());
        views.put(this.tbPower, this.lblDeviceName.getText());
    }

}
