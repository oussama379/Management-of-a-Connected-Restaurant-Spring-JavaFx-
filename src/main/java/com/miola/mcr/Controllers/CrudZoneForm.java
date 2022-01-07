package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.Zone;
import com.miola.mcr.Services.RoleService;
import com.miola.mcr.Services.ZoneService;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.selection.ListCheckModel;
import io.github.palexdev.materialfx.utils.BindingUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
public class CrudZoneForm implements Initializable {

    private final FxWeaver fxWeaver;
    private final ZoneService zoneService;
    private final RoleService roleService;

    @FXML
    private MFXButton btnCancel;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXFlowlessCheckListView<String> listRole ;

    @FXML
    private MFXTextField tfName;

    private Boolean isAnEdit = false ; // to know difference in save function

    private Long idZone;


    @Autowired
    public CrudZoneForm(FxWeaver fxWeaver, ZoneService zoneService, RoleService roleService) {
        this.fxWeaver = fxWeaver;
        this.zoneService = zoneService;
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
        boolean fieldsValidation = tfName.isValid();
        if (fieldsValidation){
            Set<Role> selectedRoles = new HashSet<>();
            for (int i = 0 ; i < listRole.getSelectionModel().getCheckedItems().size() ; i++ ){
                selectedRoles.add(roleService.findRoleByTitle(listRole.getSelectionModel().getCheckedItems().get(i)));
            }
            if (isAnEdit){
                Zone zoneToEdit = new Zone(this.idZone, getName());
                zoneToEdit.setRoles(selectedRoles);
                if (zoneService.editZone(zoneToEdit)){
                    fxWeaver.getBean(CrudZone.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudZone.class).showAlter(0);
                }
            }else{
                Zone zoneToAdd = new Zone();
                zoneToAdd.setTitle(getName());
                zoneToAdd.setRoles(selectedRoles);
                if (zoneService.saveZone(zoneToAdd)){
                    fxWeaver.getBean(CrudZone.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudZone.class).showAlter(0);
                }
            }
        }else{
            fxWeaver.getBean(CrudZone.class).showAlter(4);
        }
    }

    public void fillData(String title, Long idZone, Set<Role> roles){
        this.idZone = idZone;
        tfName.setText(title);
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

        // Role : Required
//        BooleanProperty checkboxValidation = BindingUtils.toProperty(
//                Bindings.createBooleanBinding(
//                        () -> !listRole.getSelectionModel().getCheckedItems().isEmpty(),
//                        listRole.getSelectionModel().checkedItemsProperty()
//                )
//        );
//        tfName.getValidator().add(checkboxValidation, "A value must be selected");

    }

    public void clearFields(){
        tfName.clear();
        listRole.setItems(FXCollections.observableArrayList(roleService.getAllRolesNames()));
    }


    public String getName() {
        return tfName.getText();
    }



}
