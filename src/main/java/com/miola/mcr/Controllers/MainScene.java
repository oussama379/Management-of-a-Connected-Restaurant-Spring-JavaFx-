package com.miola.mcr.Controllers;


import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXNotification;
import io.github.palexdev.materialfx.controls.SimpleMFXNotificationPane;
import io.github.palexdev.materialfx.notifications.NotificationPos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
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

    @FXML private MFXButton btnCustomerArea;
    @FXML private MFXButton btSignOut;

    @FXML private BorderPane rootPane;


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

    }

    @FXML
    void loadScene(ActionEvent event) {

        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        rootPane.setCenter(fxWeaver.loadView(mapButtonClass.get(event.getSource())));

//        if (event.getSource().equals(btUsersCrud)) {
//            Pane newLoadedPane = fxWeaver.loadView(CrudUser.class);
//            rootPane.setCenter(newLoadedPane);
//        } else if (event.getSource().equals(btZonesCrud)) {
//            Pane newLoadedPane;
//            newLoadedPane = fxWeaver.loadView(CrudZone.class);
//            rootPane.setCenter(newLoadedPane);
//        } else if (event.getSource().equals(btSensorsCrud)) {
//            Pane newLoadedPane;
//            newLoadedPane = fxWeaver.loadView(CrudSensor.class);
//            rootPane.setCenter(newLoadedPane);
//        } else if (event.getSource().equals(btDinningTablesCrud)) {
//            Pane newLoadedPane;
//            newLoadedPane = fxWeaver.loadView(CrudDinningTable.class);
//            rootPane.setCenter(newLoadedPane);
//        } else if (event.getSource().equals(btCategoriesCrud)) {
//            Pane newLoadedPane;
//            newLoadedPane = fxWeaver.loadView(CrudCategory.class);
//            rootPane.setCenter(newLoadedPane);
//        } else if (event.getSource().equals(btRolesCrud)) {
//            Pane newLoadedPane;
//            newLoadedPane = fxWeaver.loadView(CrudRole.class);
//            rootPane.setCenter(newLoadedPane);
//        } else if (event.getSource().equals(btPermissionsCrud)) {
//            Pane newLoadedPane;
//            newLoadedPane = fxWeaver.loadView(CrudPermission.class);
//            rootPane.setCenter(newLoadedPane);
//        }else if (event.getSource().equals(btAlertesCrud)) {
//            Pane newLoadedPane;
//            newLoadedPane = fxWeaver.loadView(CrudAlerte.class);
//            rootPane.setCenter(newLoadedPane);
//        }else if (event.getSource().equals(btDevicesCrud)) {
//            Pane newLoadedPane;
//            newLoadedPane = fxWeaver.loadView(CrudDevice.class);
//            rootPane.setCenter(newLoadedPane);
//        }else {
//            rootPane.setCenter(null);
//        }
    }

}
