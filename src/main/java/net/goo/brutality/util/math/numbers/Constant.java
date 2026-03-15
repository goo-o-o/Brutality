package net.goo.brutality.util.math.numbers;

import com.lowdragmc.photon.client.gameobject.emitter.data.number.NumberFunctionConfig;
import net.minecraft.util.RandomSource;

import java.util.function.Supplier;

public class Constant implements NumberFunction {

    public Number getNumber() {
        return number;
    }

    public void setNumber(Number number) {
        this.number = number;
    }

    private Number number;

    public Constant() {
        number = 0;
    }

    public Constant(Number number) {
        this.number = number;
    }

    public Constant(NumberFunctionConfig config) {
        this(config.isDecimals() ? config.defaultValue() : ((int) config.defaultValue()));
    }

    @Override
    public Number get(RandomSource randomSource, float t) {
        return number;
    }

    @Override
    public Number get(float t, Supplier<Float> lerp) {
        return number;
    }

    @Override
    public NumberFunction copy() {
        return new Constant(number);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Constant constant) {
            return number.equals(constant.number);
        }
        return super.equals(obj);
    }


}
