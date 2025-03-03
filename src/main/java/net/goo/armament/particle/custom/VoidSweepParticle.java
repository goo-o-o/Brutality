package net.goo.armament.particle.custom;

import net.goo.armament.particle.ArmaSweepParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static net.goo.armament.util.ModResources.MAX_LIGHT;

@OnlyIn(Dist.CLIENT)
public class VoidSweepParticle extends ArmaSweepParticle {
    private final SpriteSet sprites;

    VoidSweepParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pQuadSizeMultiplier, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ,0.0D, pSprites);
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
        return MAX_LIGHT;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

}