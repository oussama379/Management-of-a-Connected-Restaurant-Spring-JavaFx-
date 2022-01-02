package com.miola.mcr.Controllers;


import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.Zone;
import com.miola.mcr.Services.RoleService;
import com.miola.mcr.Services.UserService;
import com.miola.mcr.Services.ZoneService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

@Component
@FxmlView
public class CrudRole implements Initializable {


    private final ConfigurableApplicationContext applicationContext;
    private final RoleService roleService;


    @FXML
    private MFXTableView<Role> tableView;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXComboBox<String> cbUser ;

    @FXML
    private MFXComboBox<String> cbPermission;

    @FXML
    private MFXComboBox<String> cbZone ;

    @FXML
    private MFXButton btnEdit;

    private Stage formWindow;

    @Autowired
    public CrudRole(ConfigurableApplicationContext applicationContext, RoleService roleService) {
        this.applicationContext = applicationContext;
        this.roleService = roleService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formWindow = new Stage();
        populateTable();
    }

    private void populateTable() {



        /* load data */
        //ObservableList<User> data = FXCollections.observableArrayList(dummyData);
        ObservableList<Role> roles = FXCollections.observableArrayList(roleService.getAllRoles());

        /* create columns */
        MFXTableColumn<Role> idColumn = new MFXTableColumn<>("Id", Comparator.comparing(Role::getId));
        MFXTableColumn<Role> titleColumn = new MFXTableColumn<>("Title", Comparator.comparing(Role::getTitle));
        MFXTableColumn<Role> descriptionColumn = new MFXTableColumn<>("Description", Comparator.comparing(Role::getDescription));
        MFXTableColumn<Role> usersColumn = new MFXTableColumn<>("Users");
        MFXTableColumn<Role> permissionsColumn = new MFXTableColumn<>("Permissions");
        MFXTableColumn<Role> zonesColumn = new MFXTableColumn<>("Zones");


        /* link columns with proprieties */
        //  TODO WTF IS zone
        idColumn.setRowCellFunction(role -> new MFXTableRowCell(String.valueOf(role.getId())));
        titleColumn.setRowCellFunction(role -> new MFXTableRowCell(role.getTitle()));
        descriptionColumn.setRowCellFunction(role -> new MFXTableRowCell(role.getDescription()));
        usersColumn.setRowCellFunction(role -> {
            MFXTableRowCell cell = new MFXTableRowCell("");
            cbUser = new MFXComboBox<>();
            cbUser.getItems().addAll(roleService.getRoleUsersNames(role));
            cbUser.setPromptText("See Users");
            cell.setLeadingGraphic(cbUser);
            return cell;
        });
        permissionsColumn.setRowCellFunction(role -> {
            MFXTableRowCell cell = new MFXTableRowCell("");
            cbPermission = new MFXComboBox<>();
            cbPermission.getItems().addAll(roleService.getRolePermissionsNames(role));
            cbPermission.setPromptText("See Sensors");
            cell.setLeadingGraphic(cbPermission);
            return cell;
        });
        zonesColumn.setRowCellFunction(role -> {
            MFXTableRowCell cell = new MFXTableRowCell("");
            cbZone = new MFXComboBox<>();
            cbZone.getItems().addAll(roleService.getRoleZonesNames(role));
            cbZone.setPromptText("See Roles");
            cell.setLeadingGraphic(cbZone);
            return cell;
        });


        /* fill table */
        tableView.setItems(roles);
        tableView.getTableColumns().addAll(idColumn);
        tableView.getTableColumns().addAll(titleColumn);
        tableView.getTableColumns().addAll(descriptionColumn);
        tableView.getTableColumns().addAll(usersColumn);
        tableView.getTableColumns().addAll(permissionsColumn);
        tableView.getTableColumns().addAll(zonesColumn);
    }

    @FXML
    public void delete(ActionEvent event) {
        // TODO after deleting a user the view need to be reloaded
        List<Role> selectedRoles = tableView.getSelectionModel().getSelectedItems(); // gives a List<Zone> of selected zones from tableView
        if (!selectedRoles.isEmpty()) {
            for (Role role : selectedRoles){
                roleService.deleteRoleById(role.getId());
            }
        }
    }

    @FXML
    public void edit(ActionEvent event) {
        List<Role> selectedRoles = tableView.getSelectionModel().getSelectedItems();
        if (!selectedRoles.isEmpty()) {
            /* take first user in selected users */
            Role roleToBeEdit = tableView.getSelectionModel().getSelectedItems().get(0);

            /* show form */
            FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(CrudRoleForm.class);
            Scene scene = new Scene(root);
            fxWeaver.getBean(CrudRoleForm.class).fillData(roleToBeEdit.getTitle(), roleToBeEdit.getId(), roleToBeEdit.getDescription()); // send data to the form scene
            formWindow.close();
            formWindow.setScene(scene);
            formWindow.setTitle("Edit Role");
            formWindow.show();
        }
    }

    @FXML
    public void add(ActionEvent event) {
        /* show form */
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(CrudRoleForm.class);
        Scene scene = new Scene(root);
        formWindow.close();
        formWindow.setScene(scene);
        formWindow.setTitle("Add new Role");
        formWindow.show();
    }

}
