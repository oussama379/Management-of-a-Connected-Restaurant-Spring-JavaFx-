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
            Pane newLoadedPane =  fxWeaver.loadView(CrudUser.class);
            rootPane.setCenter(newLoadedPane);
        }else{
            rootPane.setCenter(null);
        }
    }

}
