package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.Alerte;
import com.miola.mcr.Entities.User;
import com.miola.mcr.Entities.Zone;
import com.miola.mcr.Services.AlerteService;
import com.miola.mcr.Services.UserService;
import io.github.palexdev.materialfx.controls.MFXButton;
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
public class CrudAlerte extends Crud implements Initializable {

    private final FxWeaver fxWeaver;
    private final AlerteService alerteService;

    @FXML
    private MFXTableView<Alerte> tableView;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnEdit;

    private Stage formWindow;
    private Pane centerPane = null;
    private AbstractMFXDialog dialog;

    @Autowired
    public CrudAlerte(FxWeaver fxWeaver, AlerteService alerteService) {
        this.fxWeaver = fxWeaver;
        this.alerteService = alerteService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable();

        formWindow = new Stage();
        formWindow.setScene(new Scene(fxWeaver.loadView(CrudAlerteForm.class)));
        dialog = MFXDialogFactory.buildDialog(DialogType.INFO, "MFXDialog - Generic Dialog", null);

        super.initialize(formWindow, dialog);
    }

    public void populateTable() {
        ObservableList<Alerte> alertes = FXCollections.observableArrayList(alerteService.getAllAlertes());

        /* create columns */
        MFXTableColumn<Alerte> idColumn = new MFXTableColumn<>("Id", Comparator.comparing(Alerte::getId));
        MFXTableColumn<Alerte> typeColumn = new MFXTableColumn<>("Type", Comparator.comparing(Alerte::getType));
        MFXTableColumn<Alerte> severityColumn = new MFXTableColumn<>("Severity", Comparator.comparing(Alerte::getSeverity));
        MFXTableColumn<Alerte> valueColumn = new MFXTableColumn<>("Value", Comparator.comparing(Alerte::getValue));
        // TODO MUTIPLE VALUES
        MFXTableColumn<Alerte> operatorColumn = new MFXTableColumn<>("Operator", Comparator.comparing(Alerte::getOperator));
        MFXTableColumn<Alerte> fromTimeColumn = new MFXTableColumn<>("From Time", Comparator.comparing(Alerte::getFromTime));
        MFXTableColumn<Alerte> toTimeColumn = new MFXTableColumn<>("To Time", Comparator.comparing(Alerte::getToTime));
        MFXTableColumn<Alerte> categoryColumn = new MFXTableColumn<>("Category", Comparator.comparing(Alerte::getCategoryName));

        /* link columns with proprieties */
        idColumn.setRowCellFunction(alerte -> new MFXTableRowCell(String.valueOf(alerte.getId())));
        typeColumn.setRowCellFunction(alerte -> new MFXTableRowCell(alerte.getType()));
        severityColumn.setRowCellFunction(alerte -> new MFXTableRowCell(alerte.getSeverity()));
        valueColumn.setRowCellFunction(alerte -> new MFXTableRowCell(String.valueOf(alerte.getValue())));
        fromTimeColumn.setRowCellFunction(alerte -> new MFXTableRowCell(alerte.getFromTime()));
        toTimeColumn.setRowCellFunction(alerte -> new MFXTableRowCell(alerte.getToTime()));
        operatorColumn.setRowCellFunction(alerte -> new MFXTableRowCell(alerte.getOperator()));
        categoryColumn.setRowCellFunction(alerte -> new MFXTableRowCell(alerte.getCategoryName()));

        /* fill table */
        //tableView.setItems(data);
        tableView.setItems(alertes);
        tableView.getTableColumns().addAll(idColumn);
        tableView.getTableColumns().addAll(typeColumn);
        tableView.getTableColumns().addAll(severityColumn);
        tableView.getTableColumns().addAll(valueColumn);
        tableView.getTableColumns().addAll(fromTimeColumn);
        tableView.getTableColumns().addAll(toTimeColumn);
        tableView.getTableColumns().addAll(operatorColumn);
        tableView.getTableColumns().addAll(categoryColumn);
    }

    @FXML
    public void delete(ActionEvent event) {
        List<Alerte> selectedAlertes = tableView.getSelectionModel().getSelectedItems(); // gives a List<User> of selected users from tableView
        if (!selectedAlertes.isEmpty()) {
            for (Alerte alerte : selectedAlertes){
                    alerteService.deleteAlerteById(alerte.getId());
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
        ObservableList<Alerte> items = FXCollections.observableArrayList(alerteService.getAllAlertes());
        tableView.setItems(items);
    }

    public void showAlter(int alertType){
        super.showAlter(alertType ,centerPane, tableView, dialog);
    }


}
