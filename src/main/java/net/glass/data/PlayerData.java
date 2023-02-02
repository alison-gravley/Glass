package net.glass.data;

import static net.glass.GlassHud.LOGGER;
import static net.glass.GlassHud.SETTING;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerData {

    private static final double mspt = 50;

    private Vec3d lastPosition;
    private long lastTime;

    private double speed;
    private GlassItemSlot head;
    private GlassItemSlot chest;
    private GlassItemSlot legs;
    private GlassItemSlot feet;
    private GlassItemSlot mainhand;
    private GlassItemSlot offhand;

    protected PlayerData() {
        lastPosition = new Vec3d(0, 0, 0);
        lastTime = 0;
        speed = 0;
        head = new GlassItemSlot(EquipmentSlot.HEAD);
        chest = new GlassItemSlot(EquipmentSlot.CHEST);
        legs = new GlassItemSlot(EquipmentSlot.LEGS);
        feet = new GlassItemSlot(EquipmentSlot.FEET);
        mainhand = new GlassItemSlot(EquipmentSlot.MAINHAND);
        offhand = new GlassItemSlot(EquipmentSlot.OFFHAND);
    }

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
        if (worldTime > lastTime + 5) {
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
    protected void updateHead(@Nullable ItemStack head) {
        updateSlot(this.head, head);
    }

    /**
     *
     * @param chest Equipped item in the chest slot of the player. May be empty or null
     */
    protected void updateChest(@Nullable ItemStack chest) {
        updateSlot(this.chest, chest);
    }

    /**
     *
     * @param legs Equipped item in the leg slot of the player. May be empty or null
     */
    protected void updateLegs(@Nullable ItemStack legs) {
        updateSlot(this.legs, legs);
    }

    /**
     *
     * @param feet Equipped item in the foot slot of the player. May be empty or null
     */
    protected void updateFeet(@Nullable ItemStack feet) {
        updateSlot(this.feet, feet);
    }

    /**
     *
     * @param mainhand Item held in the main hand of the player. May be empty, null, or
     * a non-damageable item.
     */
    protected void updateMainHand(@Nullable ItemStack mainhand) {
        updateSlot(this.mainhand, mainhand);
    }

    /**
     *
     * @param offhand Item held in the off hand of the player. May be empty, null, or
     * a non-damageable item.
     */
    protected void updateOffHand(@Nullable ItemStack offhand) {
        updateSlot(this.offhand, offhand);
    }

    /**
     * @return The current calculated speed of the player
     */
    public double speed() {
        return this.speed;
    }

    /**
     *
     * @return The current calculated speed of the player formatted as a Text object
     */
    public Text speedText() {
        return Text.literal(GlassItemSlot.df.format(this.speed) + " m/s");
    }
}
