package com.miola.mcr.Controllers;


import com.miola.mcr.Entities.DiningTable;
import com.miola.mcr.Entities.Zone;
import com.miola.mcr.Services.DiningTableService;
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
public class CrudDinningTable implements Initializable {


    private final ConfigurableApplicationContext applicationContext;
    private final DiningTableService diningTableService;

    @FXML
    private MFXTableView<DiningTable> tableView;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXComboBox<String> cbSensor ;


    @FXML
    private MFXButton btnEdit;

    private Stage formWindow;

    @Autowired
    public CrudDinningTable(ConfigurableApplicationContext applicationContext, ZoneService zoneService, UserService userService, RoleService roleService, DiningTableService diningTableService) {
        this.applicationContext = applicationContext;
        this.diningTableService = diningTableService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formWindow = new Stage();
        populateTable();
    }

    private void populateTable() {

        /* load data */
        //ObservableList<User> data = FXCollections.observableArrayList(dummyData);
        ObservableList<DiningTable> diningTables = FXCollections.observableArrayList(diningTableService.getAllDiningTables());

        /* create columns */
        MFXTableColumn<DiningTable> idColumn = new MFXTableColumn<>("Id", Comparator.comparing(DiningTable::getId));
        MFXTableColumn<DiningTable> numberColumn = new MFXTableColumn<>("Number", Comparator.comparing(DiningTable::getNumber));
        MFXTableColumn<DiningTable> stateColumn = new MFXTableColumn<>("State", Comparator.comparing(DiningTable::getState));
        MFXTableColumn<DiningTable> sensorsColumn = new MFXTableColumn<>("Sensors");



        /* link columns with proprieties */
        //  TODO WTF IS zone
        idColumn.setRowCellFunction(diningTable -> new MFXTableRowCell(String.valueOf(diningTable.getId())));
        numberColumn.setRowCellFunction(diningTable -> new MFXTableRowCell(String.valueOf(diningTable.getNumber())));
        stateColumn.setRowCellFunction(diningTable -> new MFXTableRowCell(String.valueOf(diningTable.getState())));
        sensorsColumn.setRowCellFunction(diningTable -> {
            MFXTableRowCell cell = new MFXTableRowCell("");
            cbSensor = new MFXComboBox<>();
            cbSensor.getItems().addAll(diningTableService.getDiningTableSensorsNames(diningTable));
            cbSensor.setPromptText("See Sensors");
            cell.setLeadingGraphic(cbSensor);
            return cell;
        });


        /* fill table */
        tableView.setItems(diningTables);
        tableView.getTableColumns().addAll(idColumn);
        tableView.getTableColumns().addAll(numberColumn);
        tableView.getTableColumns().addAll(stateColumn);
        tableView.getTableColumns().addAll(sensorsColumn);
    }

    @FXML
    public void delete(ActionEvent event) {
        // TODO after deleting a user the view need to be reloaded
        List<DiningTable> selectedDiningTables = tableView.getSelectionModel().getSelectedItems(); // gives a List<Zone> of selected zones from tableView
        if (!selectedDiningTables.isEmpty()) {
            for (DiningTable diningTable : selectedDiningTables){
                diningTableService.deleteDiningTableById(diningTable.getId());
            }
        }
    }

    @FXML
    public void edit(ActionEvent event) {
        List<DiningTable> selectedDiningTables = tableView.getSelectionModel().getSelectedItems();
        if (!selectedDiningTables.isEmpty()) {
            /* take first user in selected users */
            DiningTable diningTableToBeEdit = tableView.getSelectionModel().getSelectedItems().get(0);

            /* show form */
            FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(CrudDinningTableForm.class);
            Scene scene = new Scene(root);
            fxWeaver.getBean(CrudDinningTableForm.class).fillData(diningTableToBeEdit.getNumber(), diningTableToBeEdit.getId(),
                    diningTableToBeEdit.getState()); // send data to the form scene
            formWindow.close();
            formWindow.setScene(scene);
            formWindow.setTitle("Edit Dinning Table");
            formWindow.show();
        }
    }

    @FXML
    public void add(ActionEvent event) {
        /* show form */
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(CrudDinningTableForm.class);
        Scene scene = new Scene(root);
        formWindow.close();
        formWindow.setScene(scene);
        formWindow.setTitle("Add new Dinning Table");
        formWindow.show();
    }

}
