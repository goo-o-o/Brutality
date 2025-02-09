package net.goo.armament.particle.custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class VoidSweepParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    VoidSweepParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pQuadSizeMultiplier, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ, 0.0D, 0.0D, 0.0D);
        this.sprites = pSprites;
        this.lifetime = 4;
        float f = this.random.nextFloat() * 0.6F + 0.4F;
        this.rCol = f;
        this.gCol = f;
        this.bCol = f;
        this.quadSize = 1.5F - (float)pQuadSizeMultiplier * 0.5F;
        this.setSpriteFromAge(pSprites);
    }

    public int getLightColor(float pPartialTick) {
        return 15728880;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new VoidSweepParticle(pLevel, pX, pY, pZ, pXSpeed, this.sprites);
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        Vec3 cameraPos = camera.getPosition();
        float f = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPos.x);
        float f1 = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPos.y);
        float f2 = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPos.z);

        Quaternionf quaternionRotation = camera.rotation();

        Vector3f[] quadVertices = new Vector3f[] {
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F,  1.0F, 0.0F),
                new Vector3f( 1.0F,  1.0F, 0.0F),
                new Vector3f( 1.0F, -1.0F, 0.0F)
        };

        float size = this.getQuadSize(partialTicks);

        for (int i = 0; i < 4; i++) {
            quadVertices[i].rotate(quaternionRotation);
            quadVertices[i].mul(size);
            quadVertices[i].add(f, f1, f2);
        }

        // Texture coordinates
        float uMin = this.getU0();
        float uMax = this.getU1();
        float vMin = this.getV0();
        float vMax = this.getV1();
        int lightColor = this.getLightColor(partialTicks);

        // Vertex buffer for rendering
        buffer.vertex((double) quadVertices[0].x(), (double) quadVertices[0].y(), (double) quadVertices[0].z())
                .uv(uMin, vMax)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(lightColor)
                .endVertex();
        buffer.vertex((double) quadVertices[1].x(), (double) quadVertices[1].y(), (double) quadVertices[1].z())
                .uv(uMin, vMin)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(lightColor)
                .endVertex();
        buffer.vertex((double) quadVertices[2].x(), (double) quadVertices[2].y(), (double) quadVertices[2].z())
                .uv(uMax, vMin)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(lightColor)
                .endVertex();
        buffer.vertex((double) quadVertices[3].x(), (double) quadVertices[3].y(), (double) quadVertices[3].z())
                .uv(uMax, vMax)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(lightColor)
                .endVertex();
    }
}