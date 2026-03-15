package net.goo.brutality.client.particle.base.trail;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.renderers.BrutalityPhotonParticleRenderType;
import net.minecraft.client.Camera;
import net.minecraft.util.RandomSource;

import java.util.function.Function;

public interface IParticle {

    BrutalityPhotonParticleRenderType getRenderType();

    RandomSource getRandomSource();

    boolean isRemoved();

    default boolean isAlive() {
        return !isRemoved();
    }

    float getT();

    float getT(float partialTicks);

    float getMemRandom(Object object);

    float getMemRandom(Object object, Function<RandomSource, Float> randomFunc);

    void tick();

    void render(VertexConsumer buffer, Camera camera, float pPartialTicks);

}
