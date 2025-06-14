//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.goo.brutality.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
@OnlyIn(Dist.CLIENT)

public class BrutalityClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue BLACK_HOLE_SKY_COLOR;

    static {
        BUILDER.push("Brutality Client Config");

        BLACK_HOLE_SKY_COLOR = BUILDER.comment("Should the Black Hole change the color of the Sky and Fog").define("shouldBlackHoleChangeSkyColor", true);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
