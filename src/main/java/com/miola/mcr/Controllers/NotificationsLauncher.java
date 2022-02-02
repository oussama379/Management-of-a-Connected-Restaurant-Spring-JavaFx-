package com.miola.mcr.Controllers;

import io.github.palexdev.materialfx.controls.MFXNotification;
import io.github.palexdev.materialfx.controls.SimpleMFXNotificationPane;
import io.github.palexdev.materialfx.font.FontResources;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.notifications.NotificationPos;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Random;

public class NotificationsLauncher {

    public enum Severity  {Minor, Major, Critical;}
    public enum AlertType {Energy, Temperature, CO2, VOC;}
    private AlertType alertType;
    private Severity severity;
    private String data;
    private String operator;
    private double value;

    private static double space = 0.0;

    public NotificationsLauncher(AlertType alertType, Severity severity, String data, String operator, double value) {
        this.alertType = alertType;
        this.severity = severity;
        this.data = data;
        this.operator = operator;
        this.value = value;
    }

    public void showTopRight() {
        NotificationPos pos = NotificationPos.TOP_RIGHT;
        showNotification(pos);
    }

    private void showNotification(NotificationPos pos) {
        MFXNotification notification = buildNotification();
        NotificationsManager.send(pos, notification);
    }

    private MFXNotification buildNotification() {
        Region template = getTemplate();
        MFXNotification notification = new MFXNotification(template, true);
//        notification.setHideAfterDuration(Duration.seconds(3));
        SimpleMFXNotificationPane pane = (SimpleMFXNotificationPane) template;
        pane.setCloseHandler(closeEvent -> notification.hideNotification());
        return notification;
    }

    private Region getTemplate() {
        String header = "";
        String title = "";
        String style = "";
        MFXFontIcon icon = null;
        SimpleMFXNotificationPane p;

        switch (alertType){
            case Energy -> {
                header= "Energy - Notification";
                title="The appliance '"+data+"' is still ON";
            }
            case Temperature -> {
                header= "Temperature - Notification";
                title="The temperature in '"+data+"' is "+operator+" "+value;
            }
            case CO2,VOC -> {
                header= "Air - Notification";
                title="The "+alertType+" level in '"+data+"' is "+operator+" "+value;
            }
            default -> {}
        }

        switch (severity){
            case Minor -> {
                icon = new MFXFontIcon(FontResources.INFO_CIRCLE.getDescription(), 15);
                style = "-fx-background-color: orange; -fx-text-background-color: white";
            }
            case Major -> {
                icon = new MFXFontIcon(FontResources.EXCLAMATION_TRIANGLE.getDescription(), 15);
                style = "-fx-background-color: orangered; -fx-text-background-color: white";
            }
            case Critical -> {
                icon = new MFXFontIcon(FontResources.X_CIRCLE.getDescription(), 15);
                style = "-fx-background-color: red; -fx-text-background-color: white";
            }
        }

        icon.setColor(Color.WHITE);
        p = new SimpleMFXNotificationPane(icon, header, title, "");
        p.setStyle(style);
        p.getTitleLabel().setWrapText(true);
        p.getChildren().removeAll(p.getContentScroll(), p.getButtonsBox());
        p.setPrefSize(450.0D, 70.0D);
        p.getTitleLabel().setStyle("-fx-font-size: 18;");

        return p;
    }
}
