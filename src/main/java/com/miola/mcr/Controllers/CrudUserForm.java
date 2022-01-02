package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.User;
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
import java.util.ResourceBundle;

@Component
@FxmlView
public class CrudUserForm implements Initializable {

    private final FxWeaver fxWeaver;
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

    @FXML
    private MFXPasswordField pfPassword;

    @FXML
    private MFXPasswordField pfConfirmPassword;
    private BooleanProperty pfConfirmPasswordValidator;

    @FXML
    private MFXTextField tfUsername;

    private Boolean isAnEdit = false ; // to know difference in save function

    private Long idUser;

    @Autowired
    public CrudUserForm(FxWeaver fxWeaver, UserService userService, RoleService roleService) {
        this.fxWeaver = fxWeaver;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cbRole.getItems().addAll(roleService.getAllRolesNames());

        // Set Validation
        this.setFieldsValidators();
    }

    @FXML
    void cancel(ActionEvent event) {
        this.closeWindow();
    }

    @FXML
    void save(ActionEvent event) {
        User userToEditOrAdd = new User();
        userToEditOrAdd.setName(getName());
        userToEditOrAdd.setUsername(getUsername());
        userToEditOrAdd.setPassword(getPassword());
        userToEditOrAdd.setRole(roleService.findRoleByTitle(getRole()));

        boolean fieldsValidation = tfName.isValid() && tfUsername.isValid() && pfPassword.isValid() && pfConfirmPassword.isValid() && cbRole.isValid();
        if (fieldsValidation){
            if (isAnEdit){
                userToEditOrAdd.setId(this.idUser);
                if (userService.editUser(userToEditOrAdd)){
                    fxWeaver.getBean(CrudUser.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudUser.class).showAlter(0);
                }
            }else{
                if (userService.saveUser(userToEditOrAdd)){
                    fxWeaver.getBean(CrudUser.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudUser.class).showAlter(0);
                }
            }
        }else{
            fxWeaver.getBean(CrudUser.class).showAlter(4);
        }

    }


    public void fillData(String name, String username, Long idUser, String roleName){
        //System.out.print(roleName);
        this.idUser = idUser;
        tfName.setText(name);
        tfUsername.setText(username);
        cbRole.setSelectedValue(roleName);
        cbRole.setPromptText(roleName);
        isAnEdit = true;
    }

    public void closeWindow(){
        this.clearFields();
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

        // Username TextField : Required
        tfUsername.setValidated(true);
        tfUsername.getValidator().add(
                BindingUtils.toProperty(tfUsername.textProperty().isNotEmpty()),
                "Required"
        );

        // Password PasswordField : Min 8 chars,
        pfPassword.setValidated(true);
        pfPassword.getValidator().add(
                BindingUtils.toProperty(pfPassword.passwordProperty().length().greaterThanOrEqualTo(8)),
                "Must be at least 8 characters long"
        );

        // Confirm Password PasswordField : match to Password,
        pfConfirmPassword.setValidated(true);
        pfConfirmPasswordValidator = BindingUtils.toProperty(pfConfirmPassword.passwordProperty().isEqualTo(this.getPassword()));
            // Link PasswordField value to ConfirmPasswordField Validation
        pfPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            pfConfirmPassword.getValidator().remove(pfConfirmPasswordValidator);
            pfConfirmPasswordValidator = BindingUtils.toProperty(pfConfirmPassword.passwordProperty().isEqualTo(this.getPassword()));
            pfConfirmPassword.getValidator().add(pfConfirmPasswordValidator, "Passwords do not macth");
        });

        // Role ComboBox : Required
        cbRole.setValidated(true);
        cbRole.getValidator().add(
                BindingUtils.toProperty(cbRole.getSelectionModel().selectedIndexProperty().isNotEqualTo(-1)),
                "A value must be selected"
        );
    }


    public String getName() {
        return tfName.getText();
    }

    public String getUsername() {
        return tfUsername.getText();
    }

    public String getPassword() {
        return pfPassword.getPassword();
    }

    public String getRole() {
        return cbRole.getSelectedValue();
    }

    public void clearFields(){
        tfName.clear();
        tfUsername.clear();
        pfPassword.passwordProperty().setValue("");
        pfConfirmPassword.passwordProperty().setValue("");
        cbRole.getSelectionModel().clearSelection();
        cbRole.setPromptText(null);
    }
}

