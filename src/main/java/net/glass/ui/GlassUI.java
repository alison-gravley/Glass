package net.glass.ui;

import static net.glass.GlassHud.DATA;
import static net.glass.GlassHud.LOGGER;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.component.SpriteComponent;
import io.wispforest.owo.ui.component.TextureComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import io.wispforest.owo.ui.core.ParentComponent;
import io.wispforest.owo.ui.core.PositionedRectangle;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.hud.Hud;
import io.wispforest.owo.ui.parsing.UIModel;
import java.util.function.Consumer;
import net.glass.data.GlassData;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.texture.Sprite;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class GlassUI extends BaseUIModelScreen<FlowLayout> {

    private FlowLayout myRoot;

    public GlassUI() {
        super(FlowLayout.class, DataSource.asset(Identifier.of("glasshud", "armor")));
        myRoot = null;
    }

    private LabelComponent speedLabelComponent = null;
    private LabelComponent headLabelComponent;
    private LabelComponent chestLabelComponent;
    private LabelComponent legsLabelComponent;
    private LabelComponent feetLabelComponent;
    private LabelComponent mainhandLabelComponent;
    private LabelComponent offhandLabelComponent;

    private SpriteComponent headSpriteComponent;
    private SpriteComponent chestSpriteComponent;
    private SpriteComponent legsSpriteComponent;
    private SpriteComponent feetSpriteComponent;
    private SpriteComponent mainhandSpriteComponent;
    private SpriteComponent offhandSpriteComponent;

    @Override
    public void build(FlowLayout rootComponent) {
        if (myRoot == null) {
            myRoot = rootComponent;
        }
        LOGGER.info("GlassUI::build(FlowLayout root) {}", rootComponent.id());
        build();
    }

    public void build() {
        if (myRoot == null) return;
        LOGGER.info("GlassUI::build() {}", myRoot.id());
        if (speedLabelComponent == null) {
            speedLabelComponent = myRoot.childById(LabelComponent.class, "speed-label");
        }
        speedLabelComponent.text(DATA.getSpeed());
    }

    public Component getRoot() {
        if (myRoot == null) {
            myRoot =
                this.model.createAdapterWithoutScreen(
                        0,
                        0,
                        width,
                        height,
                        FlowLayout.class
                    )
                    .rootComponent;
        }
        return myRoot;
    }
}
