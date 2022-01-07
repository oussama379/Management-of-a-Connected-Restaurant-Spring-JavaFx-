package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.Permission;
import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.Zone;
import com.miola.mcr.Services.PermissionService;
import com.miola.mcr.Services.RoleService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFlowlessCheckListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.BindingUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class CrudPermissionForm implements Initializable {

    private final FxWeaver fxWeaver;
    private final PermissionService permissionService;
    private final RoleService roleService;

    @FXML
    private MFXButton btnCancel;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXFlowlessCheckListView<String> listRole ;

    @FXML
    private MFXTextField tfTitle;

    @FXML
    private MFXTextField tfDescription;


    private Boolean isAnEdit = false ; // to know difference in save function

    private Long idPermission;


    @Autowired
    public CrudPermissionForm(FxWeaver fxWeaver, PermissionService permissionService, RoleService roleService) {
        this.fxWeaver = fxWeaver;
        this.permissionService = permissionService;
        this.roleService = roleService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> roles = FXCollections.observableArrayList(roleService.getAllRolesNames());
        listRole.setItems(roles);
        // Set Validation
        this.setFieldsValidators();
    }

    @FXML
    void cancel(ActionEvent event) {
        this.closeWindow();
    }

    @FXML
    void save(ActionEvent event) {
        Set<Role> selectedRoles = new HashSet<>();
        for (int i = 0 ; i < listRole.getSelectionModel().getCheckedItems().size() ; i++ ){
            System.out.println(listRole.getSelectionModel().getCheckedItems().get(i));
            selectedRoles.add(roleService.findRoleByTitle(listRole.getSelectionModel().getCheckedItems().get(i)));
        }
        Permission permissionToEditOrAdd = new Permission();
        permissionToEditOrAdd.setTitle(getTitle());
        permissionToEditOrAdd.setDescription(getDescription());
        permissionToEditOrAdd.setRoles(selectedRoles);

        boolean fieldsValidation = tfTitle.isValid();
        if (fieldsValidation){
            if (isAnEdit){
                permissionToEditOrAdd.setId(this.idPermission);
                if (permissionService.editPermission(permissionToEditOrAdd)){
                    fxWeaver.getBean(CrudPermission.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudPermission.class).showAlter(0);
                }
            }else{
                if (permissionService.savePermission(permissionToEditOrAdd)){
                    fxWeaver.getBean(CrudPermission.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudPermission.class).showAlter(0);
                }
            }
        }else{
            fxWeaver.getBean(CrudPermission.class).showAlter(4);
        }

    }

    // TODO Add Role to this function ILYAS
    public void fillData(String title, Long id, String description, Set<Role> roles){
        this.idPermission = id;
        tfTitle.setText(title);
        tfDescription.setText(description);
        isAnEdit = true;
        List<String> roleNames = new ArrayList<>();
        for (Role role : roles)
            roleNames.add(role.getTitle());
        for (int i = 0 ; i < listRole.getItems().size() ; i++ ){
            if (roleNames.contains(listRole.getItems().get(i))){
                listRole.getSelectionModel().check(i,listRole.getItems().get(i));
            }
        }
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
        listRole.setItems(FXCollections.observableArrayList(roleService.getAllRolesNames()));
    }

    public String getTitle() {
        return tfTitle.getText();
    }
    public String getDescription() {
        return tfDescription.getText();
    }

}
