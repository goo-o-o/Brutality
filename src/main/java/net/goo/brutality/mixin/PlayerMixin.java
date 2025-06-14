package net.goo.brutality.mixin;

import net.goo.brutality.event.BrutalityModParticlesHandler;
import net.goo.brutality.item.weapon.custom.*;
import net.goo.brutality.registry.ModSounds;
import net.goo.brutality.util.ModUtils;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.goo.brutality.util.helpers.AttributeHelper.ATTACK_DAMAGE_BONUS;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "sweepAttack", at = @At("HEAD"), cancellable = true)
    private void onSweepAttack(CallbackInfo ci) {
        // Cast 'this' to Player
        Player player = (Player) (Object) this;

        if (player.level() instanceof ServerLevel world) {
            if (player.getAttackStrengthScale(0.5F) >= 1.0F) {
                double d0 = -Math.sin(Math.toRadians(player.getYRot())) * 2;
                double d1 = Math.cos(Math.toRadians(player.getYRot())) * 2;

                BrutalityModParticlesHandler.getParticleForItem(player.getMainHandItem().getItem())
                        .ifPresent(particle -> {
                            world.sendParticles(
                                    particle,
                                    player.getX() + d0,
                                    player.getY(0.5D),
                                    player.getZ() + d1,
                                    0, d0, 0.0D, d1, 0.0D
                            );

                            ci.cancel();

                        });
            }


        }
    }


    @ModifyVariable(
            method = "attack(Lnet/minecraft/world/entity/Entity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F",
                    shift = At.Shift.BEFORE
            ),
            ordinal = 0 // Targets the first 'float' local variable (which is 'f')
    )

    private float modifyAttackDamage(float originalDamage) {
        Player player = (Player) (Object) this;
        ItemStack stack = player.getMainHandItem();

        if (stack.getOrCreateTag().contains(ATTACK_DAMAGE_BONUS)) {
            return originalDamage + stack.getOrCreateTag().getFloat(ATTACK_DAMAGE_BONUS);
        }
        return originalDamage;
    }

    @Redirect(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            )
    )
    private boolean handleWeaponAttacks(Entity instance, DamageSource pSource, float pAmount) {
        if (!(instance instanceof LivingEntity livingTarget)) {
            return instance.hurt(pSource, pAmount);
        }

        Player player = (Player) pSource.getEntity();
        ItemStack item = player.getMainHandItem();
        float modifiedAmount = pAmount;


        // Shadowstep Dagger - Backstab
        if (item.getItem() instanceof ShadowstepDagger) {
            if (ModUtils.isPlayerBehind(player, livingTarget, 30)) {
                modifiedAmount *= 5;
            }
        }
        // Rhitta Axe - Bonus Damage
        else if (item.getItem() instanceof RhittaAxe) {
            modifiedAmount += RhittaAxe.computeAttackDamageBonus(player.level());
        }
        // Jackpot Hammer - Random Damage
        else if (item.getItem() instanceof JackpotHammer) {
            modifiedAmount = player.level().random.nextInt(4, 14);
            armament$handleJackpotEffects(player, livingTarget, modifiedAmount);
        }
        // HF Murasama - Armor Pierce
        else if (item.getItem() instanceof HFMurasamaSword) {
            livingTarget.invulnerableTime = 0;
            modifiedAmount /= 2;
            livingTarget.hurt(player.damageSources().indirectMagic(player, player), modifiedAmount);
            livingTarget.invulnerableTime = 0;
        }
        return livingTarget.hurt(pSource, modifiedAmount);
    }

    @Unique
    private void armament$handleJackpotEffects(Player player, LivingEntity target, float damage) {
        Level level = player.level();

        if (damage > 8) {
            int msgPicker = level.random.nextInt(3);
            String[] messages = {
                    "§a§l§oCHA-CHING! §r",
                    "§b§l§oHUGE WIN! §r",
                    "§c§l§oJACKPOT! §r"
            };
            player.displayClientMessage(Component.literal(messages[msgPicker] + damage + " damage"), true);

            level.playSound(null, target.getX(), target.getY(), target.getZ(),
                    ModUtils.getRandomSound(ModSounds.JACKPOT_SOUNDS),
                    SoundSource.PLAYERS, 1F, Mth.nextFloat(level.random, 0.5f, 1F));

            if (level instanceof ServerLevel serverLevel) {
                        List<SimpleParticleType> particleTypes = List.of(
                                TerramityModParticleTypes.COIN_DISPENSED.get(),
                                TerramityModParticleTypes.POKER_BLUE.get(),
                                TerramityModParticleTypes.POKER_BLACK.get(),
                                TerramityModParticleTypes.POKER_GREEN.get(),
                                TerramityModParticleTypes.POKER_RED.get(),
                                TerramityModParticleTypes.POKER_YELLOW.get(),
                                TerramityModParticleTypes.DICE_PARTICLE.get()
                        );
                for (SimpleParticleType particle : particleTypes) {
                    serverLevel.sendParticles(particle,
                            target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(),
                            10, 0.5, 0.5, 0.5, level.random.nextFloat());
                }
            }
        } else {
            player.displayClientMessage(Component.literal(damage + " damage"), true);
        }
    }
}


