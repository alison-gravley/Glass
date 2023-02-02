package net.glass;

import static net.glass.GlassHud.DATA;

import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.VerticalFlowLayout;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.hud.Hud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.glass.ui.GlassUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class GlassHudClient implements ClientModInitializer {

    private boolean started = false;
    private static GlassUI hudUI = null;
    private static KeyBinding openGlass;

    @Override
    public void onInitializeClient() {
        openGlass =
            KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                    "key.glasshud.open",
                    InputUtil.Type.KEYSYM,
                    InputUtil.GLFW_KEY_F9,
                    "category.glasshud.open"
                )
            );
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (
                MinecraftClient.isHudEnabled() &&
                MinecraftClient.getInstance().player != null &&
                MinecraftClient.getInstance().world != null &&
                !MinecraftClient.getInstance().isPaused() &&
                MinecraftClient.getInstance().isRunning()
            ) {
                DATA.updatePlayerData();
                if (hudUI != null) {
                    if (!started) {
                        hudUI.build();
                        started = true;
                    }
                    hudUI.build();
                }
            }
        });

        Hud.add(
            new Identifier("fuckyou"),
            () -> {
                hudUI = new GlassUI();

                return Containers
                    .verticalFlow(Sizing.content(), Sizing.content())
                    .child(hudUI.getRoot())
                    .positioning(Positioning.relative(0, 0));
            }
        );
    }
}
