package net.goo.brutality.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.config.BrutalityCommonConfig;
import net.goo.brutality.item.curios.charm.Sine;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.magic.SpellCastingHandler;
import net.goo.brutality.magic.spells.celestia.HolyMantleSpell;
import net.goo.brutality.network.ClientboundSyncCapabilitiesPacket;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModAttributes;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.ModTags;
import net.goo.brutality.util.SealUtils;
import net.goo.brutality.util.helpers.tooltip.BrutalityTooltipHelper;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Comparator;
import java.util.List;

import static net.goo.brutality.item.curios.charm.Gluttony.SOULS;
import static net.goo.brutality.item.curios.charm.Greed.GREED_BONUS;
import static net.goo.brutality.item.curios.charm.Sum.SUM_DAMAGE;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class LivingEntityHurtHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity victim = event.getEntity();
        Entity attacker = event.getSource().getEntity();
        final float[] modifiedAmount = {event.getAmount()};

        handleHurt(event, victim, modifiedAmount);
        if (victim instanceof Player victimPlayer) {
            handlePlayerHurt(event, victimPlayer, modifiedAmount);
        }
        if (attacker != null) {
            handleHurtWithSource(victim, attacker);
            if (attacker instanceof LivingEntity livingSource) {
                handleHurtFromLiving(victim, livingSource, modifiedAmount);
                if (attacker instanceof Player playerSource) {
                    handleHurtFromPlayer(victim, playerSource, modifiedAmount);
                }
            }
        }


        event.setAmount(Math.max(0, modifiedAmount[0]));
    }

    public static void handlePlayerHurt(LivingHurtEvent event, Player victim, float[] modifiedAmount) {
        Level level = victim.level();

        // DULL KNIFE SAD EFFECT
        if (victim.hasEffect(BrutalityModMobEffects.SAD.get())) {
            int foodLevel = victim.getFoodData().getFoodLevel();
            if (foodLevel > 0) {
                float foodReduction = modifiedAmount[0] / 2;
                victim.getFoodData().setFoodLevel((int) Math.max(0, foodLevel - foodReduction));
                modifiedAmount[0] = foodReduction;
            }
        }

        CuriosApi.getCuriosInventory(victim).ifPresent(handler -> {
            // HEART OF GOLD
            if (handler.isEquipped(BrutalityModItems.HEART_OF_GOLD.get())) {
                float toHeal = modifiedAmount[0] * 0.25F;
                float maxPossible = Math.max(victim.getMaxHealth() - victim.getAbsorptionAmount(), 0);
                float finalToHeal = Math.min(toHeal, maxPossible);
                DelayedTaskScheduler.queueServerWork(level, 1, () -> victim.setAbsorptionAmount(victim.getAbsorptionAmount() + finalToHeal));
            }


            if (handler.isEquipped(BrutalityModItems.NANOMACHINES.get())) {
                DamageSource source = event.getSource();
                if (source.is(DamageTypeTags.BYPASSES_ARMOR)) return;
                if (source.is(DamageTypeTags.IS_PROJECTILE) ||
                        source.is(DamageTypes.MOB_ATTACK) ||
                        source.is(DamageTypes.PLAYER_ATTACK) ||
                        source.is(DamageTypes.MOB_ATTACK_NO_AGGRO)) {
                    modifiedAmount[0] = Math.max(0, modifiedAmount[0] / 2);
                }
            }

            if (handler.isEquipped(BrutalityModItems.SUBTRACTION.get())) {
                modifiedAmount[0] = Math.max(0, modifiedAmount[0] - 3);
            }

            if (handler.isEquipped(BrutalityModItems.DIVISION.get())) {
                modifiedAmount[0] = Math.max(0, modifiedAmount[0] / 1.1F);
            }

            handler.findFirstCurio(BrutalityModItems.SUM.get()).ifPresent(slot -> {
                float damageStored = slot.stack().getOrCreateTag().getFloat(SUM_DAMAGE);
                slot.stack().getOrCreateTag().putFloat(SUM_DAMAGE, Math.min(150, damageStored + Math.min(10, modifiedAmount[0] / 2)));
            });


            if (handler.isEquipped(BrutalityModItems.BLOOD_ORB.get())) {
                SpellCastingHandler.addMana(victim, modifiedAmount[0] * 7.5F);
            }


            if (!victim.hasEffect(BrutalityModMobEffects.ENRAGED.get()))
                if (handler.isEquipped(item -> item.is(ModTags.Items.RAGE_ITEMS))) {
                    victim.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP).ifPresent(cap -> {
                        float flatRageGain = modifiedAmount[0];

                        flatRageGain *= (float) victim.getAttributeValue(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get());
                        flatRageGain *= BrutalityCommonConfig.RAGE_GAIN_MULTIPLIER.get();
                        cap.incrementRageAndTrigger(Math.max(0, flatRageGain), victim);
                    });
                }
        });


    }

    public static void handleHurt(LivingHurtEvent event, LivingEntity victim, float[] modifiedAmount) {
        AttributeInstance attributeInstance = victim.getAttribute(BrutalityModAttributes.DAMAGE_TAKEN.get());
        if (attributeInstance != null) {
            modifiedAmount[0] = (float) (modifiedAmount[0] * attributeInstance.getValue());
        }

        // HOLY MANTLE SPELL EFFECT
        if (victim.hasEffect(BrutalityModMobEffects.GRACE.get())) {
            MobEffectInstance effectInstance = victim.getEffect(BrutalityModMobEffects.GRACE.get());
            if (effectInstance != null &&
                    modifiedAmount[0] <= effectInstance.getAmplifier() *
                            BrutalitySpell.getStat(HolyMantleSpell.class, BrutalityTooltipHelper.SpellStatComponents.DEFENSE).levelDelta()) {

                event.setCanceled(true);
                victim.removeEffect(BrutalityModMobEffects.GRACE.get());
            }
        }

        CuriosApi.getCuriosInventory(victim).ifPresent(handler -> {

            // BOILING BLOOD
            if (handler.isEquipped(BrutalityModItems.BOILING_BLOOD.get())) {
                if (victim.hasEffect(BrutalityModMobEffects.ENRAGED.get())) {
                    modifiedAmount[0] *= 1.25F;
                }
            }

            if (handler.isEquipped(BrutalityModItems.BEAD_OF_LIFE.get())) {
                victim.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 2));
            }


        });

    }

    public static void handleHurtWithSource(LivingEntity victim, Entity source) {

    }

    public static void handleHurtFromLiving(LivingEntity victim, LivingEntity source, float[] modifiedAmount) {
        Level level = victim.level();

        // SEALS
        victim.getArmorSlots().forEach(stack -> {
            SealUtils.SEAL_TYPE sealType = SealUtils.getSealType(stack);
            SealUtils.handleSealProcDefensive(victim.level(), source, victim, sealType);
        });

        CuriosApi.getCuriosInventory(source).ifPresent(handler -> {
            // SAD_UVOGRE =======================================================================================================
            if (handler.isEquipped(BrutalityModItems.SAD_UVOGRE.get())) {
                if (victim.getBbHeight() < source.getBbHeight()) {
                    modifiedAmount[0] *= 1.25F;
                }
            }

            // GREED ============================================================================================================
            handler.findFirstCurio(BrutalityModItems.GREED.get()).ifPresent(slot ->
                    modifiedAmount[0] *= 1 + slot.stack().getOrCreateTag().getInt(GREED_BONUS) * 0.01F);


            // CROWBAR ==========================================================================================================
            if (handler.isEquipped(BrutalityModItems.CROWBAR.get())) {
                if (victim.getHealth() / victim.getMaxHealth() > 0.9F) {
                    modifiedAmount[0] *= 1.4F;
                }
            }


            // DUELING GLOVE ====================================================================================================
            if (handler.isEquipped(BrutalityModItems.DUELING_GLOVE.get())) {
                LivingEntity nearestEntity = level.getNearestEntity(
                        LivingEntity.class, TargetingConditions.DEFAULT, source, source.getX(), source.getY(), source.getZ(), source.getBoundingBox().inflate(100));

                if (nearestEntity == victim) {
                    modifiedAmount[0] *= 1.5F;
                }
            }


            // OLD GUILLOTINE ===================================================================================================
            if (handler.isEquipped(BrutalityModItems.OLD_GUILLOTINE.get())) {
                if (victim.getHealth() <= 5) {
                    victim.kill();
                    return;
                }
            }


            // BLOOD PULSE GAUNTLETS ============================================================================================
            if (handler.isEquipped(BrutalityModItems.BLOOD_PULSE_GAUNTLETS.get())) {
                if (source.hasEffect(BrutalityModMobEffects.ENRAGED.get())) {
                    source.level().explode(source, source.damageSources().explosion(source, null),
                            null, victim.position(), 0.25F, false, Level.ExplosionInteraction.MOB
                    );
                }
            }


            // THE OATH =========================================================================================================
            if (handler.isEquipped(BrutalityModItems.THE_OATH.get())) {
                victim.invulnerableTime = 0;
                victim.hurt(victim.damageSources().indirectMagic(source, null), modifiedAmount[0] * 0.1F);
            }


            // SUM CHARM ========================================================================================================
            handler.findFirstCurio(BrutalityModItems.SUM.get()).ifPresent(slot -> {
                float damageStored = slot.stack().getOrCreateTag().getFloat(SUM_DAMAGE);
                modifiedAmount[0] += damageStored;
                slot.stack().getOrCreateTag().putFloat(SUM_DAMAGE, 0);
            });


            // SINE CHARM =======================================================================================================
            if (level instanceof ServerLevel serverLevel)
                if (handler.isEquipped(BrutalityModItems.SINE.get())) {
                    modifiedAmount[0] += Sine.getCurrentBonus(serverLevel);
                }


            // GLUTTONY =========================================================================================================
            handler.findFirstCurio(BrutalityModItems.GLUTTONY.get()).ifPresent(slot ->
                    modifiedAmount[0] += slot.stack().getOrCreateTag().getInt(SOULS) * 0.01F);


            // CROWN OF TYRANNY =================================================================================================
            if (handler.isEquipped(BrutalityModItems.CROWN_OF_TYRANNY.get())) {
                float missingHealthRatio = victim.getHealth() / victim.getMaxHealth();
                modifiedAmount[0] *= (1 + (1 - missingHealthRatio)) * 0.75F;
            }


            // PORTABLE QUANTUM THINGAMABOB =====================================================================================
            if (handler.isEquipped(BrutalityModItems.PORTABLE_QUANTUM_THINGAMABOB.get())) {
                source.addEffect(new MobEffectInstance(TerramityModMobEffects.MIRRORING.get(), 20));
            }

            // GASTRONOMY EFFECT HANDLER
            handleGastronomy(handler, victim);
        });


        CuriosApi.getCuriosInventory(victim).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityModItems.YATA_NO_KAGAMI.get())) {
                float negated = modifiedAmount[0] * 0.15F;
                modifiedAmount[0] = Math.max(0, modifiedAmount[0] - negated);
                source.hurt(source.damageSources().indirectMagic(victim, null), negated);
            }


            if (handler.isEquipped(BrutalityModItems.BLOODSTAINED_MIRROR.get())) {
                source.hurt(source.damageSources().indirectMagic(victim, null), modifiedAmount[0] * 0.1F);
            }


            if (handler.isEquipped(BrutalityModItems.DUELING_GLOVE.get())) {
                List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, victim.getBoundingBox().inflate(100),
                        e -> e != victim && !(e instanceof ArmorStand));
                LivingEntity nearestEntity = nearbyEntities.stream()
                        .min(Comparator.comparingDouble(e -> e.distanceToSqr(victim)))
                        .orElse(null);
                if (nearestEntity != source) {
                    modifiedAmount[0] *= 1.5F;
                }
            }


        });


    }

    public static void handleGastronomy(@NotNull ICuriosItemHandler handler, LivingEntity victim) {
        int fridgeMult = 1;
        if (handler.isEquipped(BrutalityModItems.SMART_FRIDGE.get())) {
            fridgeMult = 3;
        } else if (handler.isEquipped(BrutalityModItems.FRIDGE.get())) {
            fridgeMult = 2;
        }
        // ======================================================================================================================

        if (handler.isEquipped(BrutalityModItems.PIZZA_SLOP.get())) {
            victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SLICKED.get(), 120 * fridgeMult, 1, false, false));
        }

        if (handler.isEquipped(BrutalityModItems.THE_SMOKEHOUSE.get())) {
            victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SMOKED.get(), 80 * fridgeMult, 1, false, false));
        }

        if (handler.isEquipped(BrutalityModItems.EXTRA_VIRGIN_OLIVE_OIL.get())) {
            victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.OILED.get(), 120 * fridgeMult, 3));
        }

        if (handler.isEquipped(BrutalityModItems.CARAMEL_CRUNCH_MEDALLION.get())) {
            victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CANDIED.get(), 80 * fridgeMult, 1));
            victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CARAMELIZED.get(), 80 * fridgeMult, 1));
        }

        if (handler.isEquipped(BrutalityModItems.DUNKED_DONUT.get())) {
            victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.GLAZED.get(), 80 * fridgeMult, 1));
            victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SPRINKLED.get(), 80 * fridgeMult, 1));
        }

        if (handler.isEquipped(BrutalityModItems.LOLLIPOP_OF_ETERNITY.get())) {
            victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.GLAZED.get(), 120 * fridgeMult, 2));
            victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CARAMELIZED.get(), 120 * fridgeMult, 2));
            victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SPRINKLED.get(), 120 * fridgeMult, 2));
            victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CANDIED.get(), 120 * fridgeMult, 2));
        }

        if (handler.isEquipped(BrutalityModItems.SALT_AND_PEPPER.get())) {
            victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SALTED.get(), 120 * fridgeMult, 1));
            victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.PEPPERED.get(), 120 * fridgeMult, 1));
        }

    }

    public static void handleHurtFromPlayer(LivingEntity victim, Player source, float[] modifiedAmount) {
        handleOmnivamp(source, modifiedAmount);
        handleCombo(victim, source, modifiedAmount);
        handleRage(source, modifiedAmount);


    }

    public static void handleRage(Player source, float[] modifiedAmount) {
        if (!source.hasEffect(BrutalityModMobEffects.ENRAGED.get())) {
            CuriosApi.getCuriosInventory(source).ifPresent(handler -> {
                if (handler.isEquipped(stack -> stack.is(ModTags.Items.RAGE_ITEMS))) {
                    source.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP).ifPresent(cap -> {
                        float flatRageGain = modifiedAmount[0];
                        flatRageGain *= (float) source.getAttributeValue(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get());
                        flatRageGain *= BrutalityCommonConfig.RAGE_GAIN_MULTIPLIER.get();
                        cap.incrementRageAndTrigger(Math.max(0, flatRageGain), source);
                    });
                }
            });
        }
    }

    public static void handleCombo(LivingEntity victim, Player source, float[] modifiedAmount) {
        source.getCapability(BrutalityCapabilities.PLAYER_COMBO_CAP).ifPresent(cap -> {
            if (cap.lastVictimId() == victim.getId() && System.currentTimeMillis() - cap.lastHitTime() < 5000) {
                cap.setHitCount((cap.hitCount() + 1) % 15);
            } else {
                cap.setHitCount(1);
            }

            cap.setLastVictimId(victim.getId());
            cap.setLastHitTime(System.currentTimeMillis());
            PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(source.getId(), source));


            CuriosApi.getCuriosInventory(source).ifPresent(handler ->
                    handler.findFirstCurio(BrutalityModItems.EXPONENTIAL_CHARM.get()).ifPresent(slot -> {
                        modifiedAmount[0] = (float) Math.pow(modifiedAmount[0], (0.01 * cap.hitCount()) + 1);
                    }));
        });
    }

    public static void handleOmnivamp(Player attacker, float[] modifiedAmount) {
        AttributeInstance omnivampAttr = attacker.getAttribute(BrutalityModAttributes.OMNIVAMP.get());
        if (omnivampAttr != null) {
            attacker.heal(((float) (modifiedAmount[0] * (omnivampAttr.getValue() - 1))));
        }
    }


}
