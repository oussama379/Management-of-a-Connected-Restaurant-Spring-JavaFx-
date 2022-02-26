package com.miola.mcr.Controllers;


import com.miola.mcr.Entities.*;
import com.miola.mcr.Services.DeviceService;
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
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

@Component
@FxmlView
public class CrudDevice extends Crud implements Initializable {


    private final FxWeaver fxWeaver;
    private final DeviceService deviceService;

    @FXML
    private MFXTableView<Device> tableView;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXComboBox<String> cbSensor ;

    @FXML
    private MFXButton btnEdit;

    private Stage formWindow;
    private Pane centerPane = null;
    private AbstractMFXDialog dialog;

    @Autowired
    public CrudDevice(FxWeaver fxWeaver, ZoneService zoneService, UserService userService, DeviceService deviceService, RoleService roleService) {
        this.fxWeaver = fxWeaver;
        this.deviceService = deviceService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable();

        formWindow = new Stage();
        formWindow.setScene(new Scene(fxWeaver.loadView(CrudDeviceForm.class)));
        dialog = MFXDialogFactory.buildDialog(DialogType.INFO, "MFXDialog - Generic Dialog", null);

        super.initialize(formWindow, dialog);
    }

    public void populateTable() {



        /* load data */
        //ObservableList<User> data = FXCollections.observableArrayList(dummyData);
        ObservableList<? extends Device> devices = FXCollections.observableArrayList(deviceService.getAllDevices());

        /* create columns */
        MFXTableColumn<Device> idColumn = new MFXTableColumn<>("Id", Comparator.comparing(Device::getId));
        MFXTableColumn<Device> typeDeviceColumn = new MFXTableColumn<Device>("Device Type");
        MFXTableColumn<Device> nameColumn = new MFXTableColumn<>("Name", Comparator.comparing(Device::getName));
        MFXTableColumn<Device> powerColumn = new MFXTableColumn<>("Power", Comparator.comparing(Device::getPower));
        //MFXTableColumn<AirConditioner> tempColumn = new MFXTableColumn<>("Temperature", Comparator.comparing(AirConditioner::getTemperature));
        //MFXTableColumn<AirQuality> alarmeColumn = new MFXTableColumn<>("Alarme", Comparator.comparing(AirQuality::getAlarm));
        //MFXTableColumn<AirQuality> typeColumn = new MFXTableColumn<>("Alarme Type", Comparator.comparing(AirQuality::getType));
        //MFXTableColumn<EnergyMonitor stateColumn = new MFXTableColumn<>("State", Comparator.comparing(EnergyMonitor::getState));
        MFXTableColumn<Device> sensorsColumn = new MFXTableColumn<>("Sensors");
        MFXTableColumn<Device> zoneColumn = new MFXTableColumn<>("Zone", Comparator.comparing(Device::getZone));


        /* link columns with proprieties */

        idColumn.setRowCellFunction(device -> new MFXTableRowCell(String.valueOf(device.getId())));
        nameColumn.setRowCellFunction(device -> new MFXTableRowCell(device.getName()));
//        powerColumn.setRowCellFunction(device -> new MFXTableRowCell(String.valueOf(device.getPower())));
        typeDeviceColumn.setRowCellFunction(device -> new MFXTableRowCell(device.getClass().getSimpleName()));
        //tempColumn.setRowCellFunction(airConditioner -> new MFXTableRowCell(String.valueOf(airConditioner.getTemperature())));
        //alarmeColumn.setRowCellFunction(airQuality -> new MFXTableRowCell(String.valueOf(airQuality.getAlarm())));
        //typeColumn.setRowCellFunction(airQuality -> new MFXTableRowCell(airQuality.getType()));
        //stateColumn.setRowCellFunction(energyMonitor -> new MFXTableRowCell(String.valueOf(energyMonitor.getState())));
        zoneColumn.setRowCellFunction(device -> new MFXTableRowCell(String.valueOf(device.getZone())));

        sensorsColumn.setRowCellFunction(device -> {
            MFXTableRowCell cell = new MFXTableRowCell("");
            cbSensor = new MFXComboBox<>();
            cbSensor.getItems().addAll(deviceService.getDeviceSensorsNames(device));
            cbSensor.setPromptText("See Sensors");
            cell.setLeadingGraphic(cbSensor);
            return cell;
        });
        powerColumn.setRowCellFunction(device -> {
            MFXTableRowCell cell = new MFXTableRowCell("");
            MFXButton b = new MFXButton("    ");
            b.setStyle( (device.getPower().equals(DevicePower.OFF)) ? "-fx-background-color: RED;" : "-fx-background-color: GREEN;");
            b.setDisable(true);
//            b.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            cell.setLeadingGraphic(b);
            return cell;
        });
        /* fill table */
        tableView.setItems((ObservableList<Device>) devices);
        tableView.getTableColumns().addAll(idColumn);
//        tableView.getTableColumns().addAll(typeDeviceColumn);
        tableView.getTableColumns().addAll(nameColumn);
//        tableView.getTableColumns().addAll(sensorsColumn);
        tableView.getTableColumns().addAll(zoneColumn);
        tableView.getTableColumns().addAll(powerColumn);

    }

    @FXML
    public void delete(ActionEvent event) {
        List<Device> selectedDevices = tableView.getSelectionModel().getSelectedItems(); // gives a List<Zone> of selected zones from tableView
        if (!selectedDevices.isEmpty()) {
            for (Device device : selectedDevices){
                deviceService.deleteDeviceById(device.getId());
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
        ObservableList<Device> items = FXCollections.observableArrayList(deviceService.getAllDevices());
        tableView.setItems(items);
    }

    public void showAlter(int alertType){
        super.showAlter(alertType ,centerPane, tableView, dialog);
    }

}
