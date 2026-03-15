package net.goo.brutality.client.particle.base.trail;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.renderers.BrutalityPhotonParticleRenderType;
import net.goo.brutality.client.renderers.ParticleQueueRenderType;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;

/**
 * @author KilaBash
 * @date 2023/6/6
 * @implNote TrailEmitter
 */
@ParametersAreNonnullByDefault
public class TrailEmitter extends Emitter {
    public static int VERSION = 2;

    public final TrailConfig config;

    // runtime
    protected TrailParticle trailParticle;

    public TrailEmitter(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z); // Calls the Emitter constructor
        this.config = new TrailConfig();
        init();
    }

    public TrailEmitter(ClientLevel level, double x, double y, double z, TrailConfig config) {
        super(level, x, y, z);
        this.config = config;
        init();
    }

    public void init() {
        trailParticle = new TrailParticle(this, config, getThreadSafeRandomSource());
    }


    //////////////////////////////////////
    //*****     particle logic     *****//

    /// ///////////////////////////////////

    @Override
    public int getLifetime() {
        return config.duration;
    }

    @Override
    protected void updateOrigin() {
        super.updateOrigin();
        setLifetime(config.duration);
    }

    @Override
    public boolean isLooping() {
        return config.isLooping();
    }

    @Override
    public int getParticleAmount() {
        return trailParticle.isAlive() ? 1 : 0;
    }

    @Override
    protected void update() {
        if (trailParticle.isAlive()) {
            trailParticle.tick();
        } else {
            remove();
        }

        super.update();
    }

    @Override
    public void reset() {
        super.reset();
        init();
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, Camera camera, float pPartialTicks) {
        super.render(buffer, camera, pPartialTicks);
        System.out.println("Rendering trail");
        if (!ParticleQueueRenderType.INSTANCE.isRenderingQueue() && delay <= 0 && isVisible() &&
                BrutalityPhotonParticleRenderType.checkLayer(config.renderer.getLayer()) &&
                (!config.renderer.getCull().isEnable() ||
                        BrutalityPhotonParticleRenderType.checkFrustum(config.renderer.getCull().getCullAABB(this, pPartialTicks)))) {
            ParticleQueueRenderType.INSTANCE.pipeQueue(trailParticle.getRenderType(), Collections.singleton(trailParticle), camera, pPartialTicks);
        }
    }

    //////////////////////////////////////
    //********      Emitter    *********//

    /// ///////////////////////////////////

    @Override
    @Nullable
    public AABB getCullBox(float partialTicks) {
        return config.renderer.getCull().isEnable() ? config.renderer.getCull().getCullAABB(this, partialTicks) : null;
    }

    @Override
    public void remove(boolean force) {
        trailParticle.setRemoved(true);
        super.remove(force);
        if (force) {
            trailParticle.getTails().clear();
        }
    }
}
