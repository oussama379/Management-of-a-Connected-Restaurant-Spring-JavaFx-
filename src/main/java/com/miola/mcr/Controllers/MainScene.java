package com.miola.mcr.Controllers;


import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@FxmlView
public class MainScene {

    private final ConfigurableApplicationContext applicationContext;

    @FXML
    private MFXButton btUsersCrud;

    @FXML
    private MFXButton btZonesCrud;

    @FXML
    private MFXButton btSensorsCrud;

    @FXML
    private MFXButton btDinningTablesCrud;

    @FXML
    private MFXButton btCategoriesCrud;


    @FXML
    private BorderPane rootPane;

    @Autowired
    public MainScene(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @FXML
    void loadScene(ActionEvent event) {
        String fxmlFile = "";
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);

        if (event.getSource().equals(btUsersCrud)) {
            Pane newLoadedPane = fxWeaver.loadView(CrudUser.class);
            rootPane.setCenter(newLoadedPane);
        } else if (event.getSource().equals(btZonesCrud)) {
            Pane newLoadedPane;
            newLoadedPane = fxWeaver.loadView(CrudZone.class);
            rootPane.setCenter(newLoadedPane);
        } else if (event.getSource().equals(btSensorsCrud)) {
            Pane newLoadedPane;
            newLoadedPane = fxWeaver.loadView(CrudSensor.class);
            rootPane.setCenter(newLoadedPane);
        } else if (event.getSource().equals(btDinningTablesCrud)) {
            Pane newLoadedPane;
            newLoadedPane = fxWeaver.loadView(CrudDinningTable.class);
            rootPane.setCenter(newLoadedPane);
        } else if (event.getSource().equals(btCategoriesCrud)) {
            Pane newLoadedPane;
            newLoadedPane = fxWeaver.loadView(CrudCategory.class);
            rootPane.setCenter(newLoadedPane);
        } else {
            rootPane.setCenter(null);
        }

    }

}
