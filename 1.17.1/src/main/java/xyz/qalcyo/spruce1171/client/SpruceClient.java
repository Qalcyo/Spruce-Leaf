package xyz.qalcyo.spruce1171.client;

import xyz.qalcyo.simpleeventbus.SimpleEventBus;
import xyz.qalcyo.spruce.client.ISpruceClient;
import xyz.qalcyo.spruce.client.notifications.INotifications;
import xyz.qalcyo.spruce.common.util.MathHelper;

public class SpruceClient implements ISpruceClient {

    private static final SpruceClient INSTANCE = new SpruceClient();
    private static boolean initialized = false;

    private SimpleEventBus eventBus;
    private MathHelper mathHelper;
    private Notifications notifications;

    public boolean initialize() {
        if (initialized)
            return false;
        initialized = true;

        eventBus = new SimpleEventBus();
        mathHelper = new MathHelper();
        notifications = new Notifications(this);
        notifications.push("Hello!", "How are you?");

        return true;
    }

    public SimpleEventBus getEventBus() {
        return eventBus;
    }

    public MathHelper getMathHelper() {
        return mathHelper;
    }

    public INotifications getNotifications() {
        return notifications;
    }

    public static SpruceClient getInstance() {
        return INSTANCE;
    }

    public static boolean isInitialized() {
        return initialized;
    }

}