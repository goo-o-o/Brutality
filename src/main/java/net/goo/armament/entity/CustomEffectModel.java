package net.goo.armament.entity;

public class CustomEffectModel {
    public static CustomEffectModel FLAT = new CustomEffectModel("custom_effect_flat");
    public static CustomEffectModel FLAT_INVERTED = new CustomEffectModel("custom_effect_flat_inverted");
    public static CustomEffectModel FLAT_VERTICAL_FRONTFACE = new CustomEffectModel("custom_effect_flat_vertical");
    public static CustomEffectModel FLAT_VERTICAL_SIDEFACE = new CustomEffectModel("custom_effect_flat_vertical_side");
    public static CustomEffectModel SIX_WAY_CROSS = new CustomEffectModel("custom_effect_cross");
    public static CustomEffectModel WALL = new CustomEffectModel("custom_effect_wall");
    public static CustomEffectModel WALL_CROSS = new CustomEffectModel("custom_effect_wall_cross");

    final String modelName;

    public CustomEffectModel(String model) {
        this.modelName = model;
    }

    public String getModelName() {
        return modelName;
    }
}