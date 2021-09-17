package xyz.qalcyo.spruce.client.events;

public class RenderTickEvent extends ClientTickEvent {
    public final float partialTicks;
    public RenderTickEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}