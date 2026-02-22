package net.goo.brutality.client.renderers.textures;

import net.goo.brutality.client.renderers.BrutalityRenderTypes;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;

public class BrutalityAutoFullbrightTexture extends AutoGlowingTexture {


    public BrutalityAutoFullbrightTexture(ResourceLocation originalLocation, ResourceLocation location) {
        super(originalLocation, location);
    }

    public static RenderType getRenderType(ResourceLocation texture) {
//        return RenderType.entityTranslucent(texture);
        return Util.memoize(BrutalityRenderTypes::getfullBright).apply(getEmissiveResource(texture));
    }

    private static ResourceLocation getEmissiveResource(ResourceLocation baseResource) {
        // 1. Check if the resource is valid for path manipulation
        // 'missingno' is internal and has no file extension, causing appendToPath to fail.
        String pathStr = baseResource.getPath();
        if (pathStr.equals("missingno") || !pathStr.contains(".")) {
            return baseResource; // Return the original without attempting to find a fullbright version
        }

        // 2. Safely generate the fullbright path
        ResourceLocation path = appendToPath(baseResource, "_fb");

        // 3. Register the texture only if it's not already handled
        generateTexture(path, (textureManager) ->
                textureManager.register(path, new BrutalityAutoFullbrightTexture(baseResource, path))
        );

        return path;
    }
}


