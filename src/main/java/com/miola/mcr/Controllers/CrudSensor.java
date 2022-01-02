package com.miola.mcr.Controllers;


import com.miola.mcr.Entities.Sensor;
import com.miola.mcr.Services.SensorService;
import io.github.palexdev.materialfx.controls.MFXButton;
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
public class CrudSensor implements Initializable {


    private final ConfigurableApplicationContext applicationContext;
    private final SensorService sensorService;

    @FXML
    private MFXTableView<Sensor> tableView;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnEdit;

    private Stage formWindow;

    @Autowired
    public CrudSensor(ConfigurableApplicationContext applicationContext, SensorService sensorService) {
        this.applicationContext = applicationContext;
        this.sensorService = sensorService;

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formWindow = new Stage();
        populateTable();
    }

    private void populateTable() {

        /* load data */
        //ObservableList<User> data = FXCollections.observableArrayList(dummyData);
        ObservableList<Sensor> sensors = FXCollections.observableArrayList(sensorService.getAllSensors());

        /* create columns */
        MFXTableColumn<Sensor> idColumn = new MFXTableColumn<Sensor>("Id", Comparator.comparing(Sensor::getId));
        MFXTableColumn<Sensor> nameColumn = new MFXTableColumn<Sensor>("Name", Comparator.comparing(Sensor::getName));
        MFXTableColumn<Sensor> topicColumn = new MFXTableColumn<Sensor>("Topic", Comparator.comparing(Sensor::getTopic));
        MFXTableColumn<Sensor> categoryColumn = new MFXTableColumn<Sensor>("Category", Comparator.comparing(Sensor::getCategoryName));
        MFXTableColumn<Sensor> deviceColumn = new MFXTableColumn<Sensor>("Device", Comparator.comparing(Sensor::getDeviceName));
        MFXTableColumn<Sensor> dinningTableColumn = new MFXTableColumn<Sensor>("Dinning Table", Comparator.comparing(Sensor::getDiningTableName));
        MFXTableColumn<Sensor> zoneColumn = new MFXTableColumn<Sensor>("Zone", Comparator.comparing(Sensor::getZoneName));


        /* link columns with proprieties */

        idColumn.setRowCellFunction(sensor -> new MFXTableRowCell(String.valueOf(sensor.getId())));
        nameColumn.setRowCellFunction(sensor -> new MFXTableRowCell(String.valueOf(sensor.getName())));
        topicColumn.setRowCellFunction(sensor -> new MFXTableRowCell(String.valueOf(sensor.getTopic())));
        categoryColumn.setRowCellFunction(sensor -> new MFXTableRowCell(String.valueOf(sensor.getCategoryName())));
        deviceColumn.setRowCellFunction(sensor -> new MFXTableRowCell(String.valueOf(sensor.getDeviceName())));
        dinningTableColumn.setRowCellFunction(sensor -> new MFXTableRowCell(String.valueOf(sensor.getDiningTableName())));
        zoneColumn.setRowCellFunction(sensor -> new MFXTableRowCell(String.valueOf(sensor.getZoneName())));


        /* fill table */
        tableView.setItems(sensors);
        tableView.getTableColumns().addAll(idColumn);
        tableView.getTableColumns().addAll(nameColumn);
        tableView.getTableColumns().addAll(topicColumn);
        tableView.getTableColumns().addAll(categoryColumn);
        tableView.getTableColumns().addAll(deviceColumn);
        tableView.getTableColumns().addAll(dinningTableColumn);
        tableView.getTableColumns().addAll(zoneColumn);

    }

    @FXML
    public void delete(ActionEvent event) {
        List<Sensor> selectedZones = tableView.getSelectionModel().getSelectedItems(); // gives a List<Sensor> of selected sensors from tableView
        if (!selectedZones.isEmpty()) {
            for (Sensor sensor : selectedZones){
                sensorService.deleteSensorById(sensor.getId());
            }
        }
    }

    @FXML
    public void edit(ActionEvent event) {
        List<Sensor> selectedSensors = tableView.getSelectionModel().getSelectedItems();
        if (!selectedSensors.isEmpty()) {
            /* take first user in selected users */
            Sensor sensorToBeEdit = tableView.getSelectionModel().getSelectedItems().get(0);
            /* show form */
            // TODO OUSSAMA FxWeaver
            FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(CrudSensorForm.class);
            Scene scene = new Scene(root);
            fxWeaver.getBean(CrudSensorForm.class).fillData(sensorToBeEdit.getName(), sensorToBeEdit.getId(), sensorToBeEdit.getTopic(),
                    sensorToBeEdit.getCategoryName(), sensorToBeEdit.getDeviceName(), sensorToBeEdit.getDiningTableName()
                    , sensorToBeEdit.getZoneName()); // send data to the form scene
            formWindow.close();
            formWindow.setScene(scene);
            formWindow.setTitle("Edit Sensor");
            formWindow.show();
        }
    }

    @FXML
    public void add(ActionEvent event) {
        /* show form */
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(CrudSensorForm.class);
        Scene scene = new Scene(root);
        formWindow.close();
        formWindow.setScene(scene);
        formWindow.setTitle("Add new Sensor");
        formWindow.show();
    }

}
