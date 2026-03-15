package net.goo.brutality.client.particle.base.trail;

import net.minecraft.world.level.Level;

public interface IEffect {
    Level getLevel();
    default void updateFXObjectTick(IFXObject fxObject) {}
    default void updateFXObjectFrame(IFXObject fxObject, float partialTicks) {}
}