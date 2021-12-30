package com.miola.mcr.Controllers;


import com.miola.mcr.Entities.User;
import com.miola.mcr.Entities.Zone;
import com.miola.mcr.Services.ZoneService;
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
public class CrudZone implements Initializable {


    private final ConfigurableApplicationContext applicationContext;
    private final ZoneService zoneService;

    @FXML
    private MFXTableView<Zone> tableView;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnEdit;

    private Stage formWindow;

    @Autowired
    public CrudZone(ConfigurableApplicationContext applicationContext, ZoneService zoneService) {
        this.applicationContext = applicationContext;
        this.zoneService = zoneService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formWindow = new Stage();
        populateTable();
    }

    private void populateTable() {
        // dummy data to be deleted
/*        List<User> dummyData = List.of(
                new User(1L, "Monsieur1", "user0", "123"),
                new User(2L, "Monsieur2", "user0", "123"),
                new User(0L, "Monsieur0", "user0", "123"),
                new User(3L, "Monsieur3", "user0", "123"),
                new User(4L, "Monsieur4", "user0", "123"),
                new User(5L, "Monsieur5", "user0", "123"),
                new User(6L, "Monsieur6", "user0", "123"),
                new User(7L, "Monsieur7", "user0", "123"),
                new User(8L, "Monsieur8", "user0", "123"),
                new User(9L, "Monsieur9", "user0", "123"),
                new User(1L, "Monsieur1", "user0", "123"),
                new User(2L, "Monsieur2", "user0", "123"),
                new User(0L, "Monsieur0", "user0", "123"),
                new User(3L, "Monsieur3", "user0", "123"),
                new User(4L, "Monsieur4", "user0", "123"),
                new User(5L, "Monsieur5", "user0", "123"),
                new User(6L, "Monsieur6", "user0", "123"),
                new User(7L, "Monsieur7", "user0", "123"),
                new User(8L, "Monsieur8", "user0", "123"),
                new User(9L, "Monsieur9", "user0", "123"),
                new User(1L, "Monsieur1", "user0", "123"),
                new User(2L, "Monsieur2", "user0", "123")
                );*/

        /* load data */
        //ObservableList<User> data = FXCollections.observableArrayList(dummyData);
        ObservableList<Zone> zones = FXCollections.observableArrayList(zoneService.getAllZones());

        /* create columns */
        MFXTableColumn<Zone> idColumn = new MFXTableColumn<Zone>("Id", Comparator.comparing(Zone::getId));
        MFXTableColumn<Zone> titleColumn = new MFXTableColumn<Zone>("Title", Comparator.comparing(Zone::getTitle));
        //MFXTableColumn<Zone> devicesColumn = new MFXTableColumn<Zone>("Devices", Comparator.comparing(Zone::getSensors));
        //MFXTableColumn<Zone> sensorsColumn = new MFXTableColumn<Zone>("Sensors", Comparator.comparing(Zone::getSensors));
        //MFXTableColumn<Zone> rolesColumn = new MFXTableColumn<Zone>("Roles", Comparator.comparing(Zone::getRoles));


        /* link columns with proprieties */
        //  TODO WTF IS zone
        idColumn.setRowCellFunction(zone -> new MFXTableRowCell(String.valueOf(zone.getId())));
        titleColumn.setRowCellFunction(zone -> new MFXTableRowCell(zone.getTitle()));
        //devicesColumn.setRowCellFunction(zone -> new MFXTableRowCell(zone.getSensors()));
        //sensorsColumn.setRowCellFunction(zone -> new MFXTableRowCell(zone.getSensors()));
        //rolesColumn.setRowCellFunction(zone -> new MFXTableRowCell(zone.getRoles()));


        /* fill table */
        //tableView.setItems(data);
        tableView.setItems(zones);
        tableView.getTableColumns().addAll(idColumn);
        tableView.getTableColumns().addAll(titleColumn);
        //tableView.getTableColumns().addAll(devicesColumn);
        //tableView.getTableColumns().addAll(sensorsColumn);
        //tableView.getTableColumns().addAll(rolesColumn);
    }

    @FXML
    public void delete(ActionEvent event) {
        // TODO after deleting a user the view need to be reloaded
        List<Zone> selectedUsers = tableView.getSelectionModel().getSelectedItems(); // gives a List<Zone> of selected zones from tableView
        if (!selectedUsers.isEmpty()) {
            for (Zone zone : selectedUsers){
                zoneService.deleteZoneById(zone.getId());
            }
        }
    }

    @FXML
    public void edit(ActionEvent event) {
        List<Zone> selectedUsers = tableView.getSelectionModel().getSelectedItems();
        if (!selectedUsers.isEmpty()) {
            /* take first user in selected users */
            Zone zoneToBeEdit = tableView.getSelectionModel().getSelectedItems().get(0);

            /* show form */
            FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(CrudUserForm.class);
            Scene scene = new Scene(root);
            fxWeaver.getBean(CrudZoneForm.class).fillData(zoneToBeEdit.getTitle(), zoneToBeEdit.getId()); // send data to the form scene
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
        Parent root = fxWeaver.loadView(CrudUserForm.class);
        Scene scene = new Scene(root);
        formWindow.close();
        formWindow.setScene(scene);
        formWindow.setTitle("Add new user");
        formWindow.show();
    }

}
