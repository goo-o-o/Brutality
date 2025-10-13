package net.goo.brutality.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.goo.brutality.item.base.BrutalityThrowingItem;
import net.goo.brutality.item.curios.charm.Cosine;
import net.goo.brutality.item.weapon.axe.RhittaAxe;
import net.goo.brutality.item.weapon.hammer.AtomicJudgementHammer;
import net.goo.brutality.item.weapon.hammer.JackpotHammer;
import net.goo.brutality.item.weapon.hammer.WoodenRulerHammer;
import net.goo.brutality.item.weapon.scythe.DarkinScythe;
import net.goo.brutality.item.weapon.scythe.FallenScythe;
import net.goo.brutality.item.weapon.sword.MetalRulerSword;
import net.goo.brutality.item.weapon.sword.MurasamaSword;
import net.goo.brutality.item.weapon.sword.ShadowstepSword;
import net.goo.brutality.item.weapon.sword.SupernovaSword;
import net.goo.brutality.item.weapon.sword.phasesaber.BasePhasesaber;
import net.goo.brutality.magic.SpellCastingHandler;
import net.goo.brutality.mob_effect.gastronomy.IGastronomyEffect;
import net.goo.brutality.registry.*;
import net.goo.brutality.util.ModTags;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.SealUtils;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {


    @Unique
    private static Map<Class<? extends Item>, Supplier<ParticleOptions>> PARTICLE_SUPPLIERS = Map.of(
            FallenScythe.class, BrutalityModParticles.SOUL_SWEEP_PARTICLE::get,
            ShadowstepSword.class, BrutalityModParticles.SHADOW_SWEEP_PARTICLE::get,
            MurasamaSword.class, BrutalityModParticles.MURASAMA_SWEEP_PARTICLE::get,
            SupernovaSword.class, BrutalityModParticles.SUPERNOVA_SWEEP_PARTICLE::get
    );

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Unique
    private static Optional<ParticleOptions> brutality$getParticleForItem(Item item) {
        return PARTICLE_SUPPLIERS.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(item))
                .findFirst()
                .map(entry -> entry.getValue().get());
    }

    @Shadow
    public abstract void remove(Entity.RemovalReason pReason);

    @Inject(method = "sweepAttack", at = @At("HEAD"), cancellable = true)
    private void sweepAttackParticle(CallbackInfo ci) {
        Player player = (Player) (Object) this;

        if (player.level() instanceof ServerLevel world) {
            if (player.getAttackStrengthScale(0.5F) >= 1.0F) {
                double d0 = -Math.sin(Math.toRadians(player.getYRot())) * 2;
                double d1 = Math.cos(Math.toRadians(player.getYRot())) * 2;

                brutality$getParticleForItem(player.getMainHandItem().getItem())
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
        originalDamage -= (float) player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
        originalDamage = (float) ModUtils.computeAttributes(player, stack, originalDamage);
        originalDamage += (float) player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);

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

        Level level = level();

        AttributeInstance lifestealAttr = playerAttacker.getAttribute(ModAttributes.LIFESTEAL.get());
        if (lifestealAttr != null) {
            playerAttacker.heal((float) (modifiedAmount[0] * (lifestealAttr.getValue() - 1)));
        }

        AttributeInstance stunChanceAttr = playerAttacker.getAttribute(ModAttributes.STUN_CHANCE.get());
        if (stunChanceAttr != null) {
            float chance = ModUtils.getSyncedSeededRandom(playerAttacker).nextFloat(0, 1);
            if (chance < (playerAttacker.getAttributeValue(ModAttributes.STUN_CHANCE.get()) - 1)) {
                livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.STUNNED.get(), 1));
            }
        }


        CuriosApi.getCuriosInventory(playerAttacker).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityModItems.KNUCKLE_WRAPS.get())) {
                ModUtils.modifyEffect(playerAttacker, BrutalityModMobEffects.PRECISION.get(),
                        new ModUtils.ModValue(80, true), new ModUtils.ModValue(3, false),
                        null, livingEntity -> livingEntity.addEffect(new MobEffectInstance(BrutalityModMobEffects.PRECISION.get(), 80, 0)), null);

            }


            if (handler.isEquipped(BrutalityModItems.MORTAR_AND_PESTLE_CHARM.get())) {
                if (item.getDefaultInstance().is(ModTags.Items.GASTRONOMIST_ITEMS))
                    livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.PULVERIZED.get(), 3, 3, false, true));
            }

            if (handler.isEquipped(BrutalityModItems.MANA_SYRINGE.get())) {
                SpellCastingHandler.addMana(playerAttacker, modifiedAmount[0] * 0.25F);
            }


            handler.findFirstCurio(BrutalityModItems.PLUNDER_CHEST_CHARM.get()).ifPresent(slot -> {
                if (!playerAttacker.getCooldowns().isOnCooldown(slot.stack().getItem())) {
                    int chance = playerAttacker.hasEffect(MobEffects.LUCK) ? 10 + playerAttacker.getEffect(MobEffects.LUCK).getAmplifier() : 10;
                    if (level.random.nextIntBetweenInclusive(0, 100) <= chance) {

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

            handler.findFirstCurio(BrutalityModItems.BUTTER_GAUNTLETS.get()).ifPresent(slot -> {
                if (item.getDefaultInstance().is(ModTags.Items.GASTRONOMIST_ITEMS)) {
                    if (!playerAttacker.getCooldowns().isOnCooldown(slot.stack().getItem())) {
//                    System.out.println("Not on cd");
                        int chance = playerAttacker.hasEffect(MobEffects.LUCK) ? 10 + playerAttacker.getEffect(MobEffects.LUCK).getAmplifier() : 10;
                        if (level.random.nextIntBetweenInclusive(0, 100) <= chance) {
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
            handler.findFirstCurio(BrutalityModItems.BRAIN_ROT.get()).ifPresent(slot -> {
                livingVictim.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0));
            });

            handler.findFirstCurio(BrutalityModItems.RAGE_STONE.get()).ifPresent(slot -> {
                modifiedAmount[0] *= 1.3F;
            });
            handler.findFirstCurio(BrutalityModItems.BLOOD_STONE.get()).ifPresent(slot -> {
                modifiedAmount[0] *= 1.3F;
                playerAttacker.addEffect(new MobEffectInstance(TerramityModMobEffects.LIFESTEAL.get(), 30, 0));
            });

            handler.findFirstCurio(BrutalityModItems.VAMPIRIC_TALISMAN.get()).ifPresent(slot -> {
                if (!playerAttacker.getCooldowns().isOnCooldown(slot.stack().getItem())) {
                    playerAttacker.heal(modifiedAmount[0] * 0.5F);
                    playerAttacker.getCooldowns().addCooldown(slot.stack().getItem(), 80);
                }
            });

        });

        if (stack.is(ModTags.Items.GASTRONOMIST_ITEMS)) {
//            System.out.println("Entered Tag check");
            float scoredBoost = 0, mashedBoost = 0;
            // Start with 0 multiplier

            if (livingVictim.hasEffect(BrutalityModMobEffects.SCORED.get())) // For each spellLevel of Scored and Mash the target has, add 0.25 to the boost
                scoredBoost = 0.15F * Objects.requireNonNull(livingVictim.getEffect(BrutalityModMobEffects.SCORED.get())).getAmplifier();
            if (livingVictim.hasEffect(BrutalityModMobEffects.MASHED.get()))
                mashedBoost = 0.15F * Objects.requireNonNull(livingVictim.getEffect(BrutalityModMobEffects.MASHED.get())).getAmplifier();
            // Use the boost value to boost the multiplier, so like spellLevel 3 scored would make scored 0.6 and spellLevel 2 mashed would make mashed 0.45 mashed
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

        if (item instanceof ShadowstepSword) {
            if (ModUtils.isPlayerBehind(playerAttacker, livingVictim, 30)) {
                modifiedAmount[0] *= 7;
            }
        } else if (item instanceof BasePhasesaber) {
            return livingVictim.hurt(damageSources().indirectMagic(playerAttacker, null), modifiedAmount[0]);
        } else if (item instanceof RhittaAxe) {
            modifiedAmount[0] += RhittaAxe.computeAttackDamageBonus(level);
        } else if (item instanceof JackpotHammer) {
            modifiedAmount[0] += level.random.nextInt(-5, 11);
            brutality$handleJackpotEffects(playerAttacker, livingVictim, modifiedAmount[0]);
        } else if (item instanceof WoodenRulerHammer || item instanceof MetalRulerSword) {
            modifiedAmount[0] *= livingVictim.getBbHeight() * 0.75F;
        } else if (item instanceof MurasamaSword) {
            livingVictim.invulnerableTime = 0;
            modifiedAmount[0] /= 2;
            livingVictim.hurt(playerAttacker.damageSources().indirectMagic(playerAttacker, null), modifiedAmount[0]);
            livingVictim.invulnerableTime = 0;

        } else if (item instanceof DarkinScythe) {
            if (ModUtils.getTextureIdx(stack) == 1) {
                livingVictim.invulnerableTime = 0;
                float magicDamage = modifiedAmount[0] / 4;
                livingVictim.hurt(playerAttacker.damageSources().indirectMagic(playerAttacker, null), magicDamage);
            } else if (ModUtils.getTextureIdx(stack) == 2) {
                playerAttacker.heal(modifiedAmount[0] * 0.2F);
            }
        } else if (item instanceof AtomicJudgementHammer) {
            AtomicJudgementHammer.doExplosion(playerAttacker, livingVictim.getPosition(1).add(0, livingVictim.getBbHeight() / 2, 0));
        }

        SealUtils.handleSealProc(level, playerAttacker, livingVictim.getPosition(1).add(0, livingVictim.getBbHeight() * 0.5F, 0), stack);


        return livingVictim.hurt(pSource, modifiedAmount[0]);
    }


    @Unique
    private void brutality$handleJackpotEffects(Player player, LivingEntity target, float damage) {
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

    @ModifyReturnValue(
            method = "getCurrentItemAttackStrengthDelay",
            at = @At("RETURN")
    )
    private float modifyAttackSpeed(float originalDelay) {
        Player player = (Player) (Object) this;

        if (player.isHolding(BrutalityModItems.VAMPIRE_KNIVES.get())) return 5;

        float modifiedDelay = originalDelay;

        if (CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(BrutalityModItems.COSINE_CHARM.get()).isPresent())
                .orElse(false)) {

            float bonus = Cosine.getCurrentBonus(player.level());
            modifiedDelay /= (1.0f + bonus);
        }

        if (player.getMainHandItem().getItem() instanceof BrutalityThrowingItem || player.getOffhandItem().getItem() instanceof BrutalityThrowingItem) {
            if (player.getMainHandItem().getItem() == player.getOffhandItem().getItem()) {
                modifiedDelay /= 2;
            }
        }

        return modifiedDelay;
    }

//    @ModifyVariable(
//            method = "attack(Lnet/minecraft/world/entity/Entity;)V",
//            at = @At(
//                    value = "STORE",
//                    ordinal = 1
//            ),
//            name = "flag2"
//    )
//    private boolean modifyFlag2(boolean flag2) {
//        Player self = (Player) (Object) this;
//        boolean flag = self.getAttackStrengthScale(0.5F) > 0.9;
//        float critChance = (float) self.getAttributeValue(ModAttributes.CRITICAL_STRIKE_CHANCE.get());
//        if (self.onGround()) {
//            ICuriosItemHandler handler = CuriosApi.getCuriosInventory(self).orElse(null);
//            if (handler.isEquipped(BrutalityModItems.LUCKY_INSOLES.get())) {
//                critChance += 0.2F;
//            }
//        }
//        boolean crit = ModUtils.getSyncedSeededRandom(self).nextFloat() < (critChance - 1);
//        return flag && crit;
//    }

//    @Redirect(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
//    private boolean redirect$dropEquipment(GameRules instance, GameRules.Key<GameRules.BooleanValue> v) {
//        Player player = (((Player) (Object) this));
//        CombatTracker tracker = player.getCombatTracker();
//        CombatEntry latestEntry = tracker.entries.get(tracker.entries.size() - 1);
//
//        return latestEntry.source().is(BrutalityDamageTypes.DEMATERIALIZE);
//    }
//
//    @Redirect(method = "getExperienceReward", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
//    private boolean redirect$getExperienceReward(GameRules instance, GameRules.Key<GameRules.BooleanValue> v) {
//        Player player = (((Player) (Object) this));
//        CombatTracker tracker = player.getCombatTracker();
//        CombatEntry latestEntry = tracker.entries.get(tracker.entries.size() - 1);
//
//        return latestEntry.source().is(BrutalityDamageTypes.DEMATERIALIZE);
//    }

}





