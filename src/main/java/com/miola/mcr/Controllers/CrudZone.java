package com.miola.mcr.Controllers;


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
import java.util.function.Function;

@Component
@FxmlView
public class CrudZone implements Initializable {


    private final ConfigurableApplicationContext applicationContext;
    private final ZoneService zoneService;
    //to be deleted
    private final RoleService roleService;

    @FXML
    private MFXTableView<Zone> tableView;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXComboBox<String> cbRole ;

    @FXML
    private MFXComboBox<String> cbDevice;

    @FXML
    private MFXComboBox<String> cbSensor ;

    @FXML
    private MFXButton btnEdit;

    private Stage formWindow;

    @Autowired
    public CrudZone(ConfigurableApplicationContext applicationContext, ZoneService zoneService, UserService userService, RoleService roleService) {
        this.applicationContext = applicationContext;
        this.zoneService = zoneService;
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
        ObservableList<Zone> zones = FXCollections.observableArrayList(zoneService.getAllZones());

        /* create columns */
        MFXTableColumn<Zone> idColumn = new MFXTableColumn<Zone>("Id", Comparator.comparing(Zone::getId));
        MFXTableColumn<Zone> titleColumn = new MFXTableColumn<Zone>("Title", Comparator.comparing(Zone::getTitle));
        MFXTableColumn<Zone> devicesColumn = new MFXTableColumn<Zone>("Devices");
        MFXTableColumn<Zone> sensorsColumn = new MFXTableColumn<Zone>("Sensors");
        MFXTableColumn<Zone> rolesColumn = new MFXTableColumn<Zone>("Roles");


        /* link columns with proprieties */
        //  TODO WTF IS zone
        idColumn.setRowCellFunction(zone -> new MFXTableRowCell(String.valueOf(zone.getId())));
        titleColumn.setRowCellFunction(zone -> new MFXTableRowCell(zone.getTitle()));
        devicesColumn.setRowCellFunction(zone -> {
            MFXTableRowCell cell = new MFXTableRowCell("");
            cbDevice = new MFXComboBox<>();
            cbDevice.getItems().addAll(zoneService.getZoneDevicesNames(zone));
            cbDevice.setPromptText("See Devices");
            cell.setLeadingGraphic(cbDevice);
            return cell;
        });
        sensorsColumn.setRowCellFunction(zone -> {
            MFXTableRowCell cell = new MFXTableRowCell("");
            cbSensor = new MFXComboBox<>();
            cbSensor.getItems().addAll(zoneService.getZoneSensorsNames(zone));
            cbSensor.setPromptText("See Sensors");
            cell.setLeadingGraphic(cbSensor);
            return cell;
        });
        rolesColumn.setRowCellFunction(zone -> {
            MFXTableRowCell cell = new MFXTableRowCell("");
            cbRole = new MFXComboBox<>();
            cbRole.getItems().addAll(zoneService.getZoneRolesNames(zone));
            cbRole.setPromptText("See Roles");
            cell.setLeadingGraphic(cbRole);
            return cell;
        });


        /* fill table */
        tableView.setItems(zones);
        tableView.getTableColumns().addAll(idColumn);
        tableView.getTableColumns().addAll(titleColumn);
        tableView.getTableColumns().addAll(devicesColumn);
        tableView.getTableColumns().addAll(sensorsColumn);
        tableView.getTableColumns().addAll(rolesColumn);
    }

    @FXML
    public void delete(ActionEvent event) {
        // TODO after deleting a user the view need to be reloaded
        List<Zone> selectedZones = tableView.getSelectionModel().getSelectedItems(); // gives a List<Zone> of selected zones from tableView
        if (!selectedZones.isEmpty()) {
            for (Zone zone : selectedZones){
                zoneService.deleteZoneById(zone.getId());
            }
        }
    }

    @FXML
    public void edit(ActionEvent event) {
        List<Zone> selectedZones = tableView.getSelectionModel().getSelectedItems();
        if (!selectedZones.isEmpty()) {
            /* take first user in selected users */
            Zone zoneToBeEdit = tableView.getSelectionModel().getSelectedItems().get(0);

            /* show form */
            FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(CrudZoneForm.class);
            Scene scene = new Scene(root);
            fxWeaver.getBean(CrudZoneForm.class).fillData(zoneToBeEdit.getTitle(), zoneToBeEdit.getId(), zoneToBeEdit.getRoles()); // send data to the form scene
            formWindow.close();
            formWindow.setScene(scene);
            formWindow.setTitle("Edit Zone");
            formWindow.show();
        }
    }

    @FXML
    public void add(ActionEvent event) {
        /* show form */
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(CrudZoneForm.class);
        Scene scene = new Scene(root);
        formWindow.close();
        formWindow.setScene(scene);
        formWindow.setTitle("Add new Zone");
        formWindow.show();
    }

}
