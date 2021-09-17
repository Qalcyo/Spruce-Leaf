package xyz.qalcyo.spruce1171.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import xyz.qalcyo.mango.Lists;
import xyz.qalcyo.spruce.client.events.RenderTickEvent;
import xyz.qalcyo.spruce.client.notifications.INotifications;
import xyz.qalcyo.spruce.client.notifications.Notification;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Notifications extends DrawableHelper implements INotifications {

    private final SpruceClient spruce;
    private final List<Notification> notifications = new CopyOnWriteArrayList<>();

    public Notifications(SpruceClient spruce) {
        this.spruce = spruce;
        spruce.getEventBus().register(RenderTickEvent.class, this::onRenderTick);
    }

    public void push(Notification notification) {
        notifications.add(notification);
    }

    public void render(float ticks) {
        int scaledWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
        int scaledHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();

        float y = 5;
        for (Notification notification : notifications) {
            if (notifications.indexOf(notification) > 2)
                continue;

            if (notification.data.x < 1)
                notification.data.x = scaledWidth;

            int duration = (notification.duration == -1 ? 4 : notification.duration);

            /* Text. */
            String title = Formatting.BOLD + notification.title;
            float width = 225;
            List<String> wrappedTitle = wrapTextLines(title, (int) (width - 10), " ");
            List<String> wrappedDescription = wrapTextLines(notification.description, (int) (width - 10), " ");
            int textLines = wrappedTitle.size() + wrappedDescription.size();

            /* Size and positon. */
            float height = 18 + (textLines * MinecraftClient.getInstance().textRenderer.fontHeight);
            float x = notification.data.x = spruce.getMathHelper().lerp(notification.data.x, scaledWidth - width - 5, ticks / 4);
            if (notification.data.closing && notification.data.time < 0.75f)
                x = notification.data.x = spruce.getMathHelper().lerp(notification.data.x, scaledWidth + width, ticks / 4);

            /* Mouse handling. */
            float mouseX = (float) MinecraftClient.getInstance().mouse.getX();
            float mouseY = (float) MinecraftClient.getInstance().mouse.getY();
            boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
            if (hovered && !notification.data.clicked && MinecraftClient.getInstance().mouse.wasLeftButtonClicked()) {
                notification.data.clicked = true;
                notification.click();
                notification.data.closing = true;
            }

            MatrixStack matrices = new MatrixStack();

            matrices.push();

            Color backgroundColour = notification.colour == null || notification.colour.background == null ? new Color(0, 0, 0, 200) : notification.colour.background;
            fill(matrices, (int) x, (int) y, (int) width, (int) height, backgroundColour.getRGB());
            Color foregroundColour = notification.colour == null || notification.colour.foreground == null ? new Color(255, 175, 0, 200) : notification.colour.foreground;
            drawHollowRect((int) x + 4, (int) y + 4, (int) width - 8, (int) height - 8, 1, foregroundColour.getRGB());

            /* Text. */
            if (notification.data.time > 0.1f) {
                Color textColour = new Color(255, 255, 255, 200);
                startScissorBox(x, y, width, height);
                int i = 0;
                for (String line : wrappedTitle) {
                    MinecraftClient.getInstance().textRenderer.draw(matrices, line, x + 8, y + 8 + (i * 2) + (i * MinecraftClient.getInstance().textRenderer.fontHeight), textColour.getRGB());
                    i++;
                }
                for (String line : wrappedDescription) {
                    MinecraftClient.getInstance().textRenderer.draw(matrices, new LiteralText(line), x + 8, y + 8 + (i * 2) + (i * MinecraftClient.getInstance().textRenderer.fontHeight), textColour.getRGB());
                    i++;
                }
                endScissorBox();
            }

            matrices.pop();

            /* Positioning. */
            y += height + 5;

            /* Other handling things. */
            if (notification.data.time >= duration)
                notification.data.closing = true;
            if (!hovered)
                notification.data.time += (notification.data.closing ? -0.02 : 0.02) * (ticks * 3);
            if (notification.data.closing && notification.data.time <= 0)
                notifications.remove(notification);
        }
    }

    private void onRenderTick(RenderTickEvent event) {
        render(event.partialTicks);
    }

    public void startScissorBox(int x, int y, int width, int height) {
        totalScissor(x, y, width, height);
    }

    public void startScissorBox(float x, float y, float width, float height) {
        startScissorBox((int) x, (int) y, (int) width, (int) height);
    }

    public void endScissorBox() {
        RenderSystem.disableScissor();
    }

    public void totalScissor(double xPosition, double yPosition, double width, double height) {
        Window window = MinecraftClient.getInstance().getWindow();
        int windowWidth = window.getWidth();
        int scaledWidth = window.getScaledWidth();
        int windowHeight = window.getHeight();
        int scaledHeight = window.getScaledHeight();
        RenderSystem.enableScissor((int) ((xPosition * windowWidth) / scaledWidth), (int) (((scaledHeight - (yPosition + height)) * windowHeight) / scaledHeight), (int) (width * windowWidth / scaledWidth), (int) (height * windowHeight / scaledHeight));
    }


    public void drawHollowRect(int x, int y, int width, int height, int thickness, int colour) {
        drawHorizontalLine(x, x + width, y, thickness, colour);
        drawHorizontalLine(x, x + width, y + height, thickness, colour);
        drawVerticalLine(x, y + height, y, thickness, colour);
        drawVerticalLine(x + width, y + height, y, thickness, colour);
    }

    public void drawHorizontalLine(MatrixStack matrices, int start, int end, int y, int thickness, int colour) {
        for (int i = 0; i < thickness; i++) {
            drawHorizontalLine(matrices, start, end, y + i, colour);
        }
    }

    public void drawHorizontalLine(int start, int end, int y, int thickness, int colour) {
        drawHorizontalLine(new MatrixStack(), start, end, y, thickness, colour);
    }

    public void drawVerticalLine(MatrixStack matrices, int x, int start, int end, int thickness, int colour) {
        for (int i = 0; i < thickness; i++) {
            drawVerticalLine(matrices, x + i, start, end, colour);
        }
    }

    public void drawVerticalLine(int x, int start, int end, int thickness, int colour) {
        drawVerticalLine(new MatrixStack(), x, start, end, thickness, colour);
    }

    /**
     * Adapted from XanderLib under GPL 3.0 license
     * https://github.com/isXander/XanderLib/blob/main/LICENSE
     *
     * @author isXander
     */
    private List<String> wrapTextLines(String text, int width, String split) {
        String wrapped = wrapText(text, width, split);
        if (wrapped.isEmpty())
            return Lists.newArrayList();
        return Arrays.asList(wrapped.split("\n"));
    }

    /**
     * Adapted from XanderLib under GPL 3.0 license
     * https://github.com/isXander/XanderLib/blob/main/LICENSE
     *
     * @author isXander
     */
    private String wrapText(String text, int width, String split) {
        String[] words = text.split("(" + split + "|\n)");
        int lineLength = 0;
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i != words.length - 1)
                word += split;
            int wordLength = MinecraftClient.getInstance().textRenderer.getWidth(text);
            if (lineLength + wordLength <= width) {
                output.append(word);
                lineLength += wordLength;
            } else if (wordLength <= width) {
                output.append("\n").append(word);
                lineLength = wordLength;
            } else
                output.append(wrapText(word, width, "")).append(split);
        }
        return output.toString();
    }

}