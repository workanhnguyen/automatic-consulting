package com.nva.server.views.components;

import com.vaadin.flow.component.notification.Notification;

public class CustomNotification {
    public static void showNotification(String message, String type, Notification.Position position, int duration) {
        Notification notification = new Notification(message, duration);
        notification.setPosition(position);
        notification.getElement().getThemeList().add(type);
        notification.open();
    }
}
