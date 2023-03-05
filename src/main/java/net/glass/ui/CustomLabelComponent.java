package net.glass.ui;

import io.wispforest.owo.ui.base.BaseComponent;
import io.wispforest.owo.ui.core.AnimatableProperty;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.VerticalAlignment;
import io.wispforest.owo.ui.parsing.UIModel;
import io.wispforest.owo.ui.parsing.UIParsing;
import io.wispforest.owo.ui.util.Drawer;
import java.util.Map;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents.Custom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.w3c.dom.Element;

public class CustomLabelComponent extends BaseComponent {

    protected final TextRenderer textRenderer = MinecraftClient.getInstance()
        .textRenderer;

    protected Text text;

    protected VerticalAlignment verticalTextAlignment = VerticalAlignment.TOP;
    protected HorizontalAlignment horizontalTextAlignment = HorizontalAlignment.LEFT;

    protected final AnimatableProperty<Color> color = AnimatableProperty.of(Color.WHITE);
    protected boolean shadow;
    protected float scale = 1.0f;

    public CustomLabelComponent(Text text) {
        this.text = text;
        this.shadow = false;
    }

    public CustomLabelComponent text(Text text) {
        this.text = text;
        this.notifyParentIfMounted();
        return this;
    }

    public Text text() {
        return this.text;
    }

    public CustomLabelComponent shadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public boolean shadow() {
        return this.shadow;
    }

    @Override
    public CustomLabelComponent id(String id) {
        this.id = id;
        return this;
    }

    public CustomLabelComponent verticalTextAlignment(
        VerticalAlignment verticalAlignment
    ) {
        this.verticalTextAlignment = verticalAlignment;
        return this;
    }

    public VerticalAlignment verticalTextAlignment() {
        return this.verticalTextAlignment;
    }

    public CustomLabelComponent horizontalTextAlignment(
        HorizontalAlignment horizontalAlignment
    ) {
        this.horizontalTextAlignment = horizontalAlignment;
        return this;
    }

    public HorizontalAlignment horizontalTextAlignment() {
        return this.horizontalTextAlignment;
    }

    @Override
    protected int determineHorizontalContentSize(Sizing sizing) {
        float tWidth = this.textRenderer.getWidth(text);
        tWidth *= scale;
        return ((int) Math.ceil(tWidth));
    }

    @Override
    protected int determineVerticalContentSize(Sizing sizing) {
        float tHeight = this.textRenderer.fontHeight;
        tHeight *= scale;
        return ((int) Math.ceil(tHeight));
    }

    public CustomLabelComponent color(Color color) {
        this.color.set(color);
        return this;
    }

    public AnimatableProperty<Color> color() {
        return this.color;
    }

    public float scale() {
        return scale;
    }

    public CustomLabelComponent scale(float scale) {
        this.scale = scale;
        this.notifyParentIfMounted();
        return this;
    }

    @Override
    public void draw(
        MatrixStack matrices,
        int mouseX,
        int mouseY,
        float partialTicks,
        float delta
    ) {
        matrices.push();
        matrices.scale(scale, scale, 1);

        if (this.horizontalSizing.get().isContent()) {
            x += this.horizontalSizing.get().value;
        }
        if (this.verticalSizing.get().isContent()) {
            y += this.verticalSizing.get().value;
        }

        switch (this.verticalTextAlignment) {
            case CENTER -> y +=
                (this.height - (this.textRenderer.fontHeight * scale)) / 2.0;
            case BOTTOM -> y += (this.height - (this.textRenderer.fontHeight * scale));
            //case TOP -> y -= textRenderer.fontHeight * scale;
        }

        switch (this.horizontalTextAlignment) {
            //case LEFT ->
            case RIGHT -> x += (this.width - (this.textRenderer.getWidth(text) * scale));
            case CENTER -> x +=
                (this.width - (this.textRenderer.getWidth(text) * scale)) / 2.0;
        }

        if (this.shadow) textRenderer.drawWithShadow(
            matrices,
            text,
            x * (1 / scale),
            y * (1 / scale),
            this.color.get().argb()
        ); else textRenderer.draw(
            matrices,
            text,
            x * (1 / scale),
            y * (1 / scale),
            this.color.get().argb()
        );
        matrices.pop();
    }

    @Override
    public void parseProperties(
        UIModel model,
        Element element,
        Map<String, Element> children
    ) {
        super.parseProperties(model, element, children);
        UIParsing.apply(children, "text", UIParsing::parseText, this::text);
        UIParsing.apply(children, "scale", UIParsing::parseFloat, this::scale);
        UIParsing.apply(children, "color", Color::parse, this::color);
        UIParsing.apply(children, "shadow", UIParsing::parseBool, this::shadow);

        UIParsing.apply(
            children,
            "vertical-text-alignment",
            VerticalAlignment::parse,
            this::verticalTextAlignment
        );
        UIParsing.apply(
            children,
            "horizontal-text-alignment",
            HorizontalAlignment::parse,
            this::horizontalTextAlignment
        );
    }
}
