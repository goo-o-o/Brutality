package net.goo.brutality.util;

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
}
