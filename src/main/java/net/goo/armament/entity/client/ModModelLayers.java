package net.goo.armament.entity.client;

import net.goo.armament.Armament;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation SUPERNOVA_EXPLOSION_LAYER = new ModelLayerLocation(
            new ResourceLocation(Armament.MOD_ID, "supernova_explosion_layer"), "main");
    public static final ModelLayerLocation TEST_TRIDENT_LAYER = new ModelLayerLocation(
            new ResourceLocation(Armament.MOD_ID, "test_trident_layer"), "main");
}
