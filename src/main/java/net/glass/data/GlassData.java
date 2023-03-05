package net.glass.data;

import static net.glass.GlassHud.SETTING;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.map.MapState.PlayerUpdateTracker;
import net.minecraft.text.Text;

/**
 * Use this to interact with the underlying data and update it
 */
public class GlassData {

    private final MinecraftClient client;
    private final PlayerData playerData = new PlayerData();
    private long lastUpdatedArmor = 0;

    public GlassData() {
        client = MinecraftClient.getInstance();
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    /**
     * This will update the current state of the player data
     */
    public void updatePlayerData() {
        if (client == null || client.world == null || client.player == null) return;
        updateSpeed();
        if (SETTING.ARMOR_UPDATE_RATE() + lastUpdatedArmor < client.world.getTime()) {
            updateEquippedItems();
        }
    }

    private void updateSpeed() {
        if (SETTING.SHOW_SPEED()) {
            playerData.updateSpeed(client.player.getPos(), client.world.getTime());
        }
    }

    private void updateEquippedItems() {
        if (SETTING.SHOW_ARMOR()) {
            playerData.updateHead(client.player.getEquippedStack(EquipmentSlot.HEAD));
            playerData.updateChest(client.player.getEquippedStack(EquipmentSlot.CHEST));
            playerData.updateLegs(client.player.getEquippedStack(EquipmentSlot.LEGS));
            playerData.updateFeet(client.player.getEquippedStack(EquipmentSlot.FEET));
        }

        if (SETTING.SHOW_HANDS()) {
            playerData.updateMainHand(client.player.getMainHandStack());
            playerData.updateOffHand(client.player.getOffHandStack());
        }
    }
}
