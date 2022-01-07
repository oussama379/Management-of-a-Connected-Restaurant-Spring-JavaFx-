package com.miola.mcr.Controllers;


import com.miola.mcr.Entities.Permission;
import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.Zone;
import com.miola.mcr.Services.RoleService;
import com.miola.mcr.Services.UserService;
import com.miola.mcr.Services.ZoneService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.base.AbstractMFXDialog;
import io.github.palexdev.materialfx.controls.cell.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.controls.enums.DialogType;
import io.github.palexdev.materialfx.controls.factories.MFXDialogFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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
public class CrudRole extends Crud implements Initializable {


    private final FxWeaver fxWeaver;
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
    private Pane centerPane = null;
    private AbstractMFXDialog dialog;

    @Autowired
    public CrudRole(FxWeaver fxWeaver, RoleService roleService) {
        this.fxWeaver = fxWeaver;
        this.roleService = roleService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable();

        formWindow = new Stage();
        formWindow.setScene(new Scene(fxWeaver.loadView(CrudRoleForm.class)));
        dialog = MFXDialogFactory.buildDialog(DialogType.INFO, "MFXDialog - Generic Dialog", null);

        super.initialize(formWindow, dialog);
    }

    public void populateTable() {



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
        List<Role> selectedRoles = tableView.getSelectionModel().getSelectedItems(); // gives a List<Zone> of selected zones from tableView
        if (!selectedRoles.isEmpty()) {
            for (Role role : selectedRoles){
                roleService.deleteRoleById(role.getId());
            }
            this.showAlter(3);
            this.updateTable();
        }
    }

    @FXML
    public void edit(ActionEvent event) {
        edit(this.getClass() ,tableView, formWindow);
    }

    @FXML
    public void add(ActionEvent event) {
        super.add(formWindow);
    }

    public  void updateTable(){
        /* load data */
        ObservableList<Role> items = FXCollections.observableArrayList(roleService.getAllRoles());
        tableView.setItems(items);
    }

    public void showAlter(int alertType){
        super.showAlter(alertType ,centerPane, tableView, dialog);
    }

}
