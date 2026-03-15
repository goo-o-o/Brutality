package net.goo.brutality.mixin.accessors;

import com.mojang.blaze3d.shaders.BlendMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({BlendMode.class})
public interface BlendModeAccessor {
    @Accessor
    static BlendMode getLastApplied() {
        return null;
    }

    @Accessor
    static void setLastApplied(BlendMode blendMode) {
    }
}