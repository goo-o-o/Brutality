package net.goo.brutality.common.mixin_helpers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class MixinInterfaces {
    // Functional interface to replace EntityInteraction
    @FunctionalInterface
    public interface InteractionFunction {
        InteractionResult apply(Player player, Entity entity, InteractionHand hand);
    }

    public interface MobEffectInstanceSourceAccessor {
        Integer brutality$getSourceID();
        void brutality$setSourceID(int id);
    }
}
