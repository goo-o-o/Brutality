package net.goo.armament.entity.helper;

public class ArmaVisualModel {
    public static ArmaVisualModel FLAT = new ArmaVisualModel("cs_effect_flat");
    public static ArmaVisualModel FLAT_INVERTED = new ArmaVisualModel("cs_effect_flat_inverted");
    public static ArmaVisualModel FLAT_VERTICAL_FRONTFACE = new ArmaVisualModel("cs_effect_flat_vertical");
    public static ArmaVisualModel FLAT_VERTICAL_SIDEFACE = new ArmaVisualModel("cs_effect_flat_vertical_side");
    public static ArmaVisualModel SIX_WAY_CROSS = new ArmaVisualModel("cs_effect_cross");
    public static ArmaVisualModel WALL = new ArmaVisualModel("cs_effect_wall");
    public static ArmaVisualModel WALL_CROSS = new ArmaVisualModel("cs_effect_wall_cross");
    public static ArmaVisualModel TERRA_BEAM = new ArmaVisualModel("terra_beam");

    final String modelName;

    public ArmaVisualModel(String model) {
        this.modelName = model;
    }

    public String getModelName() {
        return modelName;
    }
}