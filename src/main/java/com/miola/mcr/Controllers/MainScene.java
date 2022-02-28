package com.miola.mcr.Controllers;


import com.miola.mcr.JavaFxApplication;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXNotification;
import io.github.palexdev.materialfx.controls.SimpleMFXNotificationPane;
import io.github.palexdev.materialfx.notifications.NotificationPos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;

@Component
@FxmlView
public class MainScene implements Initializable {

    private final ConfigurableApplicationContext applicationContext;
    private final Map<MFXButton, Class> mapButtonClass=new HashMap<>();

    @FXML private MFXButton btUsersCrud;
    @FXML private MFXButton btZonesCrud;
    @FXML private MFXButton btSensorsCrud;
    @FXML private MFXButton btDinningTablesCrud;
//    @FXML private MFXButton btCategoriesCrud;
//    @FXML private MFXButton btRolesCrud;
    @FXML private MFXButton btAlertesCrud;
//    @FXML private MFXButton btPermissionsCrud;
    @FXML private MFXButton btDevicesCrud;
    @FXML private MFXButton btMenuItemCrud;

    @FXML private MFXButton btEnergyDB;
    @FXML private MFXButton btnAirDB;

    @FXML private MFXButton btHome;
    @FXML private MFXButton btManagement;
    @FXML private MFXButton btnCustomerArea;
    @FXML private MFXButton btnStatistics;

    @FXML private MFXButton btSignOut;

    @FXML private BorderPane rootPane;
    @FXML private VBox vbCruds;

    @FXML private Label lblHeader;

    private MFXButton selected;


    @Autowired
    public MainScene(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /* Rearrange */
        btUsersCrud.managedProperty().bind(btUsersCrud.visibleProperty());
        btZonesCrud.managedProperty().bind(btZonesCrud.visibleProperty());
        btSensorsCrud.managedProperty().bind(btSensorsCrud.visibleProperty());
        btDinningTablesCrud.managedProperty().bind(btDinningTablesCrud.visibleProperty());
        btAlertesCrud.managedProperty().bind(btAlertesCrud.visibleProperty());
        btDevicesCrud.managedProperty().bind(btDevicesCrud.visibleProperty());
        btMenuItemCrud.managedProperty().bind(btMenuItemCrud.visibleProperty());
        btEnergyDB.managedProperty().bind(btEnergyDB.visibleProperty());
        btnAirDB.managedProperty().bind(btnAirDB.visibleProperty());
        btHome.managedProperty().bind(btHome.visibleProperty());
        btManagement.managedProperty().bind(btManagement.visibleProperty());
        btnCustomerArea.managedProperty().bind(btnCustomerArea.visibleProperty());
        btnStatistics.managedProperty().bind(btnStatistics.visibleProperty());
        btSignOut.managedProperty().bind(btSignOut.visibleProperty());

        /* CRUDs */
        mapButtonClass.put(btUsersCrud, CrudUser.class);
        mapButtonClass.put(btZonesCrud, CrudZone.class);
        mapButtonClass.put(btSensorsCrud, CrudSensor.class);
        mapButtonClass.put(btDinningTablesCrud, CrudDinningTable.class);
        mapButtonClass.put(btAlertesCrud, CrudAlerte.class);
        mapButtonClass.put(btDevicesCrud, CrudDevice.class);
        mapButtonClass.put(btMenuItemCrud, CrudMenuItem.class);

        /* DashBoards */
        mapButtonClass.put(btEnergyDB, DBEnergy.class);
        mapButtonClass.put(btnAirDB, DBAir.class);
        mapButtonClass.put(btnCustomerArea, Orders.class);
        mapButtonClass.put(btnStatistics, Statistics.class);

        /* Selected */
        btHome.setVisible(false);
        btnCustomerArea.getStyleClass().remove("menu-button");
        btnCustomerArea.getStyleClass().add("menu-button-selected");
        selected = btnCustomerArea;

        /* Restriction */
            // Technician | id = 2
        if(Login.roleCurrentUser.getId() == 2){
            for (Map.Entry<MFXButton, Class> e: mapButtonClass.entrySet()) {
                e.getKey().setVisible(false);
            }
            btManagement.setVisible(true);
            btDevicesCrud.setVisible(true);
            btSensorsCrud.setVisible(true);
            btZonesCrud.setVisible(true);
            btManagement.getStyleClass().remove("menu-button");
            btManagement.getStyleClass().add("menu-button-selected");
            selected = btManagement;

        }
            // Waiter | id = 3
        if(Login.roleCurrentUser.getId() == 3){
            for (Map.Entry<MFXButton, Class> e: mapButtonClass.entrySet()) {
                e.getKey().setVisible(false);
            }
            btManagement.setVisible(false);
            btnCustomerArea.setVisible(true);
            btEnergyDB.setVisible(true);
            btnAirDB.setVisible(true);
        }
        initScene();
    }

    @FXML
    void loadScene(ActionEvent event) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        rootPane.setCenter(fxWeaver.loadView(mapButtonClass.get(event.getSource())));
        rootPane.getCenter().getStyleClass().add("bc-darkblue-alt");

        MFXButton b = (MFXButton)event.getSource();

        selected.getStyleClass().remove("menu-button-selected");
        selected.getStyleClass().add("menu-button");

        if (b.toString().contains("Crud")) {
            vbCruds.setVisible(!vbCruds.isVisible());
            btManagement.getStyleClass().remove("menu-button");
            btManagement.getStyleClass().add("menu-button-selected");
            selected = btManagement;
        }else {
            b.getStyleClass().remove("menu-button");
            b.getStyleClass().add("menu-button-selected");
            selected = b;
        }
        lblHeader.setText(selected.getText());
    }

    @FXML
    void showCruds(ActionEvent event){
        vbCruds.setVisible(!vbCruds.isVisible());
    }

    @FXML
    void signOut(ActionEvent event) {
            FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
            Pane root = fxWeaver.loadView(Login.class);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
    }

    void initScene(){
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        if(selected == btManagement)
            rootPane.setCenter(fxWeaver.loadView(mapButtonClass.get(btSensorsCrud)));
        else
            rootPane.setCenter(fxWeaver.loadView(mapButtonClass.get(selected)));
        rootPane.getCenter().getStyleClass().add("bc-darkblue-alt");
        lblHeader.setText(selected.getText());
    }

}
