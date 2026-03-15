package net.goo.brutality.util.math.numbers;

import com.lowdragmc.photon.client.gameobject.emitter.data.number.NumberFunctionConfig;

import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/5/26
 * @implNote RandomConstant
 */
public class RandomConstant implements NumberFunction {

    public Number getA() {
        return a;
    }

    public void setA(Number a) {
        this.a = a;
    }

    public Number getB() {
        return b;
    }

    public void setB(Number b) {
        this.b = b;
    }

    public boolean isDecimals() {
        return isDecimals;
    }

    public void setDecimals(boolean decimals) {
        isDecimals = decimals;
    }

    private Number a, b;
    private boolean isDecimals;

    public RandomConstant() {
        a = 0;
        b = 0;
    }

    public RandomConstant(Number a, Number b, boolean isDecimals) {
        this.a = a;
        this.b = b;
        this.isDecimals = isDecimals;
    }

    public RandomConstant(NumberFunctionConfig config) {
        this(config.defaultValue(), config.defaultValue(), config.isDecimals());
    }

    @Override
    public Number get(float t, Supplier<Float> lerp) {
        float min = Math.min(a.floatValue(), b.floatValue());
        float max = Math.max(a.floatValue(), b.floatValue());
        if (min == max) return max;
        if (isDecimals) return (min + lerp.get() * (max - min));
        return (int)(min + lerp.get() * (max + 1 - min));
    }

    @Override
    public NumberFunction copy() {
        return new RandomConstant(a, b, isDecimals);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RandomConstant constant) {
            return a.equals(constant.a) && b.equals(constant.b) && isDecimals == constant.isDecimals;
        }
        return super.equals(obj);
    }
}
