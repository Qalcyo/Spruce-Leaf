package xyz.qalcyo.spruce189.server;

import xyz.qalcyo.simpleeventbus.SimpleEventBus;

public class SpruceServer implements xyz.qalcyo.spruce.server.SpruceServer {

    private static SpruceServer INSTANCE = new SpruceServer();
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

    public static SpruceServer getInstance() {
        return INSTANCE;
    }

    public static boolean isInitialized() {
        return initialized;
    }

}