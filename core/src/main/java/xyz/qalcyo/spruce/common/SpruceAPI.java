package xyz.qalcyo.spruce.common;

import xyz.qalcyo.simpleeventbus.SimpleEventBus;
import xyz.qalcyo.spruce.common.util.MathHelper;

public interface SpruceAPI {
    boolean initialize();

    SimpleEventBus getEventBus();
    MathHelper getMathHelper();
}