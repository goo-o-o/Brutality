//package net.goo.brutality.mixin;
//
//import com.llamalad7.mixinextras.sugar.Local;
//import net.bettercombat.api.AttackHand;
//import net.bettercombat.api.WeaponAttributes;
//import net.bettercombat.network.Packets;
//import net.goo.brutality.item.weapon.sword.Apollo;
//import net.goo.brutality.item.weapon.sword.Erebus;
//import net.goo.brutality.registry.BrutalityModParticles;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.server.network.ServerGamePacketListenerImpl;
//import net.minecraft.world.effect.MobEffects;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(targets = "net.bettercombat.network.ServerNetwork")
//public abstract class BetterCombatServerNetworkMixin {
//
//    @Inject(
//            method = "lambda$initializeHandlers$5", // or use full signature
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/server/level/ServerPlayer;attack(Lnet/minecraft/world/entity/Entity;)V"
//            ),
//            require = 1
//    )
//    private static void onBetterCombatAttack(
//            ServerPlayer player, Packets.C2S_AttackRequest request, WeaponAttributes attributes, WeaponAttributes.Attack attack, AttackHand hand, ServerLevel world, boolean useVanillaPacket, ServerGamePacketListenerImpl handler, CallbackInfo ci, @Local(argsOnly = true) ServerPlayer serverPlayer, @Local(ordinal = 0) Entity target
//    ) {
//        if (hand != null) {
//            ItemStack stack = hand.itemStack();
//            Item item = stack.getItem();
//            if (target instanceof LivingEntity livingTarget) {
//                if (item instanceof Erebus) {
//                    if (livingTarget.hasEffect(MobEffects.GLOWING)) {
//                        modifiedAmount[0] *= 2F;
//                        if (level instanceof ServerLevel serverLevel)
//                            serverLevel.sendParticles(BrutalityModParticles.YANG_PARTICLE.get(), entity.getX(), entity.getY(0.5), entity.getZ(), 10, 1, 1, 1, 0.15);
//                    }
//                } else if (item instanceof Apollo) {
//                    if (livingVictim.hasEffect(MobEffects.DARKNESS)) {
//                        modifiedAmount[0] *= 2F;
//                        if (level instanceof ServerLevel serverLevel)
//                            serverLevel.sendParticles(BrutalityModParticles.YIN_PARTICLE.get(), entity.getX(), entity.getY(0.5), entity.getZ(), 10, 1, 1, 1, 0.15);
//                    }
//                }
//            }
//        }
//    }
//}