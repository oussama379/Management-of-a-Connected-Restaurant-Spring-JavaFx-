package com.miola.mcr.Controllers;


import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXNotification;
import io.github.palexdev.materialfx.controls.SimpleMFXNotificationPane;
import io.github.palexdev.materialfx.notifications.NotificationPos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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
    @FXML private MFXButton btCategoriesCrud;
    @FXML private MFXButton btRolesCrud;
    @FXML private MFXButton btAlertesCrud;
    @FXML private MFXButton btPermissionsCrud;
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

    private MFXButton selected;


    @Autowired
    public MainScene(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /* CRUDs */
        mapButtonClass.put(btUsersCrud, CrudUser.class);
        mapButtonClass.put(btZonesCrud, CrudZone.class);
        mapButtonClass.put(btSensorsCrud, CrudSensor.class);
        mapButtonClass.put(btDinningTablesCrud, CrudDinningTable.class);
        mapButtonClass.put(btCategoriesCrud, CrudCategory.class);
        mapButtonClass.put(btRolesCrud, CrudRole.class);
        mapButtonClass.put(btPermissionsCrud, CrudPermission.class);
        mapButtonClass.put(btAlertesCrud, CrudAlerte.class);
        mapButtonClass.put(btDevicesCrud, CrudDevice.class);
        mapButtonClass.put(btMenuItemCrud, CrudMenuItem.class);

        /* DashBoards */
        mapButtonClass.put(btEnergyDB, DBEnergy.class);
        mapButtonClass.put(btnAirDB, DBAir.class);

        mapButtonClass.put(btnCustomerArea, Orders.class);
        mapButtonClass.put(btnStatistics, Statistics.class);

        btHome.getStyleClass().remove("menu-button");
        btHome.getStyleClass().add("menu-button-selected");
        selected = btHome;
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

    }

    @FXML
    void showCruds(ActionEvent event){
        vbCruds.setVisible(!vbCruds.isVisible());
    }

}
