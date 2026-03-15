package net.goo.brutality.client.particle.base.trail;

import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FXProjectEffect implements IEffect {
    public final Level level;

    public FXProjectEffect(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return this.level;
    }
}
