package net.goo.brutality.util.particle;

public interface ISceneObject {
    Transform transform();

    // Lifecycle hooks called by Transform or the Engine
    default void onTransformChanged() {}
    default void onChildChanged() {}
    default void onParentChanged() {}

    // Logic hooks
    default void updateTick() {}
    default void updateFrame(float partialTicks) {}
}