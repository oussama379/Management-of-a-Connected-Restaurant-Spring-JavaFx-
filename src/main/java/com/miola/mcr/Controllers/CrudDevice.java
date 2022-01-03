package com.miola.mcr.Controllers;


import com.miola.mcr.Entities.*;
import com.miola.mcr.Services.DeviceService;
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
public class CrudDevice implements Initializable {


    private final ConfigurableApplicationContext applicationContext;
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

    @Autowired
    public CrudDevice(ConfigurableApplicationContext applicationContext, ZoneService zoneService, UserService userService, DeviceService deviceService, RoleService roleService) {
        this.applicationContext = applicationContext;
        this.deviceService = deviceService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formWindow = new Stage();
        populateTable();
    }

    private void populateTable() {



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


        /* link columns with proprieties */

        idColumn.setRowCellFunction(device -> new MFXTableRowCell(String.valueOf(device.getId())));
        nameColumn.setRowCellFunction(device -> new MFXTableRowCell(device.getName()));
        //powerColumn.setRowCellFunction(device -> new MFXTableRowCell(String.valueOf(device.getPower())));
        typeDeviceColumn.setRowCellFunction(device -> new MFXTableRowCell(device.getClass().getSimpleName()));
        //tempColumn.setRowCellFunction(airConditioner -> new MFXTableRowCell(String.valueOf(airConditioner.getTemperature())));
        //alarmeColumn.setRowCellFunction(airQuality -> new MFXTableRowCell(String.valueOf(airQuality.getAlarm())));
        //typeColumn.setRowCellFunction(airQuality -> new MFXTableRowCell(airQuality.getType()));
        //stateColumn.setRowCellFunction(energyMonitor -> new MFXTableRowCell(String.valueOf(energyMonitor.getState())));

        sensorsColumn.setRowCellFunction(device -> {
            MFXTableRowCell cell = new MFXTableRowCell("");
            cbSensor = new MFXComboBox<>();
            cbSensor.getItems().addAll(deviceService.getDeviceSensorsNames(device));
            cbSensor.setPromptText("See Sensors");
            cell.setLeadingGraphic(cbSensor);
            return cell;
        });
        /* fill table */
        tableView.setItems((ObservableList<Device>) devices);
        tableView.getTableColumns().addAll(idColumn);
        tableView.getTableColumns().addAll(typeDeviceColumn);
        tableView.getTableColumns().addAll(nameColumn);
        //tableView.getTableColumns().addAll(powerColumn);
        tableView.getTableColumns().addAll(sensorsColumn);

    }

    @FXML
    public void delete(ActionEvent event) {
        // TODO after deleting a user the view need to be reloaded
        List<Device> selectedDevices = tableView.getSelectionModel().getSelectedItems(); // gives a List<Zone> of selected zones from tableView
        if (!selectedDevices.isEmpty()) {
            for (Device device : selectedDevices){
                deviceService.deleteDeviceById(device.getId());
            }
        }
    }

    @FXML
    public void edit(ActionEvent event) {
        List<Device> selectedDevices = tableView.getSelectionModel().getSelectedItems();
        if (!selectedDevices.isEmpty()) {
             //take first user in selected users
            Device deviceToBeEdit = tableView.getSelectionModel().getSelectedItems().get(0);

             // show form
            FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(CrudDeviceForm.class);
            Scene scene = new Scene(root);
            fxWeaver.getBean(CrudDeviceForm.class).fillData(deviceToBeEdit.getName(), deviceToBeEdit.getId(), deviceToBeEdit.getClass().getSimpleName()); // send data to the form scene
            formWindow.close();
            formWindow.setScene(scene);
            formWindow.setTitle("Edit Device");
            formWindow.show();
        }
    }

    @FXML
    public void add(ActionEvent event) {
        /* show form */
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(CrudDeviceForm.class);
        Scene scene = new Scene(root);
        formWindow.close();
        formWindow.setScene(scene);
        formWindow.setTitle("Add new Device");
        formWindow.show();
    }

}
