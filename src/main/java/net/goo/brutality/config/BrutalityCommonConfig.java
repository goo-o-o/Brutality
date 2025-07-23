//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.goo.brutality.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BrutalityCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ATOMIC_JUDGEMENT_GRIEFING;
    public static final ForgeConfigSpec.BooleanValue SEVENTH_STAR_GRIEFING;
    public static final ForgeConfigSpec.DoubleValue ATOMIC_JUDGEMENT_MULT;

    public static final ForgeConfigSpec.DoubleValue BLACK_HOLE_PULL_STRENGTH;
    public static final ForgeConfigSpec.IntValue BLACK_HOLE_TICK_DAMAGE;
    public static final ForgeConfigSpec.IntValue GUNGNIR_HIT_QUOTA;

    static {
        BUILDER.push("Brutality Common Config");

        SEVENTH_STAR_GRIEFING = BUILDER
                .comment("Should Seventh Star Projectiles break blocks (Default = False)")
                .define("seventhStarShouldBreakBlocks", false);

        ATOMIC_JUDGEMENT_GRIEFING = BUILDER
                .comment("Should Atomic Judgement break blocks (Default = False)")
                .define("atomicJudgementShouldBreakBlocks", false);

        ATOMIC_JUDGEMENT_MULT = BUILDER
                .comment("Atomic Judgement explosion multiplier (Default = 1.0)")
                .defineInRange("atomicJudgementExplosionMultiplier", 1D, 0, 10);

        BLACK_HOLE_PULL_STRENGTH = BUILDER
                .comment("Pull Strength of the Black Hole (Default = 1.0)")
                .defineInRange("blackHolePullStrength", 1.0D, 0.1D, 10.0D);

        BLACK_HOLE_TICK_DAMAGE = BUILDER
                .comment("Black Hole Tick Damage (Default = 2)")
                .defineInRange("blackHoleTickDamage", 2, 0, 10);

        GUNGNIR_HIT_QUOTA = BUILDER
                .comment("How many times Gungnir will hit before returning (Default = 3)")
                .defineInRange("gungnirHitQuota", 3, 0, 10);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
