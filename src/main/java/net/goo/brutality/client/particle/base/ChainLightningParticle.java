package net.goo.brutality.client.particle.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.goo.brutality.client.particle.providers.ChainLightningParticleData;
import net.goo.brutality.client.renderers.BrutalityRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.*;
import java.util.List;

/***
 * This class is based of LightningEffect Code from Goety:
 <a href="https://github.com/Polarice3/Goety-2/blob/1.20/src/main/java/com/Polarice3/Goety/client/particles/LightningEffect.java">...</a>
 * which is based off Botania.
 */
public class ChainLightningParticle {
    public static final ChainLightningParticle INSTANCE = new ChainLightningParticle();

    private static float REFRESH_TIME = 3F;
    private static final double LIFETIME_AFTER_LAST_BOLT = 100;
    private Timestamp refreshTimestamp = Timestamp.ZERO;

    public record Timestamp(long ticks, float partial) {
        public static final Timestamp ZERO = new Timestamp(0, 0);

        public Timestamp subtract(Timestamp other) {
            long newTicks = this.ticks - other.ticks;
            float newPartial = this.partial - other.partial;
            if (newPartial < 0) {
                newPartial += 1;
                newTicks -= 1;
            }
            return new Timestamp(newTicks, newPartial);
        }

        public float value() {
            return this.ticks + this.partial;
        }

        public boolean isPassed(Timestamp prev, double duration) {
            long ticksPassed = this.ticks - prev.ticks;
            if (ticksPassed > duration) {
                return true;
            }

            duration -= ticksPassed;
            if (duration >= 1) {
                return false;
            }

            return (this.partial - prev.partial) >= duration;
        }
    }

    private final Random random = new Random();

    private final List<BoltEmitter> boltEmitters = new LinkedList<>();

    public static void onWorldRenderLast(Camera camera, float partialTicks, PoseStack poseStack, RenderBuffers buffers) {
        poseStack.pushPose();
        Vec3 camVec = camera.getPosition();
        poseStack.translate(-camVec.x, -camVec.y, -camVec.z);
        MultiBufferSource.BufferSource bufferSource = buffers.bufferSource();
        ChainLightningParticle.INSTANCE.render(partialTicks, poseStack, bufferSource);
        bufferSource.endBatch(BrutalityRenderTypes.LIGHTNING);
        poseStack.popPose();
    }

    public void render(float partialTicks, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource) {
        if (Minecraft.getInstance().level != null) {
            VertexConsumer buffer = bufferSource.getBuffer(BrutalityRenderTypes.LIGHTNING);
            Matrix4f matrix = poseStack.last().pose();
            Timestamp timestamp = new Timestamp(Minecraft.getInstance().level.getGameTime(), partialTicks);
            boolean refresh = timestamp.isPassed(this.refreshTimestamp, 1 / REFRESH_TIME);
            if (refresh) this.refreshTimestamp = timestamp;

            for (Iterator<BoltEmitter> iter = this.boltEmitters.iterator(); iter.hasNext(); ) {
                BoltEmitter emitter = iter.next();
                emitter.renderTick(timestamp, refresh, matrix, buffer);
                if (emitter.shouldRemove(timestamp)) {
                    iter.remove();
                }
            }
        }
    }

    public void add(Level level, ChainLightningParticleData options, float partialTicks) {
        if (!level.isClientSide()) return;

        BoltEmitter emitter = new BoltEmitter(options);
        Timestamp timestamp = new Timestamp(level.getGameTime(), partialTicks);
        if ((!emitter.options.getSpawnFunction().isConsecutive() || emitter.bolts.isEmpty()) && timestamp.isPassed(emitter.lastBoltTimestamp, emitter.lastBoltDelay)) {
            emitter.addBolt(new BoltInstance(options, timestamp), timestamp);
        }
        emitter.lastUpdateTimestamp = timestamp;
        this.boltEmitters.add(emitter);
    }

    public class BoltEmitter {
        private final Set<BoltInstance> bolts = new ObjectOpenHashSet<>();
        private final ChainLightningParticleData options;
        private Timestamp lastBoltTimestamp = Timestamp.ZERO;
        private Timestamp lastUpdateTimestamp = Timestamp.ZERO;
        private double lastBoltDelay;

        public BoltEmitter(ChainLightningParticleData options) {
            this.options = options;
        }

        private void addBolt(BoltInstance instance, Timestamp timestamp) {
            this.bolts.add(instance);
            this.lastBoltDelay = instance.options.getSpawnFunction().getSpawnDelay(random);
            this.lastBoltTimestamp = timestamp;
        }

        public void renderTick(Timestamp timestamp, boolean refresh, Matrix4f matrix, VertexConsumer buffer) {
            if (refresh) this.bolts.removeIf(bolt -> bolt.tick(timestamp));
            if (this.bolts.isEmpty() && this.options != null && this.options.getSpawnFunction().isConsecutive())
                addBolt(new BoltInstance(this.options, timestamp), timestamp);
            this.bolts.forEach(bolt -> bolt.render(matrix, buffer, timestamp));
        }

        public boolean shouldRemove(Timestamp timestamp) {
            return this.bolts.isEmpty() && timestamp.isPassed(this.lastUpdateTimestamp, LIFETIME_AFTER_LAST_BOLT);
        }

    }

    private static class BoltInstance {
        private final ChainLightningParticleData options;
        private final List<ChainLightningParticleData.BoltQuads> renderQuads;
        private final Timestamp createdTimestamp;
        private final Color color;

        public BoltInstance(ChainLightningParticleData options, Timestamp timestamp) {
            this.options = options;
            this.renderQuads = options.generate();
            this.createdTimestamp = timestamp;
            Random random = new Random();
            this.color = options.getColors()[random.nextInt(options.getColors().length)];
        }

        public void render(Matrix4f matrix, VertexConsumer buffer, Timestamp timestamp) {
            float lifeScale = timestamp.subtract(this.createdTimestamp).value() / this.options.getLifespan();
            Pair<Integer, Integer> bounds = options.getFadeFunction().getRenderBounds(this.renderQuads.size(), lifeScale);
            for (int i = bounds.getFirst(); i < bounds.getSecond(); i++) {
                this.renderQuads.get(i).getVecs().forEach(v -> buffer.vertex(matrix, (float) v.x, (float) v.y, (float) v.z)
                        .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                        .endVertex());
            }
        }

        public boolean tick(Timestamp timestamp) {
            return timestamp.isPassed(this.createdTimestamp, this.options.getLifespan());
        }
    }
}
