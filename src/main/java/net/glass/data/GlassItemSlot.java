package net.glass.data;

import java.text.DecimalFormat;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.EquipmentSlot.Type;
import net.minecraft.text.Text;

public class GlassItemSlot {

    public static final double criticalThreshold = 0.01;
    public static final double warnThreshold = 0.05;
    public static DecimalFormat df = new DecimalFormat("#,####.##");

    private final EquipmentSlot slot;
    private final Type slotType;

    private int damageTaken;
    private int maxDamage;
    private boolean empty;
    private boolean isElytra;
    private boolean isUnbreakable;

    protected GlassItemSlot(EquipmentSlot slot) {
        this.slot = slot;
        damageTaken = 0;
        maxDamage = 0;
        empty = true;
        isElytra = false;
        this.slotType = this.slot.getType();
    }

    protected boolean isEmpty() {
        return empty;
    }

    protected void setEmpty() {
        this.empty = true;
        this.damageTaken = 0;
        this.maxDamage = 0;
        this.isElytra = false;
        this.isUnbreakable = false;
    }

    protected boolean isArmorSlot() {
        return slotType == Type.ARMOR;
    }

    protected boolean isHandSlot() {
        return slotType == Type.HAND;
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

    protected double healthPercentage() {
        if (maxDamage == 0) {
            return isUnbreakable ? 100 : 0;
        } else {
            return (double) (damageLeft()) / maxDamage;
        }
    }

    protected int damageLeft() {
        return this.damageTaken <= this.maxDamage
            ? (this.maxDamage - this.damageTaken)
            : 0;
    }

    protected int damageTaken() {
        return this.damageTaken;
    }

    protected int maxDamage() {
        return this.maxDamage;
    }

    protected String healthPercentageText() {
        if (empty || isUnbreakable) {
            return "-";
        } else {
            return GlassItemSlot.df.format(healthPercentage()) + "%";
        }
    }

    protected String damageDetailText() {
        if (empty || isUnbreakable) {
            return "-";
        } else {
            return "(" + damageLeft() + "/" + maxDamage + ")";
        }
    }

    protected String itemNameText() {
        if (isElytra) return "Elytra"; else return slot.getName();
    }
}
