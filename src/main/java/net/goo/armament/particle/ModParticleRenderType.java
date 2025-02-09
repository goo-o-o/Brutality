package net.goo.armament.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ModParticleRenderType {

    ParticleRenderType PARTICLE_SHEET_END_PORTAL = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder pBuffer, TextureManager pTextureManager) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(false);
            RenderSystem.setShader(GameRenderer::getRendertypeEndPortalShader);
            RenderSystem.setShaderTexture(0, -5);
            pBuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator pTesselator) {
            pTesselator.end();
        }

        @Override
        public String toString() {
            return "PARTICLE_SHEET_END_PORTAL";
        }
    };

    void begin(BufferBuilder pBuilder, TextureManager pTextureManager);

    void end(Tesselator pTesselator);

}


