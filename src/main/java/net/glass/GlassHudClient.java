package net.glass;

import static net.glass.GlassHud.DATA;
import static net.glass.GlassHud.LOGGER;

import io.wispforest.owo.ui.hud.Hud;
import io.wispforest.owo.ui.hud.HudInspectorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.glass.ui.GlassUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class GlassHudClient implements ClientModInitializer {

    private static GlassUI hudUI = null;
    private static KeyBinding openGlass;
    private static Identifier glassHudID;

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

        glassHudID = new Identifier("glasshud", "base-hud");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (
                MinecraftClient.isHudEnabled() &&
                MinecraftClient.getInstance().player != null &&
                MinecraftClient.getInstance().world != null &&
                !MinecraftClient.getInstance().isPaused() &&
                MinecraftClient.getInstance().isRunning()
            ) {
                if (hudUI == null) return;
                while (openGlass.wasPressed()) {
                    LOGGER.info("F9 was pressed!");

                    MinecraftClient.getInstance().setScreen(new HudInspectorScreen());
                    /*
                    if (Hud.hasComponent(glassHudID)) {
                        LOGGER.info("Removing base-hud");
                        Hud.remove(glassHudID);
                    } else {
                        LOGGER.info("Adding base-hud");
                        createOrAddGlassHud();
                    }
                    */
                }
                DATA.updatePlayerData();
                hudUI.updateUI();
            }
        });

        createOrAddGlassHud();
    }

    private void createOrAddGlassHud() {
        Hud.add(
            glassHudID,
            () -> {
                if (hudUI == null) {
                    LOGGER.trace("GlassHudClient::createOrAddGlassHud hudUI is null");
                    hudUI = new GlassUI(Identifier.of("glasshud", "armor"));
                }
                return hudUI.createAdapter().rootComponent;
            }
        );
    }
}
