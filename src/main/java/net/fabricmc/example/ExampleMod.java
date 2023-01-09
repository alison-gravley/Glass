package net.fabricmc.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.util.Identifier;

import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.hud.Hud;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
public class ExampleMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("amg_modid");
	public static MyFirstScreen screen;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}
}
*/

public class ExampleMod implements ClientModInitializer {
	// public static GlassScreen glassHud;
	public static final Logger LOGGER = LoggerFactory.getLogger("amg_modid");

	@Override
	public void onInitializeClient() {

		// glassHud = new GlassScreen();
		// glassHud.initializeComponents();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (MinecraftClient.isHudEnabled() && MinecraftClient.getInstance().player != null) {
				GlassScreen.getInstance().updateComponents();
			}
		});

		// Hud.add(new Identifier("owo-ui-academy", "hint"),
		// () -> Containers.verticalFlow(Sizing.content(),
		// Sizing.content()).child(Components.label(
		// Text.empty().append("emptyText").formatted(Formatting.WHITE,
		// Formatting.BOLD))
		// .horizontalTextAlignment(HorizontalAlignment.LEFT))
		// .padding(Insets.of(5))
		// .positioning(Positioning.relative(0, 0)));

		Hud.add(new Identifier("owo-ui-academy", "hint"),
				() -> Containers.verticalFlow(Sizing.content(), Sizing.content())
						.children(GlassScreen.getInstance().getComponents())
						.padding(Insets.of(5))
						.positioning(Positioning.relative(0, 0)));

	}
}
