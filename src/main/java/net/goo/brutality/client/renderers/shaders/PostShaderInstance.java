package net.goo.brutality.client.renderers.shaders;


import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.goo.brutality.client.event.forge.ShaderRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.List;
// Massive thanks to https://github.com/Cmdpro/Databank/blob/main/src/main/java/com/cmdpro/databank/shaders/PostShaderInstance.java
@OnlyIn(Dist.CLIENT)

public abstract class PostShaderInstance {
    public abstract ResourceLocation getShaderLocation();
    private PostChain postChain;
    public float time;
    public List<PostPass> passes;
    private boolean active;
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        if (!active) {
            time = 0;
        }
        this.active = active;
    }
    public void resize(int pWidth, int pHeight) {
        if (postChain != null) {
            postChain.resize(pWidth, pHeight);
        }
    }
    public void queueRemoval() {
        if (!PostShaderManager.removalQueue.contains(this)) {
            PostShaderManager.removalQueue.add(this);
        }
    }

    public void close() {
        if (postChain != null) {
            postChain.close();
            postChain = null;
            passes = null;
        }
    }

    public static void resetDepthBackup() {
        if (depthBackupTarget != null) {
            depthBackupTarget.destroyBuffers();
            depthBackupTarget = null;
        }
    }

    public RenderTarget getSilhouetteTarget() {
        return ShaderRenderEvents.getSilhouetteTarget(this);
    }

    public void process() {
        if (postChain == null) {
            try {
                postChain = new PostChain(Minecraft.getInstance().getTextureManager(), Minecraft.getInstance().getResourceManager(), Minecraft.getInstance().getMainRenderTarget(), getShaderLocation());
                postChain.resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
                passes = postChain.passes;
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        if (postChain != null) {
            beforeProcess();
            if (active) {
                for (PostPass i : passes) {
                    i.getEffect().safeGetUniform("time").set(getTime());
                    i.getEffect().safeGetUniform("CameraPosition").set(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().toVector3f());
                    i.getEffect().safeGetUniform("ModelViewMat").set(RenderSystem.getModelViewMatrix());
                    i.getEffect().safeGetUniform("invViewMat").set(new Matrix4f(PostShaderManager.viewStackMatrix).invert());
                    i.getEffect().safeGetUniform("invProjMat").set(new Matrix4f(RenderSystem.getProjectionMatrix()).invert());
                    setUniforms(i);
                }
                processPostChain();
                afterProcess();
            }
        }
    }
    public void processPostChain() {
        getDepthBackupTarget().copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.resetTextureMatrix();
        postChain.process(Minecraft.getInstance().getPartialTick());
        Minecraft.getInstance().getMainRenderTarget().copyDepthFrom(getDepthBackupTarget());
        Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
    }


    private static RenderTarget depthBackupTarget;
    protected static RenderTarget getDepthBackupTarget() {
        if (depthBackupTarget == null) {
            depthBackupTarget = new MainTarget(Minecraft.getInstance().getMainRenderTarget().width, Minecraft.getInstance().getMainRenderTarget().height);
        }
        return depthBackupTarget;
    }
    public void tick() {
        time += 1/20f;
    }
    public void setUniforms(PostPass instance) {}
    public void beforeProcess() {}
    public void afterProcess() {}
    public float getTime() {
        return time + (Minecraft.getInstance().getPartialTick() / 20f);
    }
    public RenderLevelStageEvent.Stage getRenderStage() {
        return RenderLevelStageEvent.Stage.AFTER_LEVEL;
    }
}