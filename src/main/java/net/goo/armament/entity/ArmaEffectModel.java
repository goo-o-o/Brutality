package net.goo.armament.entity;

public class ArmaEffectModel {
    public static ArmaEffectModel FLAT = new ArmaEffectModel("custom_effect_flat");
    public static ArmaEffectModel FLAT_INVERTED = new ArmaEffectModel("custom_effect_flat_inverted");
    public static ArmaEffectModel FLAT_VERTICAL_FRONTFACE = new ArmaEffectModel("custom_effect_flat_vertical");
    public static ArmaEffectModel FLAT_VERTICAL_SIDEFACE = new ArmaEffectModel("custom_effect_flat_vertical_side");
    public static ArmaEffectModel SIX_WAY_CROSS = new ArmaEffectModel("custom_effect_cross");
    public static ArmaEffectModel WALL = new ArmaEffectModel("custom_effect_wall");
    public static ArmaEffectModel WALL_CROSS = new ArmaEffectModel("custom_effect_wall_cross");

    final String modelName;

    public ArmaEffectModel(String model) {
        this.modelName = model;
    }

    public String getModelName() {
        return modelName;
    }
}