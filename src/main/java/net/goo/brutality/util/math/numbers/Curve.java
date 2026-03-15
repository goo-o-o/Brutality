package net.goo.brutality.util.math.numbers;


import com.lowdragmc.photon.client.gameobject.emitter.data.number.curve.ECBCurves;
import net.minecraft.util.RandomSource;

import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/5/26
 * @implNote Curve
 */
public class Curve implements NumberFunction {

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public ECBCurves getCurves() {
        return curves;
    }

    public void setCurves(ECBCurves curves) {
        this.curves = curves;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(float defaultValue) {
        this.defaultValue = defaultValue;
    }


    public float getLower() {
        return lower;
    }

    public void setLower(float lower) {
        this.lower = lower;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getUpper() {
        return upper;
    }

    public void setUpper(float upper) {
        this.upper = upper;
    }

    private float min, max, defaultValue;
    private ECBCurves curves;
    protected boolean lockControlPoint = true;
    private float lower, upper;

    public Curve() {
        this(0, 0, 0, 0, 0);
    }

    public Curve(float min, float max, float lower, float upper, float defaultValue) {
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.lower = lower;
        this.upper = upper;
        var y = (upper == lower) ? 0.5f : (defaultValue - lower) / (upper - lower);
        this.curves = new ECBCurves(0, y, 0.1f, y, 0.9f, y, 1, y);
    }

    @Override
    public Number get(RandomSource randomSource, float t) {
        return lower + (upper - lower) * curves.getCurveY(t);
    }

    @Override
    public Number get(float t, Supplier<Float> lerp) {
        return lower + (upper - lower) * curves.getCurveY(t);
    }

    @Override
    public NumberFunction copy() {
        var curve = new Curve(min, max, lower, upper, defaultValue);
        curve.curves = this.curves.copy();
        return curve;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Curve curve) {
            return min == curve.min && max == curve.max && defaultValue == curve.defaultValue && lower == curve.lower && upper == curve.upper && curves.equals(curve.curves);
        }
        return super.equals(obj);
    }
}
