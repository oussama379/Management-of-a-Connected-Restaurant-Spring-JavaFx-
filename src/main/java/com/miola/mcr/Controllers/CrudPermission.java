package com.miola.mcr.Controllers;


import com.miola.mcr.Entities.DiningTable;
import com.miola.mcr.Entities.Permission;
import com.miola.mcr.Entities.Zone;
import com.miola.mcr.Services.PermissionService;
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
public class CrudPermission extends Crud implements Initializable {


    private final FxWeaver fxWeaver;
    private final PermissionService permissionService;

    @FXML
    private MFXTableView<Permission> tableView;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXComboBox<String> cbRole ;

    @FXML
    private MFXButton btnEdit;

    private Stage formWindow;
    private Pane centerPane = null;
    private AbstractMFXDialog dialog;

    @Autowired
    public CrudPermission(FxWeaver fxWeaver , PermissionService permissionService) {
        this.fxWeaver = fxWeaver;
        this.permissionService = permissionService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable();

        formWindow = new Stage();
        formWindow.setScene(new Scene(fxWeaver.loadView(CrudPermissionForm.class)));
        dialog = MFXDialogFactory.buildDialog(DialogType.INFO, "MFXDialog - Generic Dialog", null);

        super.initialize(formWindow, dialog);
    }

    public void populateTable() {



        /* load data */
        //ObservableList<User> data = FXCollections.observableArrayList(dummyData);
        ObservableList<Permission> permissions = FXCollections.observableArrayList(permissionService.getAllPermissions());

        /* create columns */
        MFXTableColumn<Permission> idColumn = new MFXTableColumn<>("Id", Comparator.comparing(Permission::getId));
        MFXTableColumn<Permission> titleColumn = new MFXTableColumn<>("Title", Comparator.comparing(Permission::getTitle));
        MFXTableColumn<Permission> descriptionColumn = new MFXTableColumn<>("Description", Comparator.comparing(Permission::getDescription));
        MFXTableColumn<Permission> rolesColumn = new MFXTableColumn<>("Roles");


        /* link columns with proprieties */
        //  TODO WTF IS zone
        idColumn.setRowCellFunction(permission -> new MFXTableRowCell(String.valueOf(permission.getId())));
        titleColumn.setRowCellFunction(permission -> new MFXTableRowCell(permission.getTitle()));
        descriptionColumn.setRowCellFunction(permission -> new MFXTableRowCell(permission.getDescription()));
        rolesColumn.setRowCellFunction(permission -> {
            MFXTableRowCell cell = new MFXTableRowCell("");
            cbRole = new MFXComboBox<>();
            cbRole.getItems().addAll(permissionService.getPermissionRolesNames(permission));
            cbRole.setPromptText("See Roles");
            cell.setLeadingGraphic(cbRole);
            return cell;
        });


        /* fill table */
        tableView.setItems(permissions);
        tableView.getTableColumns().addAll(idColumn);
        tableView.getTableColumns().addAll(titleColumn);
        tableView.getTableColumns().addAll(descriptionColumn);
        tableView.getTableColumns().addAll(rolesColumn);
    }

    @FXML
    public void delete(ActionEvent event) {
        List<Permission> selectedPermissions = tableView.getSelectionModel().getSelectedItems(); // gives a List<Zone> of selected zones from tableView
        if (!selectedPermissions.isEmpty()) {
            for (Permission permission : selectedPermissions){
                permissionService.deletePermissionById(permission.getId());
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
        ObservableList<Permission> items = FXCollections.observableArrayList(permissionService.getAllPermissions());
        tableView.setItems(items);
    }

    public void showAlter(int alertType){
        super.showAlter(alertType ,centerPane, tableView, dialog);
    }

}
