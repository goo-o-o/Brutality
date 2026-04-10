package net.goo.brutality.client.renderers.shaders.outline;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.shaders.PostShaderInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PixelateShader extends PostShaderInstance {
    
    // Singleton instance
    public static final PixelateShader INSTANCE = new PixelateShader();
    
    @Override
    public ResourceLocation getShaderLocation() {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "shaders/post/pixelate_post.json");
    }



    @Override
    public void setUniforms(PostPass instance) {
        super.setUniforms(instance);

        instance.getEffect().setSampler("PixelateSampler", getSilhouetteTarget()::getColorTextureId);

        instance.getEffect().safeGetUniform("OutSize").set(
                (float) Minecraft.getInstance().getMainRenderTarget().width,
                (float) Minecraft.getInstance().getMainRenderTarget().height
        );
        
        // Configurable pixel size (how blocky the censoring is)
        instance.getEffect().safeGetUniform("PixelSize").set(
                8F // Default: 8.0
        );
        
        // Configurable spread (how much it bleeds out)
        instance.getEffect().safeGetUniform("Spread").set(
                3F // Default: 3.0
        );
    }

}