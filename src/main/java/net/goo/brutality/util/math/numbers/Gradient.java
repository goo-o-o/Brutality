package net.goo.brutality.util.math.numbers;


import net.minecraft.util.RandomSource;

import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/5/26
 * @implNote Gradient
 */
public class Gradient implements NumberFunction {

    public GradientColor getGradientColor() {
        return gradientColor;
    }

    private final GradientColor gradientColor;

    public Gradient() {
        this.gradientColor = new GradientColor();
    }

    public Gradient(int color) {
        this.gradientColor = new GradientColor(color, color);
    }

    public Gradient(GradientColor gradientColor) {
        this.gradientColor = gradientColor;
    }


    @Override
    public Number get(RandomSource randomSource, float t) {
        return gradientColor.getColor(t);
    }

    @Override
    public Number get(float t, Supplier<Float> lerp) {
        return gradientColor.getColor(t);
    }

    @Override
    public NumberFunction copy() {
        return new Gradient(gradientColor);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Gradient gradient) {
            return gradientColor.equals(gradient.gradientColor);
        }
        return super.equals(obj);
    }

}
