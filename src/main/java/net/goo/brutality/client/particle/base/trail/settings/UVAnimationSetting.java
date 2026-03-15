//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.goo.brutality.client.particle.base.trail.settings;


import com.lowdragmc.lowdraglib.utils.Range;
import net.goo.brutality.client.particle.base.trail.IParticle;
import net.goo.brutality.util.math.numbers.NumberFunction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector4f;

@OnlyIn(Dist.CLIENT)
public class UVAnimationSetting extends ToggleGroup {
    protected Range tiles = new Range(1, 1);
    protected Animation animation;
    protected NumberFunction frameOverTime;
    protected NumberFunction startFrame;
    protected float cycle;

    public UVAnimationSetting() {
        this.animation = UVAnimationSetting.Animation.WHOLE_SHEET;
        this.frameOverTime = NumberFunction.constant(0);
        this.startFrame = NumberFunction.constant(0);
        this.cycle = 1.0F;
    }

    public Vector4f getUVs(IParticle particle, float partialTicks) {
        float t = particle.getT(partialTicks);
        float cellU = 1.0F / (float)this.tiles.getA().intValue();
        float cellV = 1.0F / (float)this.tiles.getB().intValue();
        float currentFrame = this.startFrame.get(t, () -> particle.getMemRandom("startFrame")).floatValue();
        currentFrame += this.cycle * this.frameOverTime.get(t, () -> particle.getMemRandom("frameOverTime")).floatValue();
        int cellSize = this.tiles.getA().intValue();
        float u0;
        float v0;
        if (this.animation == UVAnimationSetting.Animation.WHOLE_SHEET) {
            int X = (int)(currentFrame % (float)cellSize);
            int Y = (int)(currentFrame / (float)cellSize);
            u0 = (float)X * cellU;
            v0 = (float)Y * cellV;
        } else {
            int X = (int)(currentFrame % (float)cellSize);
            int Y = (int)(particle.getMemRandom("randomRow") * (float)this.tiles.getB().intValue());
            u0 = (float)X * cellU;
            v0 = (float)Y * cellV;
        }

        float u1 = u0 + cellU;
        float v1 = v0 + cellV;
        return new Vector4f(u0, v0, u1, v1);
    }

    public void setTiles(Range tiles) {
        this.tiles = tiles;
    }

    public Range getTiles() {
        return this.tiles;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    public void setFrameOverTime(NumberFunction frameOverTime) {
        this.frameOverTime = frameOverTime;
    }

    public NumberFunction getFrameOverTime() {
        return this.frameOverTime;
    }

    public void setStartFrame(NumberFunction startFrame) {
        this.startFrame = startFrame;
    }

    public NumberFunction getStartFrame() {
        return this.startFrame;
    }

    public void setCycle(float cycle) {
        this.cycle = cycle;
    }

    public float getCycle() {
        return this.cycle;
    }

    public enum Animation {
        WHOLE_SHEET, SINGLE_ROW
    }
}
