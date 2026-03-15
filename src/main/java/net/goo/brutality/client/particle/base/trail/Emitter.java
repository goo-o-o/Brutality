
package net.goo.brutality.client.particle.base.trail;

import com.lowdragmc.lowdraglib.utils.DummyWorld;
import net.goo.brutality.client.renderers.BrutalityPhotonParticleRenderType;
import net.goo.brutality.client.renderers.ParticleQueueRenderType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public abstract class Emitter extends FXObject implements IParticleEmitter {
    protected int delay;
    @Nullable
    protected Vector3f previousPosition;
    protected Vector3f velocity = new Vector3f();
    protected float t;
    private final RandomSource threadSafeRandomSource = RandomSource.createThreadSafe();
    protected ConcurrentHashMap<Object, Float> memRandom = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<BlockPos, Integer> lightCache = new ConcurrentHashMap<>();

    protected Emitter(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    public RandomSource getRandomSource() {
        return this.random;
    }

    public final void tick() {
        super.tick();
        if (this.isAlive()) {
            if (this.delay > 0) {
                --this.delay;
            } else {
                if (this.previousPosition != null) {
                    this.velocity = this.transform.position().sub(this.previousPosition, new Vector3f());
                }

                this.previousPosition = this.transform.position();
                this.lightCache.clear();
                this.updateOrigin();
                this.update();
            }
        }
    }

    public void setPos(double x, double y, double z) {
        if (this.transform != null) {
            this.transform.position(new Vector3f((float)x, (float)y, (float)z));
        }
    }

    public Vector3f getVelocity() {
        return new Vector3f(this.velocity);
    }

    protected void update() {
        if (this.age >= this.getLifetime() && !this.isLooping()) {
            this.remove(false);
        }

        ++this.age;
        if (this.getLifetime() > 0) {
            this.t = (float) (this.age % this.getLifetime()) / (float)this.getLifetime();
        }

    }

    protected void updateOrigin() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.oRoll = this.roll;
    }

    protected int getLightColor(float partialTick) {
        BlockPos blockPos = new BlockPos((int)this.x, (int)this.y, (int)this.z);
        Level level = this.getLevel();
        return level == null || !level.hasChunkAt(blockPos) ? 0 : LevelRenderer.getLightColor(level, blockPos);
    }

    public float getT(float partialTicks) {
        return this.lifetime > 0 ? this.t + partialTicks / (float)this.lifetime : 0.0F;
    }

    public float getMemRandom(Object object) {
        return this.getMemRandom(object, RandomSource::nextFloat);
    }

    public float getMemRandom(Object object, Function<RandomSource, Float> randomFunc) {
        Float value = this.memRandom.get(object);
        return value == null ? this.memRandom.computeIfAbsent(object, (o) -> randomFunc.apply(this.random)) : value;
    }

    public void reset() {
        this.age = 0;
        this.memRandom.clear();
        this.removed = false;
        this.onGround = false;
        this.previousPosition = null;
        this.velocity.zero();
        this.t = 0.0F;
    }

    @Nonnull
    public final BrutalityPhotonParticleRenderType getRenderType() {
        return ParticleQueueRenderType.INSTANCE;
    }

    public boolean isAlive() {
        return !this.removed || this.getParticleAmount() != 0 || super.isAlive();
    }

    public int getLightColor(BlockPos pos) {
        return this.lightCache.computeIfAbsent(pos, (p) -> {
            Level level = this.getLevel();
            return level == null || !level.hasChunkAt(p) && !(level instanceof DummyWorld) ? 0 : LevelRenderer.getLightColor(level, p);
        });
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isLooping() {
        return false;
    }

    public void setRGBAColor(Vector4f color) {
        this.rCol = color.x;
        this.gCol = color.y;
        this.bCol = color.z;
        this.alpha = color.w;
    }

    public Vector4f getRGBAColor() {
        return new Vector4f(this.rCol, this.gCol, this.bCol, this.alpha);
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return this.delay;
    }

    public float getT() {
        return this.t;
    }

    public RandomSource getThreadSafeRandomSource() {
        return this.threadSafeRandomSource;
    }

    public ConcurrentHashMap<Object, Float> getMemRandom() {
        return this.memRandom;
    }
}
