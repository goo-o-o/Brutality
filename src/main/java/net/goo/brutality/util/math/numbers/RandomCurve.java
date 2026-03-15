package net.goo.brutality.util.math.numbers;


import com.lowdragmc.photon.client.gameobject.emitter.data.number.curve.ECBCurves;

import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/5/30
 * @implNote RandomCurve
 */
public class RandomCurve implements NumberFunction {

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public ECBCurves getCurves0() {
        return curves0;
    }

    public void setCurves0(ECBCurves curves0) {
        this.curves0 = curves0;
    }

    public ECBCurves getCurves1() {
        return curves1;
    }

    public void setCurves1(ECBCurves curves1) {
        this.curves1 = curves1;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(float defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isLockControlPoint() {
        return lockControlPoint;
    }

    public void setLockControlPoint(boolean lockControlPoint) {
        this.lockControlPoint = lockControlPoint;
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
    private ECBCurves curves0, curves1;
    protected boolean lockControlPoint = true;
    private float lower, upper;

    public RandomCurve() {
        this(0, 0, 0, 0, 0);
    }

    public RandomCurve(float min, float max, float lower, float upper, float defaultValue) {
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.lower = lower;
        this.upper = upper;
        var y = (upper == lower) ? 0.5f : (defaultValue - lower) / (upper - lower);
        this.curves0 = new ECBCurves(0, y, 0.1f, y, 0.9f, y, 1, y);
        this.curves1 = new ECBCurves(0, y, 0.1f, y, 0.9f, y, 1, y);
    }

    @Override
    public Number get(float t, Supplier<Float> lerp) {
        var a = curves0.getCurveY(t);
        var b = curves1.getCurveY(t);
        var randomY = a == b ? a : (Math.min(a, b) + lerp.get() * Math.abs(a - b));
        return lower + (upper - lower) * randomY;
    }

    @Override
    public NumberFunction copy() {
        var curve = new RandomCurve(min, max, lower, upper, defaultValue);
        curve.curves0 = this.curves0.copy();
        curve.curves1 = this.curves1.copy();
        return curve;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RandomCurve curve) {
            return curves0.equals(curve.curves0) &&
                    curves1.equals(curve.curves1) &&
                    lower == curve.lower &&
                    upper == curve.upper &&
                    min == curve.min &&
                    max == curve.max &&
                    defaultValue == curve.defaultValue;
        }
        return super.equals(obj);
    }

}
