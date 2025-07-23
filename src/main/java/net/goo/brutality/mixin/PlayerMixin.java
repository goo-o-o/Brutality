package net.goo.brutality.mixin;

import net.goo.brutality.event.mod.client.BrutalityModParticleFactories;
import net.goo.brutality.item.weapon.axe.RhittaAxe;
import net.goo.brutality.item.weapon.hammer.JackpotHammer;
import net.goo.brutality.item.weapon.hammer.WoodenRulerHammer;
import net.goo.brutality.item.weapon.scythe.DarkinScythe;
import net.goo.brutality.item.weapon.sword.MetalRulerSword;
import net.goo.brutality.item.weapon.sword.MurasamaSword;
import net.goo.brutality.item.weapon.sword.ShadowstepSword;
import net.goo.brutality.mob_effect.gastronomy.IGastronomyEffect;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.ModTags;
import net.goo.brutality.util.ModUtils;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.goo.brutality.util.ModResources.CUSTOM_MODEL_DATA;
import static net.goo.brutality.util.helpers.AttributeHelper.ATTACK_DAMAGE_BONUS;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Shadow
    public abstract void remove(Entity.RemovalReason pReason);

    @Shadow public abstract Iterable<ItemStack> getArmorSlots();

    @Inject(method = "sweepAttack", at = @At("HEAD"), cancellable = true)
    private void sweepAttackParticle(CallbackInfo ci) {
        // Cast 'this' to Player
        Player player = (Player) (Object) this;

        if (player.level() instanceof ServerLevel world) {
            if (player.getAttackStrengthScale(0.5F) >= 1.0F) {
                double d0 = -Math.sin(Math.toRadians(player.getYRot())) * 2;
                double d1 = Math.cos(Math.toRadians(player.getYRot())) * 2;

                BrutalityModParticleFactories.getParticleForItem(player.getMainHandItem().getItem())
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
    private boolean handleWeaponAttacks(Entity entity, DamageSource pSource, float pAmount) {
        if (!(entity instanceof LivingEntity livingVictim)) {
            return entity.hurt(pSource, pAmount);
        }


        Player playerAttacker = (Player) pSource.getEntity();
        if (playerAttacker == null) return entity.hurt(pSource, pAmount);

        ItemStack stack = playerAttacker.getMainHandItem();
        Item item = stack.getItem();
        float[] modifiedAmount = new float[]{pAmount};


        if (item instanceof ShadowstepSword) {
            if (ModUtils.isPlayerBehind(playerAttacker, livingVictim, 30)) {
                modifiedAmount[0] *= 5;
            }
        } else if (item instanceof RhittaAxe) {
            modifiedAmount[0] += RhittaAxe.computeAttackDamageBonus(playerAttacker.level());
        } else if (item instanceof JackpotHammer) {
            modifiedAmount[0] = playerAttacker.level().random.nextInt(4, 14);
            armament$handleJackpotEffects(playerAttacker, livingVictim, modifiedAmount[0]);
        } else if (item instanceof WoodenRulerHammer || item instanceof MetalRulerSword) {
            modifiedAmount[0] *= livingVictim.getBbHeight();
        } else if (item instanceof MurasamaSword) {
            livingVictim.invulnerableTime = 0;
            modifiedAmount[0] /= 2;
            livingVictim.hurt(playerAttacker.damageSources().indirectMagic(playerAttacker, playerAttacker), modifiedAmount[0]);
            livingVictim.invulnerableTime = 0;

        } else if (item instanceof DarkinScythe) {
            if (stack.getOrCreateTag().getInt(CUSTOM_MODEL_DATA) == 1) {
                livingVictim.invulnerableTime = 0;
                float magicDamage = modifiedAmount[0] / 4;
                livingVictim.hurt(playerAttacker.damageSources().indirectMagic(playerAttacker, playerAttacker), magicDamage);
            } else if (stack.getOrCreateTag().getInt(CUSTOM_MODEL_DATA) == 2) {
                playerAttacker.heal(modifiedAmount[0] * 0.2F);
            }
        }

        CuriosApi.getCuriosInventory(playerAttacker).ifPresent(handler -> {
            handler.findFirstCurio(BrutalityModItems.MORTAR_AND_PESTLE_CHARM.get()).ifPresent(slot -> {
                if (item.getDefaultInstance().is(ModTags.Items.GASTRONOMIST_ITEMS))
                    livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.PULVERIZED.get(), 3, 3, false, true));
            });

            handler.findFirstCurio(BrutalityModItems.PLUNDER_CHEST_CHARM.get()).ifPresent(slot -> {
                if (!playerAttacker.getCooldowns().isOnCooldown(slot.stack().getItem())) {
                    int chance = playerAttacker.hasEffect(MobEffects.LUCK) ? 10 + playerAttacker.getEffect(MobEffects.LUCK).getAmplifier() : 10;
                    if (playerAttacker.level().random.nextIntBetweenInclusive(0, 100) <= chance) {

                        Optional<MobEffectInstance> randomEffect = livingVictim.getActiveEffects().stream().filter(effect -> effect.getEffect().isBeneficial())
                                .collect(Collectors.collectingAndThen(Collectors.toList(),
                                        list -> {
                                            if (list.isEmpty()) return Optional.empty();
                                            return Optional.of(list.get(livingVictim.getRandom().nextInt(list.size())));
                                        }
                                ));

                        randomEffect.ifPresent(effect -> {
                            livingVictim.removeEffect(effect.getEffect());
                            playerAttacker.addEffect(effect);
                            playerAttacker.playSound(BrutalityModSounds.TREASURE_CHEST_LOCK.get(), 1.5F, Mth.nextFloat(playerAttacker.getRandom(), 0.8F, 1.2F));
                            playerAttacker.getCooldowns().addCooldown(slot.stack().getItem(), 100);

                        });

                    }
                }
            });

            handler.findFirstCurio(BrutalityModItems.BUTTER_GAUNTLETS_HANDS.get()).ifPresent(slot -> {
                if (item.getDefaultInstance().is(ModTags.Items.GASTRONOMIST_ITEMS)) {
                    if (!playerAttacker.getCooldowns().isOnCooldown(slot.stack().getItem())) {
//                    System.out.println("Not on cd");
                        int chance = playerAttacker.hasEffect(MobEffects.LUCK) ? 10 + playerAttacker.getEffect(MobEffects.LUCK).getAmplifier() : 10;
                        if (playerAttacker.level().random.nextIntBetweenInclusive(0, 100) <= chance) {
//                            System.out.println("Rolled successfully, adding effect");
                            livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.STUNNED.get(), 4, 0, false, true));
                            playerAttacker.getCooldowns().addCooldown(slot.stack().getItem(), 60);

                        }
                    }
                }

            });

            //===========================================================================================//
            // GASTRONOMY START

            final int[] fridgeMult = {1};

            handler.findFirstCurio(BrutalityModItems.FRIDGE_CHARM.get()).ifPresent(slot -> {
                fridgeMult[0] = 2;
            });
            handler.findFirstCurio(BrutalityModItems.SMART_FRIDGE_CHARM.get()).ifPresent(slot -> {
                fridgeMult[0] = 3;
            });

            handler.findFirstCurio(BrutalityModItems.SALT_SHAKER_CHARM.get()).ifPresent(slot -> {
                livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SALTED.get(), 60 * fridgeMult[0], 0));
            });
            handler.findFirstCurio(BrutalityModItems.PEPPER_SHAKER_CHARM.get()).ifPresent(slot -> {
                livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.PEPPERED.get(), 60 * fridgeMult[0], 0));
            });
            handler.findFirstCurio(BrutalityModItems.CHEESE_SAUCE_CHARM.get()).ifPresent(slot -> {
                livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SLICKED.get(), 60 * fridgeMult[0], 0));
            });
            handler.findFirstCurio(BrutalityModItems.TOMATO_SAUCE_CHARM.get()).ifPresent(slot -> {
                livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SLICKED.get(), 60 * fridgeMult[0], 0));
            });
            handler.findFirstCurio(BrutalityModItems.SMOKE_STONE.get()).ifPresent(slot -> {
                livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SMOKED.get(), 60 * fridgeMult[0], 0));
            });
            handler.findFirstCurio(BrutalityModItems.BAMBOO_STEAMER.get()).ifPresent(slot -> {
                livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.STEAMED.get(), 60 * fridgeMult[0], 0));
            });
            handler.findFirstCurio(BrutalityModItems.SUGAR_GLAZE.get()).ifPresent(slot -> {
                livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.GLAZED.get(), 60 * fridgeMult[0], 0));
            });
            handler.findFirstCurio(BrutalityModItems.RAINBOW_SPRINKLES.get()).ifPresent(slot -> {
                livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SPRINKLED.get(), 60 * fridgeMult[0], 0));
            });
            handler.findFirstCurio(BrutalityModItems.ROCK_CANDY_RING.get()).ifPresent(slot -> {
                livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CANDIED.get(), 60 * fridgeMult[0], 0));
            });
            handler.findFirstCurio(BrutalityModItems.SEARED_SUGAR_BROOCH.get()).ifPresent(slot -> {
                livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CARAMELIZED.get(), 60 * fridgeMult[0], 0));
            });

            handler.findFirstCurio(BrutalityModItems.OLIVE_OIL_CHARM.get()).ifPresent(slot -> {
                livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.OILED.get(), 60 * fridgeMult[0], 1));
            });
            // GASTRONOMY END
            //===========================================================================================//


            handler.findFirstCurio(BrutalityModItems.ZOMBIE_HEART.get()).ifPresent(slot -> {
                livingVictim.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, 0));
            });
            handler.findFirstCurio(BrutalityModItems.SLOTH_CHARM.get()).ifPresent(slot -> {
                livingVictim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 0));
                livingVictim.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20, 0));
                livingVictim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 0));
            });
            handler.findFirstCurio(BrutalityModItems.BRAIN_ROT_HEAD.get()).ifPresent(slot -> {
                livingVictim.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0));
            });

            handler.findFirstCurio(BrutalityModItems.RAGE_STONE.get()).ifPresent(slot -> {
                modifiedAmount[0] *= 1.3F;
            });
            handler.findFirstCurio(BrutalityModItems.BLOOD_STONE.get()).ifPresent(slot -> {
                modifiedAmount[0] *= 1.3F;
                playerAttacker.addEffect(new MobEffectInstance(TerramityModMobEffects.LIFESTEAL.get(), 30, 0));
            });


        });

        if (stack.is(ModTags.Items.GASTRONOMIST_ITEMS)) {
            System.out.println("Entered Tag check");
            float scoredBoost = 0, mashedBoost = 0;
            // Start with 0 multiplier

            if (livingVictim.hasEffect(BrutalityModMobEffects.SCORED.get())) // For each level of Scored and Mash the target has, add 0.25 to the boost
                scoredBoost = 0.15F * Objects.requireNonNull(livingVictim.getEffect(BrutalityModMobEffects.SCORED.get())).getAmplifier();
            if (livingVictim.hasEffect(BrutalityModMobEffects.MASHED.get()))
                mashedBoost = 0.15F * Objects.requireNonNull(livingVictim.getEffect(BrutalityModMobEffects.MASHED.get())).getAmplifier();
            // Use the boost value to boost the multiplier, so like level 3 scored would make scored 0.6 and level 2 mashed would make mashed 0.45 mashed
            float scoredMultiplier = 1.05F + scoredBoost;
            float mashedMultiplier = 1.05F + mashedBoost;

            for (MobEffectInstance instance : livingVictim.getActiveEffects()) {
                MobEffect effect = instance.getEffect();
                if (effect instanceof IGastronomyEffect gastro) {
                    if (gastro.modifiesDamage()) {
                        float scale = gastro.baseMultiplier();

                        if (gastro.scalesWithLevel()) {
                            scale *= 1 + (gastro.multiplierPerLevel() * (instance.getAmplifier() + 1));
                        }

                        float typeMultiplier = 1F;
                        switch (gastro.getType()) {
                            case DRY -> typeMultiplier = scoredMultiplier;
                            case WET -> typeMultiplier = mashedMultiplier;
                            case BOTH -> typeMultiplier = (scoredMultiplier + mashedMultiplier) / 2F;
                        }

                        modifiedAmount[0] *= 1 + (scale * typeMultiplier);
                    }

                    gastro.applyEffect(playerAttacker, livingVictim, instance.getAmplifier());
                }
            }


        }

        return livingVictim.hurt(pSource, modifiedAmount[0]);
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
                    ModUtils.getRandomSound(BrutalityModSounds.JACKPOT_SOUNDS),
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


