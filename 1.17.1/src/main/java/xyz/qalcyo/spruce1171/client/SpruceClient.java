package xyz.qalcyo.spruce1171.client;

import xyz.qalcyo.simpleeventbus.SimpleEventBus;

public class SpruceClient implements xyz.qalcyo.spruce.client.SpruceClient {

    private static SpruceClient INSTANCE = new SpruceClient();
    private static boolean initialized = false;

    private SimpleEventBus eventBus;

    public boolean initialize() {
        if (initialized)
            return false;
        initialized = true;

        eventBus = new SimpleEventBus();

        return true;
    }

    public SimpleEventBus getEventBus() {
        return eventBus;
    }

    public static SpruceClient getInstance() {
        return INSTANCE;
    }

    public static boolean isInitialized() {
        return initialized;
    }

}