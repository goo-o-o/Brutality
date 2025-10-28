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
    public static final ForgeConfigSpec.BooleanValue THROWING_WEAPONS_BREAK_BLOCKS;
    public static final ForgeConfigSpec.BooleanValue SEVENTH_STAR_GRIEFING;
    public static final ForgeConfigSpec.DoubleValue ATOMIC_JUDGEMENT_MULT;
    public static final ForgeConfigSpec.IntValue BEACHBALL_LIFESPAN;

    public static final ForgeConfigSpec.DoubleValue BLACK_HOLE_PULL_STRENGTH;
    public static final ForgeConfigSpec.DoubleValue DEATHSAW_WALL_CLIMB_SPEED;
    public static final ForgeConfigSpec.IntValue BLACK_HOLE_TICK_DAMAGE;
    public static final ForgeConfigSpec.IntValue BIOMECH_REACTOR_DAMAGE;
    public static final ForgeConfigSpec.IntValue DEATHSAW_TICK_DAMAGE;
    public static final ForgeConfigSpec.IntValue LAST_PRISM_TICK_DAMAGE;
    public static final ForgeConfigSpec.IntValue CREASE_OF_CREATION_THRESHOLD;
    public static final ForgeConfigSpec.IntValue GUNGNIR_HIT_QUOTA;

    static {
        BUILDER.push("Brutality Common Config");

        SEVENTH_STAR_GRIEFING = BUILDER
                .comment("Should Seventh Star Projectiles break blocks (Default = False)")
                .define("seventhStarShouldBreakBlocks", false);

        DEATHSAW_WALL_CLIMB_SPEED = BUILDER
                .comment("Deathsaw Wall Climb Speed Multiplier (Default = 0.8)")
                .defineInRange("deathsawWallClimbSpeed", 0.8, 0, 100);

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

        BIOMECH_REACTOR_DAMAGE = BUILDER
                .comment("Biomech Reactor Damage (Default = 30)")
                .defineInRange("biomechReactorDamage", 30, 0, 1000);

        LAST_PRISM_TICK_DAMAGE = BUILDER
                .comment("Last Prism Tick Damage (Default = 2)")
                .defineInRange("lastPrismTickDamage", 2, 0, 100);

        DEATHSAW_TICK_DAMAGE = BUILDER
                .comment("Deathsaw Tick Damage (Default = 1)")
                .defineInRange("deathsawTickDamage", 1, 0, 100);

        GUNGNIR_HIT_QUOTA = BUILDER
                .comment("How many times Gungnir will hit before returning (Default = 3)")
                .defineInRange("gungnirHitQuota", 3, 0, 10);

        BEACHBALL_LIFESPAN = BUILDER
                .comment("Beachball Lifespan in Ticks")
                .defineInRange("beachBallLifeSpan", 1200, 20, 20000);

        CREASE_OF_CREATION_THRESHOLD = BUILDER
                .comment("Entities above this Health Point threshold will be unaffected by Crease of Creation")
                .defineInRange("creaseOfCreationThreshold", 200, 1, 20000);

        THROWING_WEAPONS_BREAK_BLOCKS = BUILDER
                .comment("Should Throwing Weapons break blocks, this includes things such as On Hit Block effects or Explosions breaking blocks")
                .define("throwingWeaponsShouldBreakBlocks", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
