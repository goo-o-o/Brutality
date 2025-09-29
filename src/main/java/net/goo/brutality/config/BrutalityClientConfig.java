//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.goo.brutality.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber (value = Dist.CLIENT)
public class BrutalityClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue BLACK_HOLE_SKY_COLOR;
    public static final ForgeConfigSpec.BooleanValue BORK_SKY_COLOR;
    public static final ForgeConfigSpec.BooleanValue THROWING_ANIMATION_SHOW_ARMS;
    public static final ForgeConfigSpec.BooleanValue THROWING_ANIMATION_SHOW_ITEMS;

    static {
        BUILDER.push("Brutality Client Config");

        BLACK_HOLE_SKY_COLOR = BUILDER.comment("Should the Black Hole change the color of the Sky and Fog").define("shouldBlackHoleChangeSkyColor", true);
        BORK_SKY_COLOR = BUILDER.comment("Should the Blade of the Ruined King change the color of the environment").define("shouldBORKChangeEnvironmentColor", true);
        THROWING_ANIMATION_SHOW_ARMS = BUILDER.comment("Should Throwing animations show Arms in First Person?").define("showFirstPersonArmThrowingAnimation", true);
        THROWING_ANIMATION_SHOW_ITEMS = BUILDER.comment("Should Throwing animations show Items in First Person?").define("showFirstPersonItemThrowingAnimation", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
