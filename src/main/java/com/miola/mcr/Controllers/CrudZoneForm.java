package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.User;
import com.miola.mcr.Services.ZoneService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView
public class CrudZoneForm implements Initializable {


    private final ZoneService zoneService;

    @FXML
    private MFXButton btnCancel;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXComboBox<String> cbRole;

    @FXML
    private MFXTextField tfName;

    //TODO (ILYAS) ADD PASSWORD CONFIRMATION TEXTFIELD
    @FXML
    private MFXTextField tfPassword;

    @FXML
    private MFXTextField tfUsername;

    private Boolean isAnEdit = false ; // to know difference in save function

    private Long idZone;

    @Autowired
    public CrudZoneForm(ZoneService zoneService) {
        this.zoneService = zoneService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO Fill cbRole (Role ComboBox) ILYAS

    }

    @FXML
    void cancel(ActionEvent event) {

    }

/*    @FXML
    void save(ActionEvent event) {
        *//*
         *  Audio
         * *//*
        // TODO after saving or editing a user the view need to be reloaded
        if (isAnEdit){
            User userToEdit = new User(this.idUser, getName(), getUsername(), getPassword());
            try {
                userService.editUser(userToEdit);
                // TODO show Confirmation Msg
            }catch (Exception e){
                // TODO show Error Msg
            }
        }else{
            User userToAdd = new User(getName(), getUsername(), getPassword());
            try {
                userService.saveUser(userToAdd);
                // TODO show Confirmation Msg
            }catch (Exception e){
                // TODO show Error Msg
            }
        }

    }*/

    // TODO Add Role to this function ILYAS
    public void fillData(String title, Long idZone){
        this.idZone = idZone;
        tfName.setText(title);
        isAnEdit = true;
    }


    public String getName() {
        return tfName.getText();
    }

    public String getUsername() {
        return tfUsername.getText();
    }

    public String getPassword() {
        return tfPassword.getText();
    }
}
