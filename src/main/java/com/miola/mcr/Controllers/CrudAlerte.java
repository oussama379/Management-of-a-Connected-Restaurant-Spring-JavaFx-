package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.Alerte;
import com.miola.mcr.Entities.User;
import com.miola.mcr.Services.AlerteService;
import com.miola.mcr.Services.UserService;
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
public class CrudAlerte implements Initializable {

    private final ConfigurableApplicationContext applicationContext;
    private final AlerteService alerteService;

    @FXML
    private MFXTableView<Alerte> tableView;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnEdit;

    private Stage formWindow;

    @Autowired
    public CrudAlerte(ConfigurableApplicationContext applicationContext, AlerteService alerteService) {
        this.applicationContext = applicationContext;
        this.alerteService = alerteService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formWindow = new Stage();
        populateTable();
    }

    private void populateTable() {
        ObservableList<Alerte> alertes = FXCollections.observableArrayList(alerteService.getAllAlertes());

        /* create columns */
        MFXTableColumn<Alerte> idColumn = new MFXTableColumn<>("Id", Comparator.comparing(Alerte::getId));
        MFXTableColumn<Alerte> typeColumn = new MFXTableColumn<>("Type", Comparator.comparing(Alerte::getType));
        MFXTableColumn<Alerte> severityColumn = new MFXTableColumn<>("Severity", Comparator.comparing(Alerte::getSeverity));
        MFXTableColumn<Alerte> valueColumn = new MFXTableColumn<>("Value", Comparator.comparing(Alerte::getValue));
        // TODO MUTIPLE VALUES
        MFXTableColumn<Alerte> operatorColumn = new MFXTableColumn<>("Operator", Comparator.comparing(Alerte::getOperator));
        //MFXTableColumn<Alerte> timeColumn = new MFXTableColumn<>("Time", Comparator.comparing(Alerte::getTime));
        MFXTableColumn<Alerte> categoryColumn = new MFXTableColumn<>("Category", Comparator.comparing(Alerte::getCategoryName));

        /* link columns with proprieties */
        idColumn.setRowCellFunction(alerte -> new MFXTableRowCell(String.valueOf(alerte.getId())));
        typeColumn.setRowCellFunction(alerte -> new MFXTableRowCell(alerte.getType()));
        severityColumn.setRowCellFunction(alerte -> new MFXTableRowCell(alerte.getSeverity()));
        valueColumn.setRowCellFunction(alerte -> new MFXTableRowCell(String.valueOf(alerte.getValue())));
        operatorColumn.setRowCellFunction(alerte -> new MFXTableRowCell(alerte.getOperator()));
        categoryColumn.setRowCellFunction(alerte -> new MFXTableRowCell(alerte.getCategoryName()));

        /* fill table */
        //tableView.setItems(data);
        tableView.setItems(alertes);
        tableView.getTableColumns().addAll(idColumn);
        tableView.getTableColumns().addAll(typeColumn);
        tableView.getTableColumns().addAll(severityColumn);
        tableView.getTableColumns().addAll(valueColumn);
        tableView.getTableColumns().addAll(operatorColumn);
        tableView.getTableColumns().addAll(categoryColumn);
    }

    @FXML
    public void delete(ActionEvent event) {
        // TODO after deleting a user the view need to be reloaded
        List<Alerte> selectedAlertes = tableView.getSelectionModel().getSelectedItems(); // gives a List<User> of selected users from tableView
        if (!selectedAlertes.isEmpty()) {
            for (Alerte alerte : selectedAlertes){
                    alerteService.deleteAlerteById(alerte.getId());
            }
        }
    }

    @FXML
    public void edit(ActionEvent event) {
        List<Alerte> selectedAlertes = tableView.getSelectionModel().getSelectedItems();
        if (!selectedAlertes.isEmpty()) {
            /* take first user in selected users */
            Alerte alerteToBeEdit = tableView.getSelectionModel().getSelectedItems().get(0);

            /* show form */
            FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(CrudAlerteForm.class);
            Scene scene = new Scene(root);
            //System.out.print(userToBeEdit.getRoleName());
            fxWeaver.getBean(CrudAlerteForm.class).fillData(alerteToBeEdit.getType(), alerteToBeEdit.getId(), alerteToBeEdit.getSeverity(),
                    String.valueOf(alerteToBeEdit.getValue()), alerteToBeEdit.getOperator(), alerteToBeEdit.getCategoryName()); // send data to the form scene
            formWindow.close();
            formWindow.setScene(scene);
            formWindow.setTitle("Edit Alert");
            formWindow.show();
        }
    }

    @FXML
    public void add(ActionEvent event) {
        /* show form */
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(CrudAlerteForm.class);
        Scene scene = new Scene(root);
        formWindow.close();
        formWindow.setScene(scene);
        formWindow.setTitle("Add new Alert");
        formWindow.show();
    }

}
