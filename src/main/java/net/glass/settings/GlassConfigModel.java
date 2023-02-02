package net.glass.settings;

import io.wispforest.owo.config.annotation.*;

@Modmenu(modId = "glasshud")
@Config(name = "glasshud", wrapperName = "GlassConfig")
public class GlassConfigModel {

    public boolean SHOW_SPEED = true;
    public boolean SHOW_DMG_VAL = true;
    public boolean SHOW_PERCENT_VAL = true;
    public boolean SHOW_ARMOR = true;
    public boolean SHOW_HANDS = true;
    public boolean TEXT_ONLY_MODE = false;
    public boolean BREAK_WARNING = false;
    public boolean BREAK_CRITICAL = false;
    public boolean UNEQUIP_ON_CRITICAL = false;

    @RangeConstraint(min = 0, max = 100)
    public int WARNING_THRESHOLD = 5;

    @RangeConstraint(min = 0, max = 100)
    public int CRITICAL_THRESHOLD = 1;
}
