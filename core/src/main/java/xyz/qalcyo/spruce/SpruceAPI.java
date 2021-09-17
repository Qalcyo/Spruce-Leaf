package xyz.qalcyo.spruce;

import xyz.qalcyo.simpleeventbus.SimpleEventBus;

public interface SpruceAPI {
    boolean initialize();

    SimpleEventBus getEventBus();
}