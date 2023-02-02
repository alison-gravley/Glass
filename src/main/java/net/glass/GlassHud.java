package net.glass;

import net.fabricmc.api.ModInitializer;
import net.glass.data.GlassData;
import net.glass.settings.GlassConfig;
//import net.glass.settings.GlassConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlassHud implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("GlassHud");
    public static final GlassConfig SETTING = GlassConfig.createAndLoad();
    public static final GlassData DATA = new GlassData();

    @Override
    public void onInitialize() {}
}
