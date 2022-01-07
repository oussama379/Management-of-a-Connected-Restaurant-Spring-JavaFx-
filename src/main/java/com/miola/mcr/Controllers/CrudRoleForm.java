package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.Role;
import com.miola.mcr.Services.RoleService;
import io.github.palexdev.materialfx.controls.MFXButton;
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
import java.util.ResourceBundle;

@Component
@FxmlView
public class CrudRoleForm implements Initializable {

    private final FxWeaver fxWeaver;
    private final RoleService roleService;

    @FXML
    private MFXButton btnCancel;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXTextField tfTitle;

    @FXML
    private MFXTextField tfDescription;

    private Boolean isAnEdit = false ; // to know difference in save function

    private Long idRole;

    @Autowired
    public CrudRoleForm(FxWeaver fxWeaver, RoleService roleService) {
        this.fxWeaver = fxWeaver;
        this.roleService = roleService;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set Validation
        this.setFieldsValidators();
    }

    @FXML
    void cancel(ActionEvent event) {
        this.closeWindow();
    }

    @FXML
    void save(ActionEvent event) {
        Role roleToEditOrAdd = new Role();
        roleToEditOrAdd.setTitle(getTitle());
        roleToEditOrAdd.setDescription(getDescription());

        boolean fieldsValidation = tfTitle.isValid();
        if (fieldsValidation){
            if (isAnEdit){
                roleToEditOrAdd.setId(this.idRole);
                if (roleService.editRole(roleToEditOrAdd)){
                    fxWeaver.getBean(CrudRole.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudRole.class).showAlter(0);
                }
            }else{
                if (roleService.saveRole(roleToEditOrAdd)){
                    fxWeaver.getBean(CrudRole.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudRole.class).showAlter(0);
                }
            }
        }else{
            fxWeaver.getBean(CrudRole.class).showAlter(4);
        }
    }


    public void fillData(String title, Long id, String description){
        this.idRole = id;
        tfTitle.setText(title);
        tfDescription.setText(description);
        isAnEdit = true;
    }

    public void closeWindow(){
        this.clearFields();
this.isAnEdit = false;
        Stage formWindow = (Stage) (tfTitle.getScene().getWindow());
        formWindow.fireEvent(new WindowEvent(formWindow, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void setFieldsValidators() {
        // Tile TextField : Required
        tfTitle.setValidated(true);
        tfTitle.getValidator().add(
                BindingUtils.toProperty(tfTitle.textProperty().isNotEmpty()),
                "Required"
        );
    }

    public void clearFields(){
        tfTitle.clear();
        tfDescription.clear();
    }


    public String getTitle() {
        return tfTitle.getText();
    }
    public String getDescription() {
        return tfDescription.getText();
    }

}

