//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.goo.brutality.config;

import net.goo.brutality.gui.CooldownMeter;
import net.goo.brutality.gui.ManaMeter;
import net.goo.brutality.gui.RageMeter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class BrutalityClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue BLACK_HOLE_SKY_COLOR;
    public static final ForgeConfigSpec.BooleanValue BORK_SKY_COLOR;
    public static final ForgeConfigSpec.BooleanValue THROWING_ANIMATION_SHOW_ARMS;
    public static final ForgeConfigSpec.BooleanValue THROWING_ANIMATION_SHOW_ITEMS;
    public static final ForgeConfigSpec.DoubleValue MANA_BAR_X, MANA_BAR_Y;
    public static final ForgeConfigSpec.EnumValue<RageMeter.Style> RAGE_METER_STYLE;
    public static final ForgeConfigSpec.EnumValue<ManaMeter.Style> MANA_METER_STYLE;
    public static final ForgeConfigSpec.EnumValue<RageMeter.Position> RAGE_METER_POSITION;
    public static final ForgeConfigSpec.EnumValue<ManaMeter.Position> MANA_METER_POSITION;
    public static final ForgeConfigSpec.EnumValue<CooldownMeter.AbilityCooldownPosition> ABILITY_COOLDOWN_METER_POSITION;
    public static final ForgeConfigSpec.EnumValue<CooldownMeter.ArmorSetCooldownPosition> ARMOR_SET_COOLDOWN_POSITION;
    public static final ForgeConfigSpec.ConfigValue<String> RAGE_METER_FIRE_OUTER;
    public static final ForgeConfigSpec.ConfigValue<String> RAGE_METER_FIRE_INNER;
    public static final ForgeConfigSpec.IntValue RAGE_METER_X_OFFSET;
    public static final ForgeConfigSpec.IntValue RAGE_METER_Y_OFFSET;
    public static final ForgeConfigSpec.IntValue MANA_METER_X_OFFSET;
    public static final ForgeConfigSpec.IntValue MANA_METER_Y_OFFSET;
    public static final ForgeConfigSpec.DoubleValue RAGE_METER_FIRE_INTENSITY;
    public static final ForgeConfigSpec.IntValue RAGE_METER_SHAKE_INTENSITY;

    static {
        BUILDER.push("Brutality Client Config");

        RAGE_METER_STYLE = BUILDER
                .comment("Rage meter HUD style")
                .defineEnum("rage_meter_style", RageMeter.Style.CLASSIC);

        RAGE_METER_POSITION = BUILDER
                .comment("Rage meter position")
                .defineEnum("rage_meter_position", RageMeter.Position.HOTBAR_RIGHT);

        MANA_METER_STYLE = BUILDER
                .comment("Mana meter HUD style")
                .defineEnum("mana_meter_style", ManaMeter.Style.ORB);

        MANA_METER_POSITION = BUILDER
                .comment("Mana meter position")
                .defineEnum("mana_meter_position", ManaMeter.Position.HOTBAR_RIGHT);

        ABILITY_COOLDOWN_METER_POSITION = BUILDER
                .comment("Ability cooldown meter position")
                .defineEnum("ability_cooldown_meter_position", CooldownMeter.AbilityCooldownPosition.RIGHT);

        ARMOR_SET_COOLDOWN_POSITION = BUILDER
                .comment("Armor set cooldown meter position")
                .defineEnum("armor_set_cooldown_meter_position", CooldownMeter.ArmorSetCooldownPosition.RIGHT);

        RAGE_METER_X_OFFSET = BUILDER.comment("Rage Meter X Offset").defineInRange("rage_meter_x_offset", 0, -1000, 1000);
        RAGE_METER_Y_OFFSET = BUILDER.comment("Rage Meter Y Offset").defineInRange("rage_meter_y_offset", 0, -1000, 1000);
        MANA_METER_X_OFFSET = BUILDER.comment("Mana Meter X Offset").defineInRange("mana_meter_x_offset", 0, -1000, 1000);
        MANA_METER_Y_OFFSET = BUILDER.comment("Mana Meter Y Offset").defineInRange("mana_meter_y_offset", 0, -1000, 1000);
        RAGE_METER_SHAKE_INTENSITY = BUILDER.comment("Rage Meter Shake Intensity").defineInRange("rage_meter_shake_intensity", 10, 0, 100);


        RAGE_METER_FIRE_OUTER = BUILDER
                .comment("Rage meter fire outer color (hex format, e.g., '#FFFF00' for yellow)")
                .define("rage_meter_fire_outer_color", "#FFFF00",
                        input -> {
                            if (!(input instanceof String str)) return false;
                            try {
                                Color.decode(str);
                                return true;
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        });

        RAGE_METER_FIRE_INNER = BUILDER
                .comment("Rage meter fire inner color (hex format, e.g., '#FF4B00' for orange-red)")
                .define("rage_meter_fire_inner_color", "#FF4B00",
                        input -> {
                            if (!(input instanceof String str)) return false;
                            try {
                                Color.decode(str);
                                return true;
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        });



        RAGE_METER_FIRE_INTENSITY = BUILDER.comment("Rage meter fire intensity multiplier (0 to disable)")
                .defineInRange("rage_meter_fire_intensity", 2.5, 0, 5);


        BLACK_HOLE_SKY_COLOR = BUILDER.comment("Should the Black Hole change the color of the Sky and Fog").define("shouldBlackHoleChangeSkyColor", true);
        BORK_SKY_COLOR = BUILDER.comment("Should the Blade of the Ruined King change the color of the environment").define("shouldBORKChangeEnvironmentColor", true);
        THROWING_ANIMATION_SHOW_ARMS = BUILDER.comment("Should Throwing animations show Arms in First Person?").define("showFirstPersonArmThrowingAnimation", true);
        THROWING_ANIMATION_SHOW_ITEMS = BUILDER.comment("Should Throwing animations show Items in First Person?").define("showFirstPersonItemThrowingAnimation", true);

        MANA_BAR_X = BUILDER.comment("Mana bar X position (0.0 to 1.0, percentage of screen width)").defineInRange("mana_bar_x", 0.2, 0.0, 1.0);
        MANA_BAR_Y = BUILDER.comment("Mana bar Y position (0.0 to 1.0, percentage of screen height)").defineInRange("mana_bar_y", 0.2, 0.0, 1.0);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }


}
