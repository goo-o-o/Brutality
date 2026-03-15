//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.goo.brutality.client.particle.base.trail;


import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.shaders.BlendMode;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.goo.brutality.client.particle.base.trail.settings.LightOverLifetimeSetting;
import net.goo.brutality.client.particle.base.trail.settings.UVAnimationSetting;
import net.goo.brutality.client.renderers.BrutalityPhotonParticleRenderType;
import net.goo.brutality.mixin.accessors.BlendModeAccessor;
import net.goo.brutality.mixin.accessors.ShaderInstanceAccessor;
import net.goo.brutality.util.math.numbers.Gradient;
import net.goo.brutality.util.math.numbers.NumberFunction;
import net.goo.brutality.util.particle.BloomEffect;
import net.goo.brutality.util.particle.CustomShaderMaterial;
import net.goo.brutality.util.particle.MaterialSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;

import javax.annotation.Nonnull;

public class TrailConfig {
    protected int duration = 100;
    protected boolean looping = true;
    protected int startDelay = 0;
    protected int time = 20;
    protected float minVertexDistance = 0.05F;
    protected boolean smoothInterpolation = false;
    protected boolean calculateSmoothByShader = false;
    protected boolean parallelRendering = false;
    protected TrailParticle.UVMode uvMode;
    protected NumberFunction widthOverTrail;
    protected NumberFunction colorOverTrail;
    public final MaterialSetting material;
    public final RendererSetting renderer;
    public final LightOverLifetimeSetting lights;
    public final UVAnimationSetting uvAnimation;
    public final BrutalityPhotonParticleRenderType particleRenderType;

    public TrailConfig() {
        this.uvMode = TrailParticle.UVMode.STRETCH;
        this.widthOverTrail = NumberFunction.constant(0.2F);
        this.colorOverTrail = new Gradient();
        this.material = new MaterialSetting();
        this.renderer = new RendererSetting();
        this.lights = new LightOverLifetimeSetting();
        this.uvAnimation = new UVAnimationSetting();
        this.particleRenderType = new RenderType();
        this.material.setMaterial(new CustomShaderMaterial());
    }
    

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public boolean isLooping() {
        return this.looping;
    }

    public void setStartDelay(int startDelay) {
        this.startDelay = startDelay;
    }

    public int getStartDelay() {
        return this.startDelay;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return this.time;
    }

    public void setMinVertexDistance(float minVertexDistance) {
        this.minVertexDistance = minVertexDistance;
    }

    public float getMinVertexDistance() {
        return this.minVertexDistance;
    }

    public boolean isSmoothInterpolation() {
        return this.smoothInterpolation;
    }

    public boolean isCalculateSmoothByShader() {
        return this.calculateSmoothByShader;
    }

    public void setParallelRendering(boolean parallelRendering) {
        this.parallelRendering = parallelRendering;
    }

    public boolean isParallelRendering() {
        return this.parallelRendering;
    }

    public void setUvMode(TrailParticle.UVMode uvMode) {
        this.uvMode = uvMode;
    }

    public net.goo.brutality.client.particle.base.trail.TrailParticle.UVMode getUvMode() {
        return this.uvMode;
    }

    public void setWidthOverTrail(NumberFunction widthOverTrail) {
        this.widthOverTrail = widthOverTrail;
    }

    public NumberFunction getWidthOverTrail() {
        return this.widthOverTrail;
    }

    public void setColorOverTrail(NumberFunction colorOverTrail) {
        this.colorOverTrail = colorOverTrail;
    }

    public NumberFunction getColorOverTrail() {
        return this.colorOverTrail;
    }

    public MaterialSetting getMaterial() {
        return this.material;
    }

    public RendererSetting getRenderer() {
        return this.renderer;
    }

    public LightOverLifetimeSetting getLights() {
        return this.lights;
    }

    public UVAnimationSetting getUvAnimation() {
        return this.uvAnimation;
    }

    private class RenderType extends BrutalityPhotonParticleRenderType {
        private BlendMode lastBlend = null;

        public void prepareStatus() {
            if (TrailConfig.this.renderer.isBloomEffect()) {
                this.beginBloom();
                BloomEffect.setBloomColor(TrailConfig.this.renderer.getBloomColor());
            } else {
                RenderSystem.setShader(GameRenderer::getParticleShader);
            }

            TrailConfig.this.material.pre();
            TrailConfig.this.material.getMaterial().begin(false);
            ShaderInstance var2 = RenderSystem.getShader();
            if (var2 instanceof ShaderInstanceAccessor shader) {
                this.lastBlend = BlendModeAccessor.getLastApplied();
                BlendModeAccessor.setLastApplied(shader.getBlend());
            }

            RenderTarget input = BloomEffect.getInput();
            input.bindWrite(false);
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
        }

        public void begin(@Nonnull BufferBuilder bufferBuilder) {
            bufferBuilder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.PARTICLE);
        }

        public void releaseStatus() {
            TrailConfig.this.material.getMaterial().end(false);
            TrailConfig.this.material.post();
            if (this.lastBlend != null) {
                this.lastBlend.apply();
                this.lastBlend = null;
            }

            this.endParticle();
        }

        public boolean isParallel() {
            return TrailConfig.this.isParallelRendering();
        }
    }
}
