package net.glass.ui;

import static net.glass.GlassHud.LOGGER;
import static net.glass.GlassHud.SETTING;

import io.wispforest.owo.ui.base.BaseUIModelScreen.DataSource;
import io.wispforest.owo.ui.component.BoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.parsing.UIModel;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.util.log.Log;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GlassUI {

    private UIModel myModel;
    private final DataSource xmlDataSource;
    private OwoUIAdapter<FlowLayout> adapter = null;
    private FlowLayout parent = null;
    private final GlassDataView viewData = new GlassDataView();
    private final float slotLabelScale = 0.5f;

    private FlowLayout speed_layout;
    private CustomLabelComponent speed_prefix = null;
    private CustomLabelComponent speed_value = null;

    private Map<EquipmentSlot, CustomLabelComponent> slot_values = new HashMap<>(6);
    private Map<EquipmentSlot, BoxComponent> slot_fillbars = new HashMap<>(6);

    public GlassUI(@NonNull Identifier xmlPath) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            LOGGER.info("LOAD UI IN DEBUG");
            xmlDataSource = DataSource.file(xmlPath.getPath() + ".xml");
        } else {
            xmlDataSource = DataSource.asset(xmlPath);
        }
        myModel = xmlDataSource.get();
    }

    public OwoUIAdapter<FlowLayout> createAdapter() {
        //We don't know the dimensions...hope this works
        if (adapter == null) {
            adapter = myModel.createAdapterWithoutScreen(0, 0, 0, 0, FlowLayout.class);
            adapter.rootComponent.positioning(Positioning.absolute(0, 0));
            parent = adapter.rootComponent;
        }
        return adapter;
    }

    private void findSlotChildren() {
        if (parent == null) return;
        EnumSet
            .allOf(EquipmentSlot.class)
            .forEach(slot -> {
                BoxComponent b = parent.childById(
                    BoxComponent.class,
                    ("slot-" + slot.getName() + "-fillbar")
                );
                if (b == null) {
                    LOGGER.error(
                        "Unable to find fillbar {}",
                        ("slot-" + slot.getName() + "-fillbar")
                    );
                } else {
                    slot_fillbars.put(slot, b);
                }
            });
    }

    private void injectChildren() {
        //Get the speed layout
        speed_layout = parent.childById(FlowLayout.class, "speedLayout");
        if (speed_layout == null) return;
        speed_layout.clearChildren();

        //Recreate the children
        speed_prefix =
            new CustomLabelComponent(Text.of("Speed:")).id("speed-prefix").scale(2.0f);
        speed_prefix.margins(Insets.right(2));

        speed_value = new CustomLabelComponent(Text.of("0.0"));
        speed_value.scale = 2.0f;
        speed_value.id("speed-value");

        //Slot value labels
        EnumSet
            .allOf(EquipmentSlot.class)
            .forEach(slot ->
                slot_values.put(
                    slot,
                    new CustomLabelComponent(Text.of(""))
                        .id("slot-" + slot.getName() + "-label")
                        .scale(slotLabelScale)
                )
            );

        //Inject children
        speed_layout.child(speed_prefix);
        speed_layout.child(speed_value);

        slot_values.forEach((k, v) -> {
            FlowLayout slot = parent.childById(
                FlowLayout.class,
                "slot-" + k.getName() + "-root"
            );
            //Find the layout
            if (slot == null) {
                LOGGER.error(
                    "Flow Layout {} not found! Unable to inject child {}",
                    "slot-" + k.getName() + "-root",
                    v.id()
                );
                return;
            }
            //Make sure the child doesn't exist
            //I am not sure how reliable the remove by itself is
            //How does it find it? I am unable to find the equals implementation
            //Will do the most generic way possible for now
            var c = slot.childById(Component.class, v.id());
            if (c != null) c.remove();
            slot.child(v);
        });
    }

    public void updateUI() {
        LOGGER.trace("GlassUI::updateUI parent {}", parent.id());
        if (!buildUIReferences()) LOGGER.error(
            "GlassUI::updateUI() unable to buildUI references"
        );

        updateSpeedUI();
    }

    private void updateSpeedUI() {
        speed_value.text(viewData.getSpeedText());
    }

    private void updateSlotsUI() {}

    private boolean buildUIReferences() {
        LOGGER.trace("GlassUI::buildUIReferences");
        if (parent == null) return false;
        if (speed_value == null || slot_values.size() != 6) injectChildren();
        if (slot_fillbars.size() != 6) findSlotChildren();

        if (
            speed_value != null && slot_values.size() == 6 && slot_fillbars.size() == 6
        ) return true;

        return false;
    }
}
