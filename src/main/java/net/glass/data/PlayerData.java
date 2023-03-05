package net.glass.data;

import static net.glass.GlassHud.LOGGER;
import static net.glass.GlassHud.SETTING;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;

@Getter
public class PlayerData {

    private static final double mspt = 50;

    @Getter(AccessLevel.NONE)
    private Vec3d lastPosition = new Vec3d(0, 0, 0);

    @Getter(AccessLevel.NONE)
    private long lastTime = 0;

    private double speed = 0;
    private final GlassItemSlot head = new GlassItemSlot(EquipmentSlot.HEAD);
    private final GlassItemSlot chest = new GlassItemSlot(EquipmentSlot.CHEST);
    private final GlassItemSlot legs = new GlassItemSlot(EquipmentSlot.LEGS);
    private final GlassItemSlot feet = new GlassItemSlot(EquipmentSlot.FEET);
    private final GlassItemSlot mainhand = new GlassItemSlot(EquipmentSlot.MAINHAND);
    private final GlassItemSlot offhand = new GlassItemSlot(EquipmentSlot.OFFHAND);

    protected PlayerData() {}

    /**
     * Calculates the current Blocks Per Second of the player by interpolating
     * the world time and distance travelled within a timeslice of ~5 ticks
     * @param pos Current Position of Player
     * @param worldTime Current Time from World (Not Time of Day)
     */
    protected void updateSpeed(Vec3d pos, long worldTime) {
        if (pos == null) {
            LOGGER.warn(
                "updateSpeed called with null position and a worldTime of " + worldTime
            );
            return;
        }
        if (lastTime == 0 || lastTime > worldTime) {
            lastTime = worldTime;
            lastPosition = pos;
            return;
        }

        //Only do this every 5 ticks or so
        if (worldTime > lastTime + SETTING.SPEED_UPDATE_RATE()) {
            double timePassed = mspt * (worldTime - lastTime);
            this.speed = (pos.distanceTo(lastPosition) / timePassed) * 1000;
            lastPosition = pos;
            lastTime = worldTime;
        }
    }

    /**
     * Helper function to update the damage state of a slot with the current item from the player
     * @param slot Internal slot to update
     * @param item Current item held or equipped by the player
     */
    private void updateSlot(GlassItemSlot slot, ItemStack item) {
        if (item == null || item.isEmpty()) {
            slot.setEmpty();
        } else {
            slot.updateSlotHealth(
                item.getDamage(),
                item.getMaxDamage(),
                item.isOf(Items.ELYTRA),
                !item.isDamageable()
            );
        }
    }

    /**
     *
     * @param head Equipped item in the head slot of the player. May be empty or null
     */
    protected void updateHead(ItemStack head) {
        updateSlot(this.head, head);
    }

    /**
     *
     * @param chest Equipped item in the chest slot of the player. May be empty or null
     */
    protected void updateChest(ItemStack chest) {
        updateSlot(this.chest, chest);
    }

    /**
     *
     * @param legs Equipped item in the leg slot of the player. May be empty or null
     */
    protected void updateLegs(ItemStack legs) {
        updateSlot(this.legs, legs);
    }

    /**
     *
     * @param feet Equipped item in the foot slot of the player. May be empty or null
     */
    protected void updateFeet(ItemStack feet) {
        updateSlot(this.feet, feet);
    }

    /**
     *
     * @param mainhand Item held in the main hand of the player. May be empty, null, or
     * a non-damageable item.
     */
    protected void updateMainHand(ItemStack mainhand) {
        updateSlot(this.mainhand, mainhand);
    }

    /**
     *
     * @param offhand Item held in the off hand of the player. May be empty, null, or
     * a non-damageable item.
     */
    protected void updateOffHand(ItemStack offhand) {
        updateSlot(this.offhand, offhand);
    }

    public GlassItemSlot getSlotByType(@NonNull EquipmentSlot type) {
        switch (type) {
            case CHEST:
                return chest;
            case FEET:
                return feet;
            case HEAD:
                return head;
            case LEGS:
                return legs;
            case MAINHAND:
                return mainhand;
            case OFFHAND:
                return offhand;
            default:
                return head;
        }
    }
}
