package net.goo.brutality.mixin.accessors;

public class BrutalityFontHooks {
    private static final ThreadLocal<String> ACTIVE_RARITY = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<Float> DIM_FACTOR = ThreadLocal.withInitial(() -> 1.0f);

    public static void setActiveRarity(String rarity) { ACTIVE_RARITY.set(rarity); }
    public static String getActiveRarity() { return ACTIVE_RARITY.get(); }

    public static void setDimFactor(float dim) { DIM_FACTOR.set(dim); }
    public static float getDimFactor() { return DIM_FACTOR.get(); }
}
