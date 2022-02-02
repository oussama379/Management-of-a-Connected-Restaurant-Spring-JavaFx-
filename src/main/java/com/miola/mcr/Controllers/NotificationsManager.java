package com.miola.mcr.Controllers;

import com.miola.mcr.JavaFxApplication;
import io.github.palexdev.materialfx.collections.CircularQueue;
import io.github.palexdev.materialfx.controls.MFXNotification;
import io.github.palexdev.materialfx.notifications.NotificationPos;
import io.github.palexdev.materialfx.notifications.PositionManager;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Window;
import javafx.util.Duration;
//import org.kordamp.ikonli.javafx.FontIcon;

import java.util.HashMap;
import java.util.Map;

public class NotificationsManager {
    private static final Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    private static final Window window = getWindow();
    private static final Map<NotificationPos, PositionManager> notifications = new HashMap();
    private static final CircularQueue<MFXNotification> notificationsHistory = new CircularQueue(20);

    public NotificationsManager() {
    }

    public static void send(NotificationPos pos, MFXNotification notification) {
        notifications.computeIfAbsent(pos, (notificationPos) -> {
            return new PositionManager(screenBounds, window, notificationPos);
        });
        ((PositionManager)notifications.get(pos)).show(notification);
        notificationsHistory.add(notification);
    }

    public static void send(NotificationPos pos, MFXNotification notification, double spacing) {
        notifications.computeIfAbsent(pos, (notificationPos) -> {
            return new PositionManager(screenBounds, window, notificationPos);
        });
        ((PositionManager)notifications.get(pos)).setSpacing(spacing).show(notification);
        notificationsHistory.add(notification);
    }

    public static void send(NotificationPos pos, MFXNotification notification, int limit) {
        notifications.computeIfAbsent(pos, (notificationPos) -> {
            return new PositionManager(screenBounds, window, notificationPos);
        });
        ((PositionManager)notifications.get(pos)).setLimit(limit).show(notification);
        notificationsHistory.add(notification);
    }

    public static void send(NotificationPos pos, MFXNotification notification, double spacing, int limit) {
        notifications.computeIfAbsent(pos, (notificationPos) -> {
            return new PositionManager(screenBounds, window, notificationPos);
        });
        ((PositionManager)notifications.get(pos)).setSpacing(spacing).setLimit(limit).show(notification);
        notificationsHistory.add(notification);
    }

    public static PositionManager getPositionManager(NotificationPos pos) {
        return (PositionManager)notifications.get(pos);
    }

    public CircularQueue<MFXNotification> getNotificationsHistory() {
        return notificationsHistory;
    }

    public static void setHistoryLimit(int size) {
        notificationsHistory.setSize(size);
    }

    private static Window getWindow() {
        return JavaFxApplication.stage;
    }
}
