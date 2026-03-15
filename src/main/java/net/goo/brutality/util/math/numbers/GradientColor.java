package net.goo.brutality.util.math.numbers;

import com.lowdragmc.lowdraglib.utils.ColorUtils;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.List;

public class GradientColor {
    protected List<Vec2> aP;
    protected List<Vec2> rP;
    protected List<Vec2> gP;
    protected List<Vec2> bP;

    public GradientColor() {
        this.aP = new ArrayList<>(List.of(new Vec2(0.0F, 1.0F), new Vec2(1.0F, 1.0F)));
        this.rP = new ArrayList<>(List.of(new Vec2(0.0F, 1.0F), new Vec2(1.0F, 1.0F)));
        this.gP = new ArrayList<>(List.of(new Vec2(0.0F, 1.0F), new Vec2(1.0F, 1.0F)));
        this.bP = new ArrayList<>(List.of(new Vec2(0.0F, 1.0F), new Vec2(1.0F, 1.0F)));
    }

    public GradientColor(int... colors) {
        this.aP = new ArrayList<>();
        this.rP = new ArrayList<>();
        this.gP = new ArrayList<>();
        this.bP = new ArrayList<>();
        if (colors.length == 1) {
            this.aP.add(new Vec2(0.5F, ColorUtils.alpha(colors[0])));
            this.rP.add(new Vec2(0.5F, ColorUtils.red(colors[0])));
            this.gP.add(new Vec2(0.5F, ColorUtils.green(colors[0])));
            this.bP.add(new Vec2(0.5F, ColorUtils.blue(colors[0])));
        }

        for(int i = 0; i < colors.length; ++i) {
            float t = (float)i / ((float)colors.length - 1.0F);
            this.aP.add(new Vec2(t, ColorUtils.alpha(colors[i])));
            this.rP.add(new Vec2(t, ColorUtils.red(colors[i])));
            this.gP.add(new Vec2(t, ColorUtils.green(colors[i])));
            this.bP.add(new Vec2(t, ColorUtils.blue(colors[i])));
        }

    }

    public float get(List<Vec2> data, float t) {
        float value = (data.get(0)).y;
        boolean found = t < (data.get(0)).x;
        if (!found) {
            for(int i = 0; i < data.size() - 1; ++i) {
                Vec2 s = data.get(i);
                Vec2 e = data.get(i + 1);
                if (t >= s.x && t <= e.x) {
                    value = s.y * (e.x - t) / (e.x - s.x) + e.y * (t - s.x) / (e.x - s.x);
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            value = (data.get(data.size() - 1)).y;
        }

        return value;
    }

    public int getColor(float t) {
        return ColorUtils.color(this.get(this.aP, t), this.get(this.rP, t), this.get(this.gP, t), this.get(this.bP, t));
    }

    public int getRGBColor(float t) {
        return ColorUtils.color(1.0F, this.get(this.rP, t), this.get(this.gP, t), this.get(this.bP, t));
    }

    public int add(List<Vec2> data, float t, float value) {
        if (data.isEmpty()) {
            data.add(new Vec2(t, value));
            return 0;
        } else if (t < (data.get(0)).x) {
            data.add(0, new Vec2(t, value));
            return 0;
        } else {
            for(int i = 0; i < data.size() - 1; ++i) {
                if (t >= (data.get(i)).x && t <= (data.get(i + 1)).x) {
                    data.add(i + 1, new Vec2(t, value));
                    return i + 1;
                }
            }

            data.add(new Vec2(t, value));
            return data.size() - 1;
        }
    }

    public int addAlpha(float t, float value) {
        return this.add(this.aP, t, value);
    }

    public int addRGB(float t, float r, float g, float b) {
        this.add(this.rP, t, r);
        this.add(this.gP, t, g);
        return this.add(this.bP, t, b);
    }

    public List<Vec2> getAP() {
        return this.aP;
    }

    public List<Vec2> getRP() {
        return this.rP;
    }

    public List<Vec2> getGP() {
        return this.gP;
    }

    public List<Vec2> getBP() {
        return this.bP;
    }
}
