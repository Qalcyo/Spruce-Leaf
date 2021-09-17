package xyz.qalcyo.spruce.client;

import xyz.qalcyo.spruce.client.notifications.INotifications;
import xyz.qalcyo.spruce.common.SpruceAPI;

public interface ISpruceClient extends SpruceAPI {
    INotifications getNotifications();
}