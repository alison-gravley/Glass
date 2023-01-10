package net.fabricmc.example;

import static net.fabricmc.example.ExampleMod.LOGGER;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import java.text.DecimalFormat;
import java.util.*;
import java.util.EnumSet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

public class GlassScreen {

  private static GlassScreen glassHud;

  public enum ShowFlag {
    SPEED,
    ARMOR,
    ARMOR_VALUE,
    TOOLS,
    TOOL_VALUE,
  }

  public static final EnumSet<ShowFlag> ALL_FLAGS = EnumSet.allOf(
    ShowFlag.class
  );
  private String currentSpeed;
  private String currentChestStatus;
  private String currentLegsStatus;
  private String currentFeetStatus;
  private String currentHeadStatus;
  private LabelComponent speedLabelComponent;
  private LabelComponent feetLabelComponent;
  private LabelComponent legsLabelComponent;
  private LabelComponent chestLabelComponent;
  private LabelComponent headLabelComponent;
  protected final List<Component> children = new ArrayList<>();
  protected final List<Component> childrenView = Collections.unmodifiableList(
    this.children
  );

  private ClientPlayerEntity thePlayer = MinecraftClient.getInstance().player;
  private ClientWorld theWorld = MinecraftClient.getInstance().world;

  private long lastTimeChecked = 0;
  private Vec3d lastPosition = new Vec3d(0, 0, 0);

  public static GlassScreen getInstance() {
    if (glassHud == null) {
      LOGGER.info("getInstance() - Glass Screen was null");
      glassHud = new GlassScreen();
    }
    // LOGGER.info("getInstance() - Return");
    return glassHud;
  }

  public List<Component> getComponents() {
    return this.childrenView;
  }

  public GlassScreen() {
    initializeComponents();
  }

  private void updateSpeed() {
    double distanceTravelled = 0;
    double timePassedMS = 0;
    double approxSpeed = 0;
    double tickTime = 0;
    DecimalFormat df = new DecimalFormat("#,####.##");
    if (lastPosition == null) {
      lastPosition = thePlayer.getEyePos();
    }
    if (lastTimeChecked == 0) {
      lastTimeChecked = theWorld.getTime();
    }

    if (theWorld.getTime() > lastTimeChecked + 5) {
      distanceTravelled = lastPosition.distanceTo(thePlayer.getEyePos());
      timePassedMS = 50 * (theWorld.getTime() - lastTimeChecked);

      approxSpeed = (distanceTravelled / timePassedMS) * (1000);

      lastTimeChecked = theWorld.getTime();
      lastPosition = thePlayer.getEyePos();
      currentSpeed =
        "Velocity: " +
        df.format(thePlayer.getVelocity().length()) +
        "\nSpeed: " +
        df.format(approxSpeed) +
        " m/s";
    }

    speedLabelComponent.text(Text.empty().append(currentSpeed));
  }

  private void updateArmor() {
    currentChestStatus =
      updateArmorText(
        thePlayer.getEquippedStack(EquipmentSlot.CHEST),
        EquipmentSlot.CHEST.toString()
      );
    currentLegsStatus =
      updateArmorText(
        thePlayer.getEquippedStack(EquipmentSlot.LEGS),
        EquipmentSlot.LEGS.toString()
      );
    currentFeetStatus =
      updateArmorText(
        thePlayer.getEquippedStack(EquipmentSlot.FEET),
        EquipmentSlot.FEET.toString()
      );
    currentHeadStatus =
      updateArmorText(
        thePlayer.getEquippedStack(EquipmentSlot.HEAD),
        EquipmentSlot.HEAD.toString()
      );

    chestLabelComponent.text(Text.of(currentChestStatus));
    feetLabelComponent.text(Text.of(currentFeetStatus));
    legsLabelComponent.text(Text.of(currentLegsStatus));
    headLabelComponent.text(Text.of(currentHeadStatus));
  }

  private String updateArmorText(ItemStack armorItem, String name) {
    double remainingDamage = 0;
    if (armorItem.isEmpty()) {
      return name + ": EMPTY";
    } else {
      remainingDamage = armorItem.getMaxDamage() - armorItem.getDamage();
      return (
        name + ": " + (remainingDamage / armorItem.getMaxDamage()) * 100 + "%"
      );
    }
  }

  public void initializeComponents() {
    children.clear();

    currentSpeed = "EMPTY";
    currentChestStatus = "EMPTY";
    currentFeetStatus = "EMPTY";
    currentLegsStatus = "EMPTY";
    currentHeadStatus = "EMPTY";

    speedLabelComponent = Components.label(initialText());
    feetLabelComponent = Components.label(initialText());
    legsLabelComponent = Components.label(initialText());
    chestLabelComponent = Components.label(initialText());
    headLabelComponent = Components.label(initialText());

    updateComponents();
    if (ALL_FLAGS.contains(ShowFlag.SPEED)) {
      initializePosition(speedLabelComponent);
      children.add(speedLabelComponent);
    }

    if (ALL_FLAGS.contains(ShowFlag.ARMOR)) {
      children.add(headLabelComponent);
      children.add(chestLabelComponent);
      children.add(legsLabelComponent);
      children.add(feetLabelComponent);
    }
  }

  private Text initialText() {
    return Text
      .empty()
      .append("emptyText")
      .formatted(Formatting.WHITE, Formatting.BOLD);
  }

  private void initializePosition(LabelComponent l) {
    l.horizontalTextAlignment(HorizontalAlignment.LEFT);
  }

  public void updateComponents() {
    if (thePlayer != null) {
      updateArmor();
      updateSpeed();
    }
  }
}
