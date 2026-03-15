package net.goo.brutality.client.particle.base.trail;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.util.particle.Transform;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public abstract class FXObject extends Particle implements IFXObject {
    protected String name = "";
    protected final Transform transform = new Transform(this);
    protected boolean visible = true;
    @Nullable
    protected IEffect effect;
    @Nullable
    protected ClientLevel realLevel;
    // Use ClientLevel to match Minecraft's Particle constructor
    protected FXObject(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.hasPhysics = false;
        this.friction = 1.0f;
    }

    @Override
    public boolean isAlive() {
        // A standalone particle is alive if it hasn't been removed
        if (!this.removed) return true;

        // Check hierarchy: if any child is still active, we stay "alive"
        for (var child : transform.children()) {
            if (child.sceneObject() instanceof IFXObject fx && fx.isAlive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isVisible() {
        if (!visible) return false;
        if (transform.parent() != null && transform.parent().sceneObject() instanceof IFXObject parentFX) {
            return parentFX.isVisible();
        }
        return true;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void setEffect(@Nullable IEffect effect) {
        this.effect = effect;
    }

    @Override
    @Nullable
    public IEffect getEffect() {
        return this.effect;
    }

    @Override
    public void setLevel(@Nullable Level level) {
        // We store it here instead of trying to assign to super.level
        this.realLevel = (ClientLevel) level;
    }

    @Nullable
    @Override
    public Level getLevel() {
        // If we have a custom level set, use it; otherwise fall back to the engine level
        return this.realLevel != null ? this.realLevel : this.level;
    }
    @Override
    public void move(double x, double y, double z) {
        // We override this to prevent Minecraft's default physics from
        // overwriting our Transform.position()
    }

    @Override
    protected int getLightColor(float partialTick) {
        if (this.level == null) return 0;
        Vector3f pos = transform.position();
        BlockPos blockPos = BlockPos.containing(pos.x, pos.y, pos.z);
        return this.level.hasChunkAt(blockPos) ? LevelRenderer.getLightColor(this.level, blockPos) : 0;
    }

    @Override
    public void remove(boolean force) {
        this.remove(); // Calls Particle.remove()
    }

    @Override
    public Transform transform() {
        return this.transform;
    }

    @Override
    public void tick() {
        super.tick(); // Handles age increment
        this.updateTick();
    }

    @Override
    public void updateTick() {
        if (effect != null) {
            effect.updateFXObjectTick(this);
        }
    }

    @Override
    public void render(@Nonnull VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        this.updateFrame(pPartialTicks);
    }

    @Override
    public void updateFrame(float partialTicks) {
        if (effect != null && isVisible()) {
            effect.updateFXObjectFrame(this, partialTicks);
        }
    }

    @Override
    @Nonnull
    public ParticleRenderType getRenderType() {
        return NO_RENDER_RENDER_TYPE;
    }

    public static final ParticleRenderType NO_RENDER_RENDER_TYPE = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {}
        @Override
        public void end(Tesselator tesselator) {}
        @Override
        public String toString() { return "NO_RENDER"; }
    };
}