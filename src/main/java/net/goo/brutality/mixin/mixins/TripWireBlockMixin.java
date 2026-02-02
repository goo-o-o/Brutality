package net.goo.brutality.mixin.mixins;

import net.goo.brutality.util.item.StatTrakUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TripWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TripWireBlock.class)
public class TripWireBlockMixin {

    @Inject(method = "playerWillDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private void onDisarm(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer, CallbackInfo ci) {
        StatTrakUtils.incrementStatTrakIfPossible(pPlayer.getMainHandItem());
    }
}
