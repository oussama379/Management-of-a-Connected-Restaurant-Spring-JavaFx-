package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.User;
import com.miola.mcr.Services.RoleService;
import com.miola.mcr.Services.UserService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class CrudUserForm implements Initializable {

    private final UserService userService;
    private final RoleService roleService;

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

    private Long idUser;

    @Autowired
    public CrudUserForm(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO Fill cbRole (Role ComboBox) ILYAS -> this roleService.getAllRolesNames() returns a List<String>
//        ObservableList<String> roles = FXCollections.observableArrayList(roleService.getAllRolesNames());

        // Temporary
        cbRole.getItems().addAll(roleService.getAllRolesNames());
    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {
        User userToEditOrAdd = new User();
        userToEditOrAdd.setName(getName());
        userToEditOrAdd.setUsername(getUsername());
        userToEditOrAdd.setPassword(getPassword());
        userToEditOrAdd.setRole(roleService.findRoleByTitle(getRole()));

        if (isAnEdit){
            userToEditOrAdd.setId(this.idUser);
            try {
                System.out.print(roleService.findRoleByTitle(getRole()).toString());
                userService.editUser(userToEditOrAdd);
                // TODO show Confirmation Msg
            }catch (Exception e){
                // TODO show Error Msg
            }
        }else{
           try {
               System.out.print(roleService.findRoleByTitle(getRole()).toString());
               userService.saveUser(userToEditOrAdd);
               // TODO show Confirmation Msg
           }catch (Exception e){
               // TODO show Error Msg
           }
        }

    }


    public void fillData(String name, String username, Long idUser, String roleName){
        //System.out.print(roleName);
        this.idUser = idUser;
        tfName.setText(name);
        tfUsername.setText(username);
        // TODO setSelectedValue not displaying the role -> find other method
        cbRole.setSelectedValue(roleName);
        cbRole.setPromptText(roleName);
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

    public String getRole() {
        return cbRole.getSelectedValue();
    }
}

