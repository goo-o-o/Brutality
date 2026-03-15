package net.goo.brutality.util.math.numbers;

import net.minecraft.util.RandomSource;

import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/5/25
 * @implNote NumberFunction
 */
public interface NumberFunction {
//    Map<String, Class< ? extends NumberFunction>> REGISTRY = new HashMap<>(Map.ofEntries(
//            Map.entry(Color.class.getSimpleName(), Color.class),
//            Map.entry(RandomColor.class.getSimpleName(), RandomColor.class),
//            Map.entry(Constant.class.getSimpleName(), Constant.class),
//            Map.entry(RandomConstant.class.getSimpleName(), RandomConstant.class),
//            Map.entry(Curve.class.getSimpleName(), Curve.class),
//            Map.entry(RandomCurve.class.getSimpleName(), RandomCurve.class),
//            Map.entry(Gradient.class.getSimpleName(), Gradient.class),
//            Map.entry(RandomGradient.class.getSimpleName(), RandomGradient.class)
//    ));

    static NumberFunction constant(Number constant) {
        return new Constant(constant);
    }

    static NumberFunction color(Number color) {
        return new Color(color);
    }


    static NumberFunction copy(NumberFunction function) {
        return function.copy();
    }

    static boolean isEqual(NumberFunction a, NumberFunction b) {
        return a.equals(b);
    }

    NumberFunction copy();

    default Number get(RandomSource randomSource, float t) {
        return get(t, randomSource::nextFloat);
    }

    Number get(float t, Supplier<Float> lerp);

}
