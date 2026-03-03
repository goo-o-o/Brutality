package net.goo.brutality.common.mixin_helpers;

public interface MobEffectInstanceSourceAccessor {
    // Used to attach an entity to any effect instance
    Integer getSourceId();
    void setSourceId(int id);
}
