package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.*;
import com.miola.mcr.Services.UserService;
import com.miola.mcr.Services.ZoneService;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.base.AbstractMFXDialog;
import io.github.palexdev.materialfx.controls.enums.DialogType;
import io.github.palexdev.materialfx.controls.factories.MFXDialogFactory;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Crud {

    @Autowired private FxWeaver fxWeaver;
    @Autowired private UserService userService;

    public abstract void populateTable();

    public abstract void updateTable();

    public abstract void delete(ActionEvent event);

    public void initialize(Stage formWindow, AbstractMFXDialog dialog){
//        Parent root = fxWeaver.loadView(CrudUserForm.class);
//        Scene scene = new Scene(root);
        formWindow.initModality(Modality.APPLICATION_MODAL); // makes stage act as a modal
        formWindow.setResizable(false); // prevents resize and removes minimize and maximize buttons
        formWindow.initStyle(StageStyle.UNDECORATED);
//        formWindow.setScene(scene);
        formWindow.setOnCloseRequest(ev -> {
            updateTable();
        });

        dialog.setIsDraggable(true);
        dialog.setPrefSize(200, 100);
        dialog.setVisible(false);
    }

    public void add(Stage formWindow) {
        /* show form */
        formWindow.close();
        formWindow.setTitle("Add new Menu Item");
        formWindow.showAndWait(); // blocks execution until the stage is closed
    }

    public void edit(Class crudClass,MFXTableView<?> tableView, Stage formWindow) {
        String title = "";
        List<?> selectedItems = tableView.getSelectionModel().getSelectedItems();
        if (!selectedItems.isEmpty()) {
            /* take first user in selected users */
            /* send data to the form scene */
            if (crudClass.equals(CrudUser.class)){
                User userToBeEdit = (User) tableView.getSelectionModel().getSelectedItems().get(0);
                fxWeaver.getBean(CrudUserForm.class).fillData(userToBeEdit.getName(), userToBeEdit.getUsername(), userToBeEdit.getId(), userToBeEdit.getRoleName());
            }
            else if (crudClass.equals(CrudZone.class)){
                Zone zoneToBeEdit = (Zone) tableView.getSelectionModel().getSelectedItems().get(0);
                fxWeaver.getBean(CrudZoneForm.class).fillData(zoneToBeEdit.getTitle(), zoneToBeEdit.getId(), zoneToBeEdit.getRoles());
            }
            else if (crudClass.equals(CrudSensor.class)){
                Sensor sensorToBeEdit = (Sensor) tableView.getSelectionModel().getSelectedItems().get(0);
                fxWeaver.getBean(CrudSensorForm.class).fillData(sensorToBeEdit.getName(), sensorToBeEdit.getId(), sensorToBeEdit.getTopic(),
                        sensorToBeEdit.getCategoryName(), sensorToBeEdit.getDeviceName(), sensorToBeEdit.getDiningTableName()
                        , sensorToBeEdit.getZoneName());
            }
            else if (crudClass.equals(CrudRole.class)){
                Role roleToBeEdit = (Role) tableView.getSelectionModel().getSelectedItems().get(0);
                fxWeaver.getBean(CrudRoleForm.class).fillData(roleToBeEdit.getTitle(), roleToBeEdit.getId(), roleToBeEdit.getDescription());
            }
            else if (crudClass.equals(CrudPermission.class)){
                Permission permissionToBeEdit = (Permission) tableView.getSelectionModel().getSelectedItems().get(0);
                fxWeaver.getBean(CrudPermissionForm.class).fillData(permissionToBeEdit.getTitle(),
                        permissionToBeEdit.getId(), permissionToBeEdit.getDescription(), permissionToBeEdit.getRoles());
            }
            else if (crudClass.equals(CrudDinningTable.class)){
                DiningTable diningTableToBeEdit = (DiningTable) tableView.getSelectionModel().getSelectedItems().get(0);
                fxWeaver.getBean(CrudDinningTableForm.class).fillData(diningTableToBeEdit.getNumber(), diningTableToBeEdit.getId(),
                        diningTableToBeEdit.getState());
            }
            else if (crudClass.equals(CrudDevice.class)){
                Device deviceToBeEdit = (Device) tableView.getSelectionModel().getSelectedItems().get(0);
                fxWeaver.getBean(CrudDeviceForm.class).fillData(deviceToBeEdit.getName(), deviceToBeEdit.getId(), deviceToBeEdit.getClass().getSimpleName());
            }
            else if (crudClass.equals(CrudCategory.class)){
                Category categoryToBeEdit = (Category) tableView.getSelectionModel().getSelectedItems().get(0);
                fxWeaver.getBean(CrudCategoryForm.class).fillData(categoryToBeEdit.getTitle(), categoryToBeEdit.getId(), categoryToBeEdit.getDescription());
            }
            else if (crudClass.equals(CrudAlerte.class)){
                Alerte alerteToBeEdit = (Alerte) tableView.getSelectionModel().getSelectedItems().get(0);
                fxWeaver.getBean(CrudAlerteForm.class).fillData(alerteToBeEdit.getType(), alerteToBeEdit.getId(), alerteToBeEdit.getSeverity(),
                        String.valueOf(alerteToBeEdit.getValue()), alerteToBeEdit.getOperator(), alerteToBeEdit.getCategoryName());

            }else if (crudClass.equals(CrudMenuItem.class)){
                MenuItem menuItemToBeEdit = (MenuItem) tableView.getSelectionModel().getSelectedItems().get(0);
                fxWeaver.getBean(CrudMenuItemForm.class).fillData(menuItemToBeEdit.getId(), menuItemToBeEdit.getTitle(),
                        menuItemToBeEdit.getPrice(), menuItemToBeEdit.getDescription());
            }

            /* show form */
            formWindow.close();
            formWindow.showAndWait(); // blocks execution until the stage is closed
        }
    }

    /*
     * int alertType :
     *       0 -> Save Failure
     *       1 -> Save Success
     *       2 -> Delete Confirmation TODO Delete Confirmation
     *       3 -> Delete Success
     *       4 -> Validation Warning
     * */
    public void showAlter(int alertType,
                          Pane centerPane,
                          MFXTableView<?> tableView,
                          AbstractMFXDialog dialog){
        centerPane = (AnchorPane) ((BorderPane) tableView.getScene().getRoot()).getCenter();
        if (!centerPane.getChildren().contains(dialog)) {
            AnchorPane.setRightAnchor(dialog, 20.0);
            AnchorPane.setBottomAnchor(dialog, 20.0);
            centerPane.getChildren().add(dialog);
            dialog.setOverlayClose(true);
        }
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished( event -> dialog.close() );

        switch (alertType){
            case 0:
                MFXDialogFactory.convertToSpecific(DialogType.ERROR, dialog);
                dialog.setTitle("Save failed");
                break;
            case 1:
                MFXDialogFactory.convertToSpecific(DialogType.INFO, dialog);
                dialog.setTitle("Saved successfully");
                break;
            case 2:
                break;
            case 3:
                MFXDialogFactory.convertToSpecific(DialogType.INFO, dialog);
                dialog.setTitle("Deleted successfully");
                break;
            case 4:
                MFXDialogFactory.convertToSpecific(DialogType.WARNING, dialog);
                dialog.setTitle("Fields validation failed");
                break;
            default:
        }
        dialog.show();
        delay.play();
    }

}
