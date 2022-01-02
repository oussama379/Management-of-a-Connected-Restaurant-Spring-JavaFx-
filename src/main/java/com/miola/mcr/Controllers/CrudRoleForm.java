package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.Category;
import com.miola.mcr.Entities.Role;
import com.miola.mcr.Services.CategoryService;
import com.miola.mcr.Services.RoleService;
import io.github.palexdev.materialfx.controls.MFXButton;
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
public class CrudRoleForm implements Initializable {

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
    public CrudRoleForm(RoleService roleService) {
        this.roleService = roleService;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {
        Role roleToEditOrAdd = new Role();
        roleToEditOrAdd.setTitle(getTitle());
        roleToEditOrAdd.setDescription(getDescription());
        if (isAnEdit){
            try {
                roleToEditOrAdd.setId(this.idRole);
                roleService.editRole(roleToEditOrAdd);
                // TODO show Confirmation Msg
            }catch (Exception e){
                // TODO show Error Msg
            }
        }else{
           try {
               roleService.saveRole(roleToEditOrAdd);
               // TODO show Confirmation Msg
           }catch (Exception e){
               // TODO show Error Msg
           }
        }

    }


    public void fillData(String title, Long id, String description){
        this.idRole = id;
        tfTitle.setText(title);
        tfDescription.setText(description);
        isAnEdit = true;
    }


    public String getTitle() {
        return tfTitle.getText();
    }
    public String getDescription() {
        return tfDescription.getText();
    }

}

