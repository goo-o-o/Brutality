package net.goo.armament.entity.helper;

public class ArmaVisualAnimation {
    public static ArmaVisualAnimation SPIN = new ArmaVisualAnimation("animation.cs_effect.spin", 15);
    public static ArmaVisualAnimation ASCEND = new ArmaVisualAnimation("animation.cs_effect.ascend", 20);
    public static ArmaVisualAnimation SLOW_ROTATION = new ArmaVisualAnimation("animation.cs_effect.slow_rotation", Integer.MAX_VALUE);
    public static ArmaVisualAnimation SWEEP_RTOL = new ArmaVisualAnimation("animation.cs_effect.sweep_rtol", 15);
    public static ArmaVisualAnimation SWEEP_LTOR = new ArmaVisualAnimation("animation.cs_effect.sweep_ltor", 15);
    public static ArmaVisualAnimation STRETCH = new ArmaVisualAnimation("animation.cs_effect.stretch", 15);
    public static ArmaVisualAnimation GOO = new ArmaVisualAnimation("animation.cs_effect.goo", Integer.MAX_VALUE);
    public static ArmaVisualAnimation SPIN_EXPAND = new ArmaVisualAnimation("animation.cs_effect.spin_expand", 15);

    private final String animName;
    private final int lifespan;

    public ArmaVisualAnimation(String animation, int lifespan) {
        this.animName = animation;
        this.lifespan = lifespan;
    }

    public String getAnimName() {
        return animName;
    }

    public int getLifespan() {
        return lifespan;
    }

    public static ArmaVisualAnimation noAnimWithLifespan(int duration) {
        return new ArmaVisualAnimation("animation.arma_effect.none", duration);
    }
}