package com.miola.mcr.Controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView
public class CrudUserForm implements Initializable {

    @FXML
    private MFXButton btnCancel;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXComboBox<String> cbRole;

    @FXML
    private MFXTextField tfName;

    @FXML
    private MFXTextField tfPassword;

    @FXML
    private MFXTextField tfUsername;

    private Boolean isAnEdit = false ; // to know difference in save function

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO Fill cbRole (Role ComboBox) ILYAS
    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {
        /*
        *  Audio
        * */
        if (isAnEdit){
            // TODO Edit User
        }else{
            // TODO Save User
        }

    }

    // TODO Add Role to this function ILYAS
    public void fillData(String name, String username, String password){
        tfName.setText(name);
        tfUsername.setText(username);
        tfPassword.setText(password);
        isAnEdit = true;
    }
}

