package com.miola.mcr.Controllers;


import com.miola.mcr.Entities.Alerte;
import com.miola.mcr.Entities.Category;
import com.miola.mcr.Entities.Zone;
import com.miola.mcr.Services.CategoryService;
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
public class CrudCategory extends Crud implements Initializable {


    private final FxWeaver fxWeaver;
    private final CategoryService categoryService;

    @FXML
    private MFXTableView<Category> tableView;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXComboBox<String> cbRole ;

    @FXML
    private MFXComboBox<String> cbAlert;

    @FXML
    private MFXComboBox<String> cbSensor ;

    @FXML
    private MFXButton btnEdit;

    private Stage formWindow;
    private Pane centerPane = null;
    private AbstractMFXDialog dialog;

    @Autowired
    public CrudCategory(FxWeaver fxWeaver , ZoneService zoneService, UserService userService, CategoryService categoryService, RoleService roleService) {
        this.fxWeaver = fxWeaver;
        this.categoryService = categoryService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable();

        formWindow = new Stage();
        formWindow.setScene(new Scene(fxWeaver.loadView(CrudCategoryForm.class)));
        dialog = MFXDialogFactory.buildDialog(DialogType.INFO, "MFXDialog - Generic Dialog", null);

        super.initialize(formWindow, dialog);
    }

    public void populateTable() {
        /* load data */
        //ObservableList<User> data = FXCollections.observableArrayList(dummyData);
        ObservableList<Category> categories = FXCollections.observableArrayList(categoryService.getAllCategories());

        /* create columns */
        MFXTableColumn<Category> idColumn = new MFXTableColumn<>("Id", Comparator.comparing(Category::getId));
        MFXTableColumn<Category> titleColumn = new MFXTableColumn<>("Title", Comparator.comparing(Category::getTitle));
        MFXTableColumn<Category> descriptionColumn = new MFXTableColumn<>("Description", Comparator.comparing(Category::getDescription));
        MFXTableColumn<Category> alertsColumn = new MFXTableColumn<>("Alerts");
        MFXTableColumn<Category> sensorsColumn = new MFXTableColumn<>("Sensors");

        idColumn.setRowCellFunction(category -> new MFXTableRowCell(String.valueOf(category.getId())));
        titleColumn.setRowCellFunction(category -> new MFXTableRowCell(category.getTitle()));
        descriptionColumn.setRowCellFunction(category -> new MFXTableRowCell(category.getDescription()));
        alertsColumn.setRowCellFunction(category -> {
            MFXTableRowCell cell = new MFXTableRowCell("");
            cbAlert = new MFXComboBox<>();
            cbAlert.getItems().addAll(categoryService.getCategoryAlertsNames(category));
            cbAlert.setPromptText("See Alerts");
            cell.setLeadingGraphic(cbAlert);
            return cell;
        });
        sensorsColumn.setRowCellFunction(category -> {
            MFXTableRowCell cell = new MFXTableRowCell("");
            cbSensor = new MFXComboBox<>();
            cbSensor.getItems().addAll(categoryService.getCategorySensorsNames(category));
            cbSensor.setPromptText("See Sensors");
            cell.setLeadingGraphic(cbSensor);
            return cell;
        });

        /* fill table */
        tableView.setItems(categories);
        tableView.getTableColumns().addAll(idColumn);
        tableView.getTableColumns().addAll(titleColumn);
        tableView.getTableColumns().addAll(descriptionColumn);
        tableView.getTableColumns().addAll(alertsColumn);
        tableView.getTableColumns().addAll(sensorsColumn);
    }

    @FXML
    public void delete(ActionEvent event) {
        List<Category> selectedCategories = tableView.getSelectionModel().getSelectedItems(); // gives a List<Zone> of selected zones from tableView
        if (!selectedCategories.isEmpty()) {
            for (Category category : selectedCategories){
                categoryService.deleteCategoryId(category.getId());
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
        ObservableList<Category> items = FXCollections.observableArrayList(categoryService.getAllCategories());
        tableView.setItems(items);
    }

    public void showAlter(int alertType){
        super.showAlter(alertType ,centerPane, tableView, dialog);
    }


}
