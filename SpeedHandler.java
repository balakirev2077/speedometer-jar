package com.example.speedometer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class SpeedHandler {
    /** Сколько последних тиков усредняем (сглаживание). */
    private static final int WINDOW = 5;
    /** Блоков в секунду -> км/ч (1 блок = 1 метр, 20 тиков = 1 секунда). */
    private static final double BLOCKS_PER_TICK_TO_KMH = 20.0 * 3.6;

    private final double[] samples = new double[WINDOW];
    private int index = 0;
    private double speedKmh = 0;

    private boolean enabled = true;
    private boolean includeVertical = false; // false = только горизонтальная скорость

    private final KeyBinding toggleKey =
            new KeyBinding("Показывать скорость", Keyboard.KEY_K, "Speedometer");
    private final KeyBinding modeKey =
            new KeyBinding("Учитывать вертикаль", Keyboard.KEY_J, "Speedometer");

    public void registerKeys() {
        ClientRegistry.registerKeyBinding(toggleKey);
        ClientRegistry.registerKeyBinding(modeKey);
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent e) {
        if (toggleKey.isPressed()) enabled = !enabled;
        if (modeKey.isPressed()) includeVertical = !includeVertical;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP p = mc.player;
        if (p == null || mc.isGamePaused()) return;

        double dx = p.posX - p.prevPosX;
        double dy = includeVertical ? p.posY - p.prevPosY : 0;
        double dz = p.posZ - p.prevPosZ;
        double perTick = Math.sqrt(dx * dx + dy * dy + dz * dz);

        // Отсекаем телепортации / смену измерения
        if (perTick > 10) perTick = 0;

        samples[index] = perTick * BLOCKS_PER_TICK_TO_KMH;
        index = (index + 1) % WINDOW;

        double sum = 0;
        for (double s : samples) sum += s;
        speedKmh = sum / WINDOW;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post e) {
        if (e.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (!enabled || mc.player == null || mc.gameSettings.showDebugInfo) return;

        String text = String.format("Скорость: %.1f км/ч", speedKmh);
        // Цвет: белый -> жёлтый -> красный по мере роста скорости
        int color = speedKmh < 20 ? 0xFFFFFF : speedKmh < 50 ? 0xFFFF55 : 0xFF5555;
        mc.fontRenderer.drawStringWithShadow(text, 4, 4, color);
    }
}
