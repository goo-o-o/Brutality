package net.goo.brutality.common.mixin_helpers;

public class BrutalityFontHooks {
    private static final ThreadLocal<String> ACTIVE_COLOR_DATA = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<Float> DIM_FACTOR = ThreadLocal.withInitial(() -> 1.0f);

    public static void setActiveColorData(String rarity) { ACTIVE_COLOR_DATA.set(rarity); }
    public static String getActiveColorData() { return ACTIVE_COLOR_DATA.get(); }

    public static void setDimFactor(float dim) { DIM_FACTOR.set(dim); }
    public static float getDimFactor() { return DIM_FACTOR.get(); }
}
