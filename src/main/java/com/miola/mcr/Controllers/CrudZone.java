package com.miola.mcr.Controllers;


import com.miola.mcr.Entities.User;
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
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
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
public class CrudZone extends Crud implements Initializable {

    private final FxWeaver fxWeaver;
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
    private Pane centerPane = null;
    private AbstractMFXDialog dialog;

    @Autowired
    public CrudZone(ZoneService zoneService, UserService userService, FxWeaver fxWeaver, RoleService roleService) {
        this.fxWeaver = fxWeaver;
        this.zoneService = zoneService;
        this.roleService = roleService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable();

        formWindow = new Stage();
        formWindow.setScene(new Scene(fxWeaver.loadView(CrudZoneForm.class)));
        dialog = MFXDialogFactory.buildDialog(DialogType.INFO, "MFXDialog - Generic Dialog", null);

        super.initialize(formWindow, dialog);
    }

    public void populateTable() {



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
        List<Zone> selectedZones = tableView.getSelectionModel().getSelectedItems(); // gives a List<Zone> of selected zones from tableView
        if (!selectedZones.isEmpty()) {
            for (Zone zone : selectedZones){
                zoneService.deleteZoneById(zone.getId());
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
        ObservableList<Zone> zones = FXCollections.observableArrayList(zoneService.getAllZones());
        tableView.setItems(zones);
    }

    public void showAlter(int alertType){
        super.showAlter(alertType ,centerPane, tableView, dialog);
    }

}
