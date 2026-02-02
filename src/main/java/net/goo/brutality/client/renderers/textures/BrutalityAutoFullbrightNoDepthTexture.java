package net.goo.brutality.client.renderers.textures;

import net.goo.brutality.client.renderers.BrutalityRenderTypes;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;

public class BrutalityAutoFullbrightNoDepthTexture extends AutoGlowingTexture {

    public BrutalityAutoFullbrightNoDepthTexture(ResourceLocation originalLocation, ResourceLocation location) {
        super(originalLocation, location);
    }

    public static RenderType getRenderType(ResourceLocation texture) {
//        return RenderType.entityCutout(getEmissiveResource(texture));
        return Util.memoize(BrutalityRenderTypes::getfullBrightNoDepth).apply(getEmissiveResource(texture));
    }

    private static ResourceLocation getEmissiveResource(ResourceLocation baseResource) {
        ResourceLocation path = appendToPath(baseResource, "_fbnd");
        generateTexture(path, (textureManager) -> {
            textureManager.register(path, new AutoGlowingTexture(baseResource, path));
        });
        return path;
    }
}


