package net.goo.brutality.mixin;

import net.goo.brutality.registry.BrutalityModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class BedBlockMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void cancelIfCaffeinated(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit, CallbackInfoReturnable<InteractionResult> cir) {
        if (pPlayer.hasEffect(BrutalityModMobEffects.CAFFEINATED.get())) {
            pPlayer.sendSystemMessage(Component.translatable("message.brutality.caffeine_sleep"));
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }

}
