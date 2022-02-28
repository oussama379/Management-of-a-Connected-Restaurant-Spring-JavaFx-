package com.miola.mcr;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PreloaderApplication extends Preloader {
    private Stage preloaderStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.preloaderStage = primaryStage;

//        primaryStage.setWidth(800);
//        primaryStage.setHeight(600);
        primaryStage.setScene(createPreloaderScene());
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
        if (stateChangeNotification.getType() == Type.BEFORE_START) {
            preloaderStage.hide();
        }
    }

    private Scene createPreloaderScene() {
        ImageView img = new ImageView(new Image(PreloaderApplication.class.getResource("/img/MCR.png").toExternalForm()));
        img.setFitHeight(450); img.setFitWidth(573);
        VBox p = new VBox();
//        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        MFXProgressBar pb = new MFXProgressBar(); pb.setPrefWidth(573);

        p.getChildren().addAll(img, pb);
        return new Scene(p);
    }
}