package net.goo.brutality.mixin.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.goo.brutality.item.base.BrutalityThrowingItem;
import net.goo.brutality.item.curios.charm.Cosine;
import net.goo.brutality.item.weapon.axe.RhittaAxe;
import net.goo.brutality.item.weapon.hammer.AtomicJudgementHammer;
import net.goo.brutality.item.weapon.hammer.JackpotHammer;
import net.goo.brutality.item.weapon.hammer.WoodenRulerHammer;
import net.goo.brutality.item.weapon.scythe.DarkinScythe;
import net.goo.brutality.item.weapon.sword.*;
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
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {


    @Unique
    private static final Map<Class<? extends Item>, Supplier<ParticleOptions>> PARTICLE_SUPPLIERS = Map.of(
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

    @Shadow
    public abstract void attack(Entity pTarget);

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

        Optional<ICuriosItemHandler> handlerOptional = CuriosApi.getCuriosInventory(player).resolve();
        if (handlerOptional.isPresent()) {
            ICuriosItemHandler handler = handlerOptional.get();
            if (!stack.isEmpty() && handler.isEquipped(BrutalityModItems.SUSPICIOUSLY_LARGE_HANDLE.get())) {
                float attackSpeed = (float) player.getAttributeValue(Attributes.ATTACK_SPEED);
                float difference = attackSpeed - 0.5F;
                float damageBoost = difference * 5F;
                originalDamage += damageBoost;
            }
        }

        return originalDamage;
    }


    @ModifyReturnValue(
            method = "getCurrentItemAttackStrengthDelay",
            at = @At("RETURN")
    )
    private float modifyAttackSpeed(float originalDelay) {
        Player player = (Player) (Object) this;

        if (player.isHolding(BrutalityModItems.VAMPIRE_KNIVES.get())) return 5;

        float modifiedDelay = originalDelay;

        Optional<ICuriosItemHandler> handler = CuriosApi.getCuriosInventory(player).resolve();
        if (handler.isPresent()) {
            if (handler.get().isEquipped(BrutalityModItems.COSINE.get())) {
                float bonus = Cosine.getCurrentBonus(player.level());
                modifiedDelay /= (1.0f + bonus);
            }
        }

        if (player.getMainHandItem().getItem() instanceof BrutalityThrowingItem || player.getOffhandItem().getItem() instanceof BrutalityThrowingItem) {
            if (player.getMainHandItem().getItem() == player.getOffhandItem().getItem()) {
                modifiedDelay /= 2;
            }
        }

        if (!this.getMainHandItem().isEmpty() && handler.isPresent()) {
            if (handler.get().isEquipped(BrutalityModItems.SUSPICIOUSLY_LARGE_HANDLE.get())) {
                return (float) (1.0D / 0.5F * 20.0D);
            }
        }

        return modifiedDelay;
    }


    // Replace the entire @Redirect method body with this cleaned version
    @Redirect(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            )
    )
    private boolean handleWeaponAttacks(Entity entity, DamageSource pSource, float pAmount) {
        if (!(entity instanceof LivingEntity victim)) return entity.hurt(pSource, pAmount);

        Player attacker = (Player) pSource.getEntity();
        if (attacker == null) return entity.hurt(pSource, pAmount);

        ItemStack stack = ModUtils.getAttackStack(attacker);
        Item item = stack.getItem();
        Level level = level();

        float[] modifiedAmount = {pAmount};

        brutality$handlePlayerAttackAttributes(attacker, victim, modifiedAmount);
        brutality$handlePlayerAttackCurios(attacker, victim, item, level, modifiedAmount);
        brutality$handleGastronomistWeapon(attacker, victim, stack, modifiedAmount);

        boolean earlyReturn = brutality$handleSpecificWeapon(attacker, victim, item, stack, level, pSource, modifiedAmount);

        if (earlyReturn) return true;

        SealUtils.handleSealProcOffensive(level, attacker, victim.getPosition(1).add(0, victim.getBbHeight() * 0.5F, 0), stack);

        return victim.hurt(pSource, modifiedAmount[0]);
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


    @Unique
    private static void brutality$handlePlayerAttackAttributes(Player attacker, LivingEntity victim, float[] modifiedAmount) {
        AttributeInstance lifestealAttr = attacker.getAttribute(BrutalityModAttributes.LIFESTEAL.get());
        if (lifestealAttr != null) {
            attacker.heal(modifiedAmount[0] * (float) (lifestealAttr.getValue() - 1));
        }

        AttributeInstance stunChanceAttr = attacker.getAttribute(BrutalityModAttributes.STUN_CHANCE.get());
        if (stunChanceAttr != null) {
            float chance = ModUtils.getSyncedSeededRandom(attacker).nextFloat();
            if (chance < attacker.getAttributeValue(BrutalityModAttributes.STUN_CHANCE.get()) - 1) {
                victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.STUNNED.get(), 1));
            }
        }
    }

    @Unique
    private static void brutality$handlePlayerAttackCurios(Player attacker, LivingEntity victim, Item item, Level level, float[] modifiedAmount) {
        CuriosApi.getCuriosInventory(attacker).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityModItems.KNUCKLE_WRAPS.get())) {
                ModUtils.modifyEffect(attacker, BrutalityModMobEffects.PRECISION.get(),
                        new ModUtils.ModValue(80, true), new ModUtils.ModValue(3, false),
                        null, living -> living.addEffect(new MobEffectInstance(BrutalityModMobEffects.PRECISION.get(), 80, 0)), null);
            }

            if (handler.isEquipped(BrutalityModItems.MORTAR_AND_PESTLE.get())) {
                if (item.getDefaultInstance().is(ModTags.Items.GASTRONOMIST_ITEMS)) {
                    victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.PULVERIZED.get(), 3, 3, false, true));
                }
            }

            if (handler.isEquipped(BrutalityModItems.MANA_SYRINGE.get())) {
                SpellCastingHandler.addMana(attacker, modifiedAmount[0] * 0.25F);
            }

            handler.findFirstCurio(BrutalityModItems.PLUNDER_CHEST.get()).ifPresent(slot -> {
                if (!attacker.getCooldowns().isOnCooldown(slot.stack().getItem())) {
                    int chance = attacker.hasEffect(MobEffects.LUCK) ? 10 + attacker.getEffect(MobEffects.LUCK).getAmplifier() : 10;
                    if (level.random.nextIntBetweenInclusive(0, 100) <= chance) {
                        Optional<MobEffectInstance> randomEffect = victim.getActiveEffects().stream()
                                .filter(e -> e.getEffect().isBeneficial())
                                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                                    if (list.isEmpty()) return Optional.empty();
                                    return Optional.of(list.get(victim.getRandom().nextInt(list.size())));
                                }));
                        randomEffect.ifPresent(effect -> {
                            victim.removeEffect(effect.getEffect());
                            attacker.addEffect(effect);
                            attacker.playSound(BrutalityModSounds.TREASURE_CHEST_LOCK.get(), 1.5F, Mth.nextFloat(attacker.getRandom(), 0.8F, 1.2F));
                            attacker.getCooldowns().addCooldown(slot.stack().getItem(), 100);
                        });
                    }
                }
            });

            handler.findFirstCurio(BrutalityModItems.BUTTER_GAUNTLETS.get()).ifPresent(slot -> {
                if (item.getDefaultInstance().is(ModTags.Items.GASTRONOMIST_ITEMS) && !attacker.getCooldowns().isOnCooldown(slot.stack().getItem())) {
                    int chance = attacker.hasEffect(MobEffects.LUCK) ? 10 + attacker.getEffect(MobEffects.LUCK).getAmplifier() : 10;
                    if (level.random.nextIntBetweenInclusive(0, 100) <= chance) {
                        victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.STUNNED.get(), 4, 0, false, true));
                        attacker.getCooldowns().addCooldown(slot.stack().getItem(), 60);
                    }
                }
            });

            int fridgeMult = 1;
            if (handler.isEquipped(BrutalityModItems.SMART_FRIDGE.get())) fridgeMult = 3;
            else if (handler.isEquipped(BrutalityModItems.FRIDGE.get())) fridgeMult = 2;

            // Gastronomy applicators
            if (handler.isEquipped(BrutalityModItems.SALT_SHAKER.get()))
                victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SALTED.get(), 60 * fridgeMult, 0));
            if (handler.isEquipped(BrutalityModItems.PEPPER_SHAKER.get()))
                victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.PEPPERED.get(), 60 * fridgeMult, 0));
            if (handler.isEquipped(BrutalityModItems.CHEESE_SAUCE.get()))
                victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SLICKED.get(), 60 * fridgeMult, 0));
            if (handler.isEquipped(BrutalityModItems.TOMATO_SAUCE.get()))
                victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SLICKED.get(), 60 * fridgeMult, 0));
            if (handler.isEquipped(BrutalityModItems.SMOKE_STONE.get()))
                victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SMOKED.get(), 60 * fridgeMult, 0));
            if (handler.isEquipped(BrutalityModItems.BAMBOO_STEAMER.get()))
                victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.STEAMED.get(), 60 * fridgeMult, 0));
            if (handler.isEquipped(BrutalityModItems.SUGAR_GLAZE.get()))
                victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.GLAZED.get(), 60 * fridgeMult, 0));
            if (handler.isEquipped(BrutalityModItems.RAINBOW_SPRINKLES.get()))
                victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SPRINKLED.get(), 60 * fridgeMult, 0));
            if (handler.isEquipped(BrutalityModItems.ROCK_CANDY_RING.get()))
                victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CANDIED.get(), 60 * fridgeMult, 0));
            if (handler.isEquipped(BrutalityModItems.SEARED_SUGAR_BROOCH.get()))
                victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CARAMELIZED.get(), 60 * fridgeMult, 0));
            if (handler.isEquipped(BrutalityModItems.OLIVE_OIL.get()))
                victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.OILED.get(), 60 * fridgeMult, 1));

            // Other curios
            if (handler.isEquipped(BrutalityModItems.ZOMBIE_HEART.get()))
                victim.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, 0));
            if (handler.isEquipped(BrutalityModItems.SLOTH.get())) {
                victim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 0));
                victim.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20, 0));
                victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 0));
            }
            if (handler.isEquipped(BrutalityModItems.BRAIN_ROT.get()))
                victim.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0));
            if (handler.isEquipped(BrutalityModItems.RAGE_STONE.get())) modifiedAmount[0] *= 1.3F;
            if (handler.isEquipped(BrutalityModItems.BLOOD_STONE.get())) {
                modifiedAmount[0] *= 1.3F;
                attacker.addEffect(new MobEffectInstance(TerramityModMobEffects.LIFESTEAL.get(), 30, 0));
            }
            handler.findFirstCurio(BrutalityModItems.VAMPIRIC_TALISMAN.get()).ifPresent(slot -> {
                if (!attacker.getCooldowns().isOnCooldown(slot.stack().getItem())) {
                    attacker.heal(modifiedAmount[0] * 0.5F);
                    attacker.getCooldowns().addCooldown(slot.stack().getItem(), 80);
                }
            });
        });
    }

    @Unique
    private static void brutality$handleGastronomistWeapon(Player attacker, LivingEntity victim, ItemStack stack, float[] modifiedAmount) {
        if (!stack.is(ModTags.Items.GASTRONOMIST_ITEMS)) return;

        float scoredBoost = 0f, mashedBoost = 0f;
        if (victim.hasEffect(BrutalityModMobEffects.SCORED.get()))
            scoredBoost = 0.15F * Objects.requireNonNull(victim.getEffect(BrutalityModMobEffects.SCORED.get())).getAmplifier();
        if (victim.hasEffect(BrutalityModMobEffects.MASHED.get()))
            mashedBoost = 0.15F * Objects.requireNonNull(victim.getEffect(BrutalityModMobEffects.MASHED.get())).getAmplifier();

        float scoredMultiplier = 1.05F + scoredBoost;
        float mashedMultiplier = 1.05F + mashedBoost;

        Optional<ICuriosItemHandler> handler = CuriosApi.getCuriosInventory(attacker).resolve();

        for (MobEffectInstance instance : victim.getActiveEffects()) {
            if (instance.getEffect() instanceof IGastronomyEffect gastro) {
                if (gastro.modifiesDamage()) {
                    float scale = gastro.baseMultiplier();
                    if (gastro.scalesWithLevel())
                        scale *= 1 + gastro.multiplierPerLevel() * (instance.getAmplifier() + 1);

                    float typeMult = switch (gastro.getType()) {
                        case DRY -> scoredMultiplier;
                        case WET -> mashedMultiplier;
                        case BOTH -> (scoredMultiplier + mashedMultiplier) / 2F;
                    };

                    if (handler.isPresent()) {
                        if (handler.get().isEquipped(BrutalityModItems.ICE_CREAM_SANDWICH.get())) {
                            typeMult += 0.2F;
                        }
                    }


                    modifiedAmount[0] *= 1 + scale * typeMult;
                }
                gastro.applyEffect(attacker, victim, instance.getAmplifier());
            }
        }
    }

    @Unique
    private static boolean brutality$handleSpecificWeapon(Player attacker, LivingEntity victim, Item item, ItemStack stack, Level level, DamageSource source, float[] modifiedAmount) {
        if (item instanceof ShadowstepSword && ModUtils.isPlayerBehind(attacker, victim, 30)) {
            modifiedAmount[0] *= 7;
        } else if (item instanceof BasePhasesaber) {
            return victim.hurt(attacker.damageSources().indirectMagic(attacker, attacker), modifiedAmount[0]);
        } else if (item instanceof RhittaAxe) {
            modifiedAmount[0] += RhittaAxe.computeAttackDamageBonus(level);
        } else if (item instanceof JackpotHammer) {
            modifiedAmount[0] += level.random.nextInt(-5, 11);
            ((PlayerMixin) (Object) attacker).brutality$handleJackpotEffects(attacker, victim, modifiedAmount[0]);
        } else if (item instanceof WoodenRulerHammer || item instanceof MetalRulerSword) {
            modifiedAmount[0] *= victim.getBbHeight() * 0.75F;
        } else if (item instanceof MurasamaSword) {
            victim.invulnerableTime = 0;
            modifiedAmount[0] /= 2;
            victim.hurt(attacker.damageSources().indirectMagic(attacker, null), modifiedAmount[0]);
            victim.invulnerableTime = 0;
        } else if (item instanceof DarkinScythe) {
            int tex = ModUtils.getTextureIdx(stack);
            if (tex == 1) {
                victim.invulnerableTime = 0;
                victim.hurt(attacker.damageSources().indirectMagic(attacker, null), modifiedAmount[0] / 4);
            } else if (tex == 2) {
                attacker.heal(modifiedAmount[0] * 0.2F);
            }
        } else if (item instanceof AtomicJudgementHammer) {
            AtomicJudgementHammer.doExplosion(attacker, victim.getPosition(1).add(0, victim.getBbHeight() / 2, 0));
        } else if (item instanceof Tsukuyomi && victim.hasEffect(MobEffects.GLOWING)) {
            modifiedAmount[0] *= 2F;
            if (level instanceof ServerLevel sl)
                sl.sendParticles(BrutalityModParticles.YANG_PARTICLE.get(), victim.getX(), victim.getY(0.5), victim.getZ(), 10, 1, 1, 1, 0.15);
        } else if (item instanceof Amaterasu && victim.hasEffect(MobEffects.DARKNESS)) {
            modifiedAmount[0] *= 2F;
            if (level instanceof ServerLevel sl)
                sl.sendParticles(BrutalityModParticles.YIN_PARTICLE.get(), victim.getX(), victim.getY(0.5), victim.getZ(), 10, 1, 1, 1, 0.15);
        }
        return false; // no early return
    }

}





