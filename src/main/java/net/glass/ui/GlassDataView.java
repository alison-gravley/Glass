package net.glass.ui;

import static net.glass.GlassHud.DATA;
import static net.glass.GlassHud.SETTING;

import java.text.DecimalFormat;
import lombok.NonNull;
import net.glass.data.GlassItemSlot;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.Text;

/**
 * Gets Data and formats it for the UI to use
 */
public class GlassDataView {

    public static DecimalFormat dfSpeed = new DecimalFormat("#,####.## m/s");
    public static DecimalFormat dfPercent = new DecimalFormat("###.##%");

    protected Text getSlotValueText(@NonNull EquipmentSlot slot) {
        var glassSlot = DATA.getPlayerData().getSlotByType(slot);
        String value = "";
        if (glassSlot.isEmpty()) return Text.empty();
        if (SETTING.SHOW_PERCENT_VAL()) value +=
            dfPercent.format(glassSlot.getHealthPercentage() * 100);
        if (SETTING.SHOW_DMG_VAL()) value +=
            "(" + glassSlot.getDamageLeft() + "/" + glassSlot.getMaxDamage() + ")";

        return Text.of(value);
    }

    protected Text getSpeedText() {
        return Text.literal(dfSpeed.format(DATA.getPlayerData().getSpeed()));
    }
}
