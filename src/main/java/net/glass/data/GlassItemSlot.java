package net.glass.data;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.EquipmentSlot.Type;

public class GlassItemSlot {

    public static final double criticalThreshold = 0.01;
    public static final double warnThreshold = 0.05;

    private final EquipmentSlot slot;
    private final Type slotType;

    private int damageTaken = 0;
    private int maxDamage = 0;
    private boolean empty = true;
    private boolean isElytra = false;
    private boolean isUnbreakable = false;

    protected GlassItemSlot(EquipmentSlot slot) {
        this.slot = slot;
        this.slotType = this.slot.getType();
    }

    /*
     * Protected Setters
     */

    protected void setEmpty() {
        this.empty = true;
        this.damageTaken = 0;
        this.maxDamage = 0;
        this.isElytra = false;
        this.isUnbreakable = false;
    }

    protected void updateSlotHealth(
        int current,
        int max,
        boolean isElytra,
        boolean unbreakable
    ) {
        this.isElytra = isElytra;
        this.isUnbreakable = unbreakable;
        if (isUnbreakable) {
            this.damageTaken = 0;
            this.maxDamage = 0;
        } else {
            updateSlotHealth(current, max);
        }
    }

    protected void updateSlotHealth(int current, int max) {
        if (max <= 0) {
            this.maxDamage = 0;
            this.damageTaken = 0;
            this.empty = true;
        }
        if (current <= 0) {
            this.damageTaken = 0;
            if (!this.isElytra) {
                this.maxDamage = 0;
                this.empty = true;
            }
        }
        if (this.damageTaken > this.maxDamage) {
            this.damageTaken = 0;
            this.maxDamage = 0;
            this.empty = true;
        }
    }

    /*
     * Public Getters
     */

    public boolean isEmpty() {
        return empty;
    }

    public boolean isArmorSlot() {
        return slotType == Type.ARMOR;
    }

    public boolean isHandSlot() {
        return slotType == Type.HAND;
    }

    public double getHealthPercentage() {
        if (maxDamage == 0) {
            return isUnbreakable ? 1 : 0;
        } else {
            return (double) (getDamageLeft()) / maxDamage;
        }
    }

    public int getDamageLeft() {
        return this.damageTaken <= this.maxDamage
            ? (this.maxDamage - this.damageTaken)
            : 0;
    }

    public int getDamageTaken() {
        return this.damageTaken;
    }

    public int getMaxDamage() {
        return this.maxDamage;
    }

    protected String damageDetailText() {
        if (empty || isUnbreakable) {
            return "-";
        } else {
            return "(" + getDamageLeft() + "/" + maxDamage + ")";
        }
    }

    protected String itemNameText() {
        if (isElytra) return "Elytra"; else return slot.getName();
    }
}
