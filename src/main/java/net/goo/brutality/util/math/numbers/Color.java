package net.goo.brutality.util.math.numbers;

import com.lowdragmc.photon.client.gameobject.emitter.data.number.NumberFunctionConfig;

public class Color extends Constant {

    public Color() {
        super(-1);
    }

    public Color(Number number) {
        super(number);
    }

    public Color(NumberFunctionConfig config) {
        super(config);
    }

    @Override
    public NumberFunction copy() {
        return new Color(getNumber());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Color color) {
            return getNumber().equals(color.getNumber());
        }
        return super.equals(obj);
    }
}
