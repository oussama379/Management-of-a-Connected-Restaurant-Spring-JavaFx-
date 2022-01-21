package com.miola.mcr.Controllers;


import com.miola.mcr.Entities.DiningTable;
import com.miola.mcr.Entities.MenuItem;
import com.miola.mcr.Services.*;
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
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

@Component
@FxmlView
public class CrudMenuItem extends Crud implements Initializable {

    private final FxWeaver fxWeaver;
    private final MenuItemService menuItemService;

    @FXML
    private MFXTableView<MenuItem> tableView;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnEdit;

    private Stage formWindow;
    private Pane centerPane = null;
    private AbstractMFXDialog dialog;

    @Autowired
    public CrudMenuItem(FxWeaver fxWeaver, ZoneService zoneService, UserService userService, RoleService roleService, MenuItemService menuItemService) {
        this.fxWeaver = fxWeaver;
        this.menuItemService = menuItemService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable();

        formWindow = new Stage();
        formWindow.setScene(new Scene(fxWeaver.loadView(CrudMenuItemForm.class)));
        dialog = MFXDialogFactory.buildDialog(DialogType.INFO, "MFXDialog - Generic Dialog", null);

        super.initialize(formWindow, dialog);
    }

    public void populateTable() {

        /* load data */
        ObservableList<MenuItem> menuItems = FXCollections.observableArrayList(menuItemService.getAllMenuItems());

        /* create columns */
        MFXTableColumn<MenuItem> idColumn = new MFXTableColumn<>("Id", Comparator.comparing(MenuItem::getId));
        MFXTableColumn<MenuItem> titleColumn = new MFXTableColumn<>("Title", Comparator.comparing(MenuItem::getTitle));
        MFXTableColumn<MenuItem> priceColumn = new MFXTableColumn<>("Price", Comparator.comparing(MenuItem::getPrice));
        MFXTableColumn<MenuItem> descriptionColumn = new MFXTableColumn<>("Description", Comparator.comparing(MenuItem::getDescription));



        /* link columns with proprieties */
        idColumn.setRowCellFunction(MenuItem -> new MFXTableRowCell(String.valueOf(MenuItem.getId())));
        titleColumn.setRowCellFunction(MenuItem -> new MFXTableRowCell(MenuItem.getTitle()));
        priceColumn.setRowCellFunction(MenuItem -> new MFXTableRowCell(String.valueOf(MenuItem.getPrice())));
        descriptionColumn.setRowCellFunction(MenuItem -> new MFXTableRowCell(MenuItem.getDescription()));



        /* fill table */
        tableView.setItems(menuItems);
        tableView.getTableColumns().addAll(idColumn);
        tableView.getTableColumns().addAll(titleColumn);
        tableView.getTableColumns().addAll(priceColumn);
        tableView.getTableColumns().addAll(descriptionColumn);
    }

    @FXML
    public void delete(ActionEvent event) {
        List<MenuItem> selectedMenuItems = tableView.getSelectionModel().getSelectedItems(); // gives a List<Zone> of selected zones from tableView
        if (!selectedMenuItems.isEmpty()) {
            for (MenuItem menuItem : selectedMenuItems){
                menuItemService.deleteMenuItemById(menuItem.getId());
            }
            this.showAlter(3);
            this.updateTable();
        }
    }

    // TODO Edit Not Working
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
        ObservableList<MenuItem> items = FXCollections.observableArrayList(menuItemService.getAllMenuItems());
        tableView.setItems(items);
    }

    public void showAlter(int alertType){
        super.showAlter(alertType ,centerPane, tableView, dialog);
    }

}
