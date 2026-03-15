package net.goo.brutality.client.particle.base.trail.settings;

import net.goo.brutality.client.particle.base.trail.IParticle;
import net.goo.brutality.util.math.numbers.NumberFunction;

public class LightOverLifetimeSetting extends ToggleGroup {

    protected NumberFunction skyLight = NumberFunction.constant(15);

    protected NumberFunction blockLight = NumberFunction.constant(15);

    public LightOverLifetimeSetting() {
        this.enable = true;
    }

    public int getLight(IParticle particle, float partialTicks) {
        int sky = skyLight.get(particle.getT(partialTicks), () -> particle.getMemRandom("sky-light")).intValue();
        int block = blockLight.get(particle.getT(partialTicks), () -> particle.getMemRandom("block-light")).intValue();
        return sky << 20 | block << 4;
    }
}
