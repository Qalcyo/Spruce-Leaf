package xyz.qalcyo.spruce.client.notifications;

import java.util.function.Consumer;

public interface INotifications {
    void push(Notification notification);
    default void push(String title, String description, NotificationColour colour, int duration, Consumer<Notification> clickListener) {
        push(new Notification(title, description, colour, duration, clickListener));
    }
    default void push(String title, String description, Consumer<Notification> clickListener) {
        push(new Notification(title, description, clickListener));
    }
    default void push(String title, String description, int duration) {
        push(new Notification(title, description, duration));
    }
    default void push(String title, String description, NotificationColour colour) {
        push(new Notification(title, description, colour));
    }
    default void push(String title, String description, NotificationColour colour, int duration) {
        push(new Notification(title, description, colour, duration));
    }
    default void push(String title, String description, int duration, Consumer<Notification> clickListener) {
        push(new Notification(title, description, duration, clickListener));
    }
    default void push(String title, String description, NotificationColour colour, Consumer<Notification> clickListener) {
        push(new Notification(title, description, colour, clickListener));
    }
    default void push(String title, String description) {
        push(new Notification(title, description));
    }

    void render(float partialTicks);
}