//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.goo.brutality.util.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MaterialSetting {
    protected final BlendMode blendMode = new BlendMode();
    protected boolean cull = true;
    protected boolean depthTest = true;
    protected boolean depthMask = false;
    @NotNull
    protected IMaterial material = new TextureMaterial();

    public void pre() {
        this.blendMode.apply();
        if (this.cull) {
            RenderSystem.enableCull();
        } else {
            RenderSystem.disableCull();
        }

        if (this.depthTest) {
            RenderSystem.enableDepthTest();
        } else {
            RenderSystem.disableDepthTest();
        }

        RenderSystem.depthMask(this.depthMask);
    }

    public void post() {
        if (this.blendMode.getBlendFunc() != BlendMode.BlendFuc.ADD) {
            RenderSystem.blendEquation(BlendMode.BlendFuc.ADD.op);
        }

        if (!this.cull) {
            RenderSystem.enableCull();
        }

        if (!this.depthTest) {
            RenderSystem.enableDepthTest();
        }

        if (!this.depthMask) {
            RenderSystem.depthMask(true);
        }

    }


    public BlendMode getBlendMode() {
        return this.blendMode;
    }

    public boolean isCull() {
        return this.cull;
    }

    public boolean isDepthTest() {
        return this.depthTest;
    }

    public boolean isDepthMask() {
        return this.depthMask;
    }

    @NotNull
    public IMaterial getMaterial() {
        return this.material;
    }

    public void setCull(boolean cull) {
        this.cull = cull;
    }

    public void setDepthTest(boolean depthTest) {
        this.depthTest = depthTest;
    }

    public void setDepthMask(boolean depthMask) {
        this.depthMask = depthMask;
    }

    public void setMaterial(@NotNull IMaterial material) {
        this.material = material;
    }
}
