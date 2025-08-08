package net.goo.brutality.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }
//
//    // Courtesy of MomLove
//    @Redirect(method = "restoreFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
//    private boolean redirect$restoreFrom(GameRules instance, GameRules.Key<GameRules.BooleanValue> v) {
//        ServerPlayer player = (((ServerPlayer) (Object) this));
//        CombatTracker tracker = player.getCombatTracker();
//        System.out.println(tracker.entries);
//        if (!tracker.entries.isEmpty()) {
//            CombatEntry lastEntry = tracker.entries.get(tracker.entries.size() - 1);
//            System.out.println(lastEntry);
//            if (lastEntry.source().is(BrutalityDamageTypes.DEMATERIALIZE)) {
//                tracker.entries.clear();
//                return true;
//            }
//        }
//        return instance.getBoolean(v); // Default behavior
//    }
}