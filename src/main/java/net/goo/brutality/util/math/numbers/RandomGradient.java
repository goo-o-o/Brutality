package net.goo.brutality.util.math.numbers;


import com.lowdragmc.lowdraglib.utils.ColorUtils;

import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/5/26
 * @implNote RandomGradient
 */
public class RandomGradient implements NumberFunction {



    public GradientColor getGradientColor0() {
        return gradientColor0;
    }

    public GradientColor getGradientColor1() {
        return gradientColor1;
    }

    private final GradientColor gradientColor0, gradientColor1;

    public RandomGradient() {
        this.gradientColor0 = new GradientColor();
        this.gradientColor1 = new GradientColor();
    }

    public RandomGradient(int color) {
        this.gradientColor0 = new GradientColor(color, color);
        this.gradientColor1 = new GradientColor(color, color);
    }

    public RandomGradient(GradientColor gradientColor0, GradientColor gradientColor1) {
        this.gradientColor0 = gradientColor0;
        this.gradientColor1 = gradientColor1;
    }

    @Override
    public Number get(float t, Supplier<Float> lerp) {
        int color0 = gradientColor0.getColor(t);
        int color1 = gradientColor1.getColor(t);
        return ColorUtils.blendColor(color0, color1, lerp.get());
    }

    @Override
    public NumberFunction copy() {
        return new RandomGradient(gradientColor0, gradientColor1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RandomGradient gradient) {
            return gradientColor0.equals(gradient.gradientColor0) &&
                    gradientColor1.equals(gradient.gradientColor1);
        }
        return super.equals(obj);
    }


}
