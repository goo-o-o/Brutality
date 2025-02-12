package net.goo.armament.entity;

public class CustomVisualType {
    private final String name;
    private final String texture;
    private final CustomEffectModel model;
    private final CustomVisualAnimation animation;
    private final int frames;
    private final int framesSpeed;
    private final double scale;
    private final boolean rotateRandomly;
    private final boolean fadeOut;
    private final boolean specialProperties;

    public CustomVisualType(String name, String texture, CustomEffectModel model, CustomVisualAnimation animation, int frames, int framesSpeed, double scale, boolean rotateRandomly, boolean fadeOut, boolean specialProperties) {
        this.name = name;
        this.texture = texture;
        this.model = model;
        this.animation = animation;
        this.frames = frames;
        this.framesSpeed = framesSpeed;
        this.scale = scale;
        this.rotateRandomly = rotateRandomly;
        this.fadeOut = fadeOut;
        this.specialProperties = specialProperties;
    }

    public CustomVisualType(String texture, CustomEffectModel model, CustomVisualAnimation animation, int frames, int framesSpeed, double scale, boolean rotateRandomly, boolean fadeOut, boolean specialProperties) {
        this(texture, texture, model, animation, frames, framesSpeed, scale, rotateRandomly, fadeOut, specialProperties);
    }

    public static CustomVisualType createSkin(String skin, CustomVisualType parent) {
        return new CustomVisualType(parent.name + "_" + skin, "skin/" + parent.texture + "_" + skin, parent.model, parent.animation, parent.frames, parent.framesSpeed, parent.scale, parent.rotateRandomly, parent.fadeOut, parent.specialProperties);
    }

    public String getName() {
        return name;
    }

    public String getTexture() {
        return texture;
    }

    public CustomEffectModel getModel() {
        return model;
    }

    public CustomVisualAnimation getAnimation() {
        return animation;
    }

    public int getFrames() {
        return frames;
    }

    public int getFramesSpeed() {
        return framesSpeed;
    }

    public double getScale() {
        return scale;
    }

    public boolean isRotateRandomly() {
        return rotateRandomly;
    }

    public boolean isFadeOut() {
        return fadeOut;
    }

    public boolean hasSpecialProperties() {
        return specialProperties;
    }
}