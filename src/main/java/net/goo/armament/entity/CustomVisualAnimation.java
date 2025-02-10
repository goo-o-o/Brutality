package net.goo.armament.entity;

public class CustomVisualAnimation {
    public static CustomVisualAnimation SPIN = new CustomVisualAnimation("animation.cs_effect.spin", 15);
    public static CustomVisualAnimation ASCEND = new CustomVisualAnimation("animation.cs_effect.ascend", 20);
    public static CustomVisualAnimation SLOW_ROTATION = new CustomVisualAnimation("animation.cs_effect.slow_rotation", Integer.MAX_VALUE);
    public static CustomVisualAnimation SWEEP_RTOL = new CustomVisualAnimation("animation.cs_effect.sweep_rtol", 15);
    public static CustomVisualAnimation SWEEP_LTOR = new CustomVisualAnimation("animation.cs_effect.sweep_ltor", 15);
    public static CustomVisualAnimation STRETCH = new CustomVisualAnimation("animation.cs_effect.stretch", 15);
    public static CustomVisualAnimation GOO = new CustomVisualAnimation("animation.cs_effect.goo", Integer.MAX_VALUE);
    public static CustomVisualAnimation SPIN_EXPAND = new CustomVisualAnimation("animation.cs_effect.spin_expand", 15);

    private final String animName;
    private final int lifespan;

    public CustomVisualAnimation(String animation, int lifespan) {
        this.animName = animation;
        this.lifespan = lifespan;
    }

    public String getAnimName() {
        return animName;
    }

    public int getLifespan() {
        return lifespan;
    }

    public static CustomVisualAnimation noAnimWithLifespan(int duration) {
        return new CustomVisualAnimation("animation.cs_effect.none", duration);
    }
}