//package net.goo.brutality.event;
//
//import net.goo.brutality.Brutality;
//import net.goo.brutality.entity.capabilities.EntityCapabilities;
//import net.goo.brutality.event.forge.DelayedTaskScheduler;
//import net.goo.brutality.item.weapon.hammer.AtomicJudgementHammer;
//import net.goo.brutality.item.weapon.hammer.TerratonHammer;
//import net.goo.brutality.item.weapon.lance.EventHorizonLance;
//import net.goo.brutality.network.PacketHandler;
//import net.goo.brutality.network.s2cSyncCapabilitiesPacket;
//import net.goo.brutality.registry.BrutalityCapabilities;
//import net.goo.brutality.registry.BrutalityModItems;
//import net.goo.brutality.registry.BrutalityModMobEffects;
//import net.goo.brutality.registry.ModAttributes;
//import net.goo.brutality.util.ModTags;
//import net.goo.brutality.util.ModUtils;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.tags.DamageTypeTags;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.damagesource.DamageTypes;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.AttributeInstance;
//import net.minecraft.world.entity.ai.targeting.TargetingConditions;
//import net.minecraft.world.entity.decoration.ArmorStand;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.event.entity.living.LivingDeathEvent;
//import net.minecraftforge.event.entity.living.LivingFallEvent;
//import net.minecraftforge.event.entity.living.LivingHurtEvent;
//import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import top.theillusivec4.curios.api.CuriosApi;
//import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
//
//import java.util.*;
//
//import static net.goo.brutality.item.curios.charm.Gluttony.SOULS;
//import static net.goo.brutality.item.curios.charm.Sum.SUM_DAMAGE;
//
//@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
//public class LivingEntityEventHandler {
//
//    public static class AttackCombo {
//        public UUID victimId;
//        public int hitCount;
//        public long lastHitTime;
//    }
//
//    public static final Map<UUID, AttackCombo> attackCombos = new HashMap<>();
//
//    @SubscribeEvent
//    public static void onLivingHurt(LivingHurtEvent event) {
//        if (event.getEntity().level().isClientSide) return;
//
//        Entity victim = event.getEntity();
//        Entity attacker = event.getSource().getEntity();
//        Level level = victim.level();
//        final float[] damage = {event.getAmount()};
//
//        if (victim instanceof LivingEntity livingVictim) {
//            if (attacker instanceof LivingEntity livingAttacker) {
//                if (attacker instanceof Player playerAttacker) {
//                    // Attacker is Player
//                    UUID attackerId = playerAttacker.getUUID();
//                    UUID victimId = victim.getUUID();
//
//                    AttackCombo combo = attackCombos.get(attackerId);
//
//                    if (combo != null && combo.victimId.equals(victimId)) {
//                        // Existing Combo
//                        long currentTime = System.currentTimeMillis();
//                        if (currentTime - combo.lastHitTime < 5000) { // 5 seconds
//                            combo.hitCount = (combo.hitCount + 1) % 15;
//                        } else {
//                            combo.hitCount = 1;
//                        }
//                        combo.lastHitTime = currentTime;
//                    } else {
//                        // New Combo
//                        combo = new AttackCombo();
//                        combo.victimId = victimId;
//                        combo.hitCount = 1;
//                        combo.lastHitTime = System.currentTimeMillis();
//                        attackCombos.put(attackerId, combo);
//                    }
//
//                    AttackCombo finalCombo = combo;
//
//                    CuriosApi.getCuriosInventory(playerAttacker).ifPresent(handler -> {
//
//                        handler.findFirstCurio(BrutalityModItems.EXPONENTIAL_CHARM.get()).ifPresent(slot -> {
////                        System.out.println("BEFORE: " + damage[0]);
//                            damage[0] = (float) Math.pow(damage[0], (0.01 * finalCombo.hitCount) + 1);
////                        System.out.println("AFTER: " + damage[0]);
//                        });
//                    });
//
//                }
//
//
//                // Attacker is Living Entity
//                CuriosApi.getCuriosInventory(livingAttacker).ifPresent(handler -> {
//                    handler.findFirstCurio(BrutalityModItems.DUELING_GLOVE_HANDS.get()).ifPresent(slot -> {
//                        LivingEntity nearestEntity = level.getNearestEntity(
//                                LivingEntity.class, TargetingConditions.DEFAULT, livingAttacker, livingAttacker.getX(), livingAttacker.getY(), livingAttacker.getZ(), livingAttacker.getBoundingBox().inflate(100));
//
//                        if (nearestEntity == victim) {
////                            System.out.println("Nearest entity is the victim");
//                            damage[0] *= 1.5F;
//                        }
//                    });
//                    handler.findFirstCurio(BrutalityModItems.BLOOD_PULSE_GAUNTLETS.get()).ifPresent(slot -> {
//                        if (livingAttacker.hasEffect(BrutalityModMobEffects.ENRAGED.get())) {
//                            livingAttacker.level().explode(livingAttacker, livingAttacker.damageSources().explosion(livingAttacker, null),
//                                    null, livingVictim.position(), 0.25F, false, Level.ExplosionInteraction.MOB
//                            );
//                        }
//                    });
//
//                    handler.findFirstCurio(BrutalityModItems.SUM_CHARM.get()).ifPresent(slot -> {
//                        float damageStored = slot.stack().getOrCreateTag().getFloat(SUM_DAMAGE);
//                        damage[0] += damageStored;
//                        slot.stack().getOrCreateTag().putFloat(SUM_DAMAGE, 0);
//                    });
//
//                    //===========================================================================================//
//                    // GASTRONOMY START
//
//                    final int[] fridgeMult = {1};
//
//                    handler.findFirstCurio(BrutalityModItems.FRIDGE_CHARM.get()).ifPresent(slot -> {
//                        fridgeMult[0] = 2;
//                    });
//                    handler.findFirstCurio(BrutalityModItems.SMART_FRIDGE_CHARM.get()).ifPresent(slot -> {
//                        fridgeMult[0] = 3;
//                    });
//
//                    handler.findFirstCurio(BrutalityModItems.PIZZA_SLOP_CHARM.get()).ifPresent(slot -> {
//                        livingVictim.addEffect(new
//                                MobEffectInstance(BrutalityModMobEffects.SLICKED.get(), 120 * fridgeMult[0], 1, false, false));
//                    });
//                    handler.findFirstCurio(BrutalityModItems.THE_SMOKEHOUSE.get()).ifPresent(slot -> {
//                        livingVictim.addEffect(new
//                                MobEffectInstance(BrutalityModMobEffects.SMOKED.get(), 80 * fridgeMult[0], 1, false, false));
//                    });
//                    handler.findFirstCurio(BrutalityModItems.EXTRA_VIRGIN_OLIVE_OIL_CHARM.get()).ifPresent(slot -> {
//                        livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.OILED.get(), 120 * fridgeMult[0], 3));
//                    });
//                    handler.findFirstCurio(BrutalityModItems.CARAMEL_CRUNCH_MEDALLION.get()).ifPresent(slot -> {
//                        livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CANDIED.get(), 80 * fridgeMult[0], 1));
//                        livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CARAMELIZED.get(), 80 * fridgeMult[0], 1));
//                    });
//                    handler.findFirstCurio(BrutalityModItems.DUNKED_DONUT.get()).ifPresent(slot -> {
//                        livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.GLAZED.get(), 80 * fridgeMult[0], 1));
//                        livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SPRINKLED.get(), 80 * fridgeMult[0], 1));
//                    });
//                    handler.findFirstCurio(BrutalityModItems.LOLLIPOP_OF_ETERNITY.get()).ifPresent(slot -> {
//                        livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.GLAZED.get(), 120 * fridgeMult[0], 2));
//                        livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CARAMELIZED.get(), 120 * fridgeMult[0], 2));
//                        livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SPRINKLED.get(), 120 * fridgeMult[0], 2));
//                        livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CANDIED.get(), 120 * fridgeMult[0], 2));
//                    });
//                    handler.findFirstCurio(BrutalityModItems.SALT_AND_PEPPER_CHARM.get()).ifPresent(slot -> {
//                        livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SALTED.get(), 120 * fridgeMult[0], 1));
//                        livingVictim.addEffect(new MobEffectInstance(BrutalityModMobEffects.PEPPERED.get(), 120 * fridgeMult[0], 1));
//                    });
//                    // GASTRONOMY END
//                    //===========================================================================================//
//
//
//                });
//
//
//                if (livingVictim.hasEffect(BrutalityModMobEffects.STONEFORM.get())) {
//                    int amp = Objects.requireNonNull(livingVictim.getEffect(BrutalityModMobEffects.STONEFORM.get())).getAmplifier();
//                    damage[0] *= (1 - 0.1F * (amp + 1F));
//                }
//
//
//                // For victim LivingEntity
//                CuriosApi.getCuriosInventory(livingVictim).ifPresent(handler -> {
//
//                    handler.findFirstCurio(BrutalityModItems.DUELING_GLOVE_HANDS.get()).ifPresent(slot -> {
//
//                        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, livingVictim.getBoundingBox().inflate(100),
//                                e -> e != livingVictim && !(e instanceof ArmorStand));
//
//                        LivingEntity nearestEntity = nearbyEntities.stream()
//                                .min(Comparator.comparingDouble(e -> e.distanceToSqr(livingVictim)))
//                                .orElse(null);
//
////                        System.out.println("NEARBY ENTITIES: " + nearbyEntities);
////                        System.out.println("NEAREST: " + nearestEntity);
////                        System.out.println("ATTACKER: " + livingAttacker);
//
//                        if (nearestEntity != livingAttacker) {
//                            damage[0] *= 1.5F;
//                        }
//                    });
//
//
//                });
//
//
//                if (victim instanceof Player victimPlayer) {
//                    // If Victim is Player
//                    if (victimPlayer.hasEffect(BrutalityModMobEffects.SAD.get())) {
//                        int foodLevel = victimPlayer.getFoodData().getFoodLevel();
//                        if (foodLevel > 0) {
//                            float foodReduction = damage[0] / 2;
//                            victimPlayer.getFoodData().setFoodLevel((int) Math.max(0, foodLevel - foodReduction));
//                            damage[0] = foodReduction;
//                        }
//                    }
//
//
//                    CuriosApi.getCuriosInventory(victimPlayer).ifPresent(handler -> {
//
//
//                        handler.findFirstCurio(BrutalityModItems.HEART_OF_GOLD.get()).ifPresent(slot -> {
//                            if (victimPlayer.getAbsorptionAmount() == 0)
//                                DelayedTaskScheduler.queueServerWork(1, () ->
//                                        victimPlayer.setAbsorptionAmount(victimPlayer.getAbsorptionAmount() + damage[0] / 2));
//                        });
//
//
//                        handler.findFirstCurio(BrutalityModItems.NANOMACHINES_HANDS.get()).ifPresent(slot -> {
//                            DamageSource source = event.getSource();
//                            if (source.is(DamageTypeTags.BYPASSES_ARMOR)) return;
//
//                            if (source.is(DamageTypeTags.IS_PROJECTILE) ||
//                                    source.is(DamageTypes.MOB_ATTACK) ||
//                                    source.is(DamageTypes.PLAYER_ATTACK) ||
//                                    source.is(DamageTypes.MOB_ATTACK_NO_AGGRO)) {
//                                damage[0] = Math.max(0, damage[0] / 2);
//                            }
//                        });
//
//
//                        handler.findFirstCurio(BrutalityModItems.SUBTRACTION_CHARM.get()).ifPresent(slot -> {
//                            damage[0] = Math.max(0, damage[0] - 2);
//                        });
//
//                        handler.findFirstCurio(BrutalityModItems.DIVISION_CHARM.get()).ifPresent(slot -> {
//                            damage[0] = Math.max(0, damage[0] / 1.1F);
//                        });
//
//                        handler.findFirstCurio(BrutalityModItems.SUM_CHARM.get()).ifPresent(slot -> {
//                            float damageStored = slot.stack().getOrCreateTag().getFloat(SUM_DAMAGE);
//                            slot.stack().getOrCreateTag().putFloat(SUM_DAMAGE, Math.min(150, damageStored + Math.min(10, damage[0] / 2)));
//                        });
//
//                        handler.findFirstCurio(BrutalityModItems.YATA_NO_KAGAMI.get()).ifPresent(slot -> {
//                            float negated = damage[0] * 0.25F;
//                            damage[0] = Math.max(0, damage[0] - negated);
//                            livingAttacker.hurt(livingAttacker.damageSources().indirectMagic(victimPlayer, livingAttacker), negated);
//                        });
//
//                        if (!victimPlayer.hasEffect(BrutalityModMobEffects.ENRAGED.get()))
//                            if (!handler.findCurios(stack -> stack.is(ModTags.Items.RAGE_ITEMS)).isEmpty()) {
//                                victimPlayer.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP).ifPresent(cap -> {
//                                    // Before modification
//                                    float rageGain = damage[0];
//
//                                    AttributeInstance rageGainAttr = victimPlayer.getAttribute(ModAttributes.RAGE_GAIN_MULTIPLIER.get());
//                                    if (rageGainAttr != null) rageGain *= (float) rageGainAttr.getValue();
//
//                                    cap.incrementRage(Math.max(0, rageGain));
//                                    tryTriggerRage(victimPlayer, handler, cap);
//                                });
//                            }
//                    });
//                }
//            }
//        }
//
//        event.setAmount(Math.max(0, damage[0]));
//
//        if (event.getSource().getEntity() instanceof Player attackerPlayer) {
//            if (!attackerPlayer.hasEffect(BrutalityModMobEffects.ENRAGED.get()))
//                CuriosApi.getCuriosInventory(attackerPlayer).ifPresent(handler -> {
//                    if (!handler.findCurios(stack -> stack.is(ModTags.Items.RAGE_ITEMS)).isEmpty()) {
//                        attackerPlayer.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP).ifPresent(cap -> {
//                            float damageDealt = damage[0]; // Final amount dealt
//                            float rageGain = damageDealt / 5F;
//
//                            AttributeInstance rageGainAttr = attackerPlayer.getAttribute(ModAttributes.RAGE_GAIN_MULTIPLIER.get());
//                            if (rageGainAttr != null) rageGain *= (float) rageGainAttr.getValue();
//
//                            cap.incrementRage(Math.max(0, rageGain));
//                            tryTriggerRage(attackerPlayer, handler, cap);
//                        });
//                    }
//                });
//        }
//
//    }
//
//    @SubscribeEvent
//    public static void onLivingKnockback(LivingKnockBackEvent event) {
//        LivingEntity victim = event.getEntity();
//        Entity lastAttacker = victim.getLastAttacker();
//        if (lastAttacker instanceof Player player) {
//            Item item = player.getMainHandItem().getItem();
//
//            if (item instanceof TerratonHammer) {
//                float kbMult = ((float) Math.pow(player.getAttackStrengthScale(0.5F), 3));
//
//                event.setStrength((event.getOriginalStrength() * 10) * kbMult);
//
//            }
//
//            if (item instanceof EventHorizonLance) {
//                event.setCanceled(true);
//
//                Vec3 direction = lastAttacker.getPosition(1f).subtract(victim.getPosition(1F)).normalize();
//
//                victim.addDeltaMovement(direction.scale(1.2));
//
//            }
//        }
//
//    }
//
//    @SubscribeEvent
//    public static void onLivingFall(LivingFallEvent event) {
//        LivingEntity entity = event.getEntity();
//        if (entity instanceof Player player && !player.level().isClientSide()) {
//            CuriosApi.getCuriosInventory(entity).ifPresent(handler -> {
//                handler.findFirstCurio(BrutalityModItems.CELESTIAL_STARBOARD.get()).ifPresent(slotResult ->
//                        event.setCanceled(true));
//            });
//
//            if (player.getMainHandItem().getItem() instanceof AtomicJudgementHammer) {
//                event.setDamageMultiplier(0.15F);
//                if (event.getDistance() > 10 && !player.level().isClientSide() && player.isShiftKeyDown()) {
//                    AtomicJudgementHammer.doCustomExplosion(player.level(), player, player, player.getMainHandItem());
//                }
//            }
//
//
//        }
//    }
//
//    @SubscribeEvent
//    public static void onLivingDeath(LivingDeathEvent event) {
//        if (event.getSource().getEntity() instanceof Player causingPlayer) {
//            CuriosApi.getCuriosInventory(causingPlayer).ifPresent(handler -> {
//                handler.findFirstCurio(BrutalityModItems.GLUTTONY_CHARM.get()).ifPresent(slot -> {
//                            slot.stack().getOrCreateTag().putInt(SOULS, slot.stack().getOrCreateTag().getInt(SOULS) + 1);
//                        }
//                );
//
//                handler.findFirstCurio(BrutalityModItems.RAMPAGE_CLOCK.get()).ifPresent(slot -> {
//                            ModUtils.tryExtendEffect(causingPlayer, BrutalityModMobEffects.ENRAGED.get(), 20);
//                        }
//                );
//
//            });
//        }
//
//        if (event.getEntity() instanceof Player victimPlayer) {
//            if (victimPlayer.hasEffect(BrutalityModMobEffects.ENRAGED.get())) {
//                CuriosApi.getCuriosInventory(victimPlayer).ifPresent(handler -> {
//                    handler.findFirstCurio(BrutalityModItems.GRUDGE_TOTEM.get()).ifPresent(slot -> {
//                        event.setCanceled(true);
//                        victimPlayer.setHealth(0.5F);
//                    });
//                });
//            }
//        }
//    }
//
//    public static void tryTriggerRage(Player player, ICuriosItemHandler handler, EntityCapabilities.PlayerRageCap cap) {
//        int maxRage = (int) player.getAttributeValue(ModAttributes.MAX_RAGE.get());
//
//        if (cap.rageValue() >= maxRage) {
//            // Don't trigger if wearing Anger Management
//            if (handler.findFirstCurio(BrutalityModItems.ANGER_MANAGEMENT.get()).isEmpty()) {
//                int duration = 40;
//                int rageLevel = (int) Math.floor(cap.rageValue() / 100);
//                AttributeInstance rageTimeAttr = player.getAttribute(ModAttributes.RAGE_TIME_MULTIPLIER.get());
//                if (rageTimeAttr != null) {
//                    duration = (int) (duration * rageTimeAttr.getValue());
//                }
//                AttributeInstance rageLevelAttr = player.getAttribute(ModAttributes.RAGE_LEVEL.get());
//                if (rageLevelAttr != null) {
//                    rageLevel += (int) rageLevelAttr.getValue();
//                }
//
//                player.addEffect(new MobEffectInstance(BrutalityModMobEffects.ENRAGED.get(), duration, rageLevel, false, true));
//                cap.setRageValue(0);
//
//                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1F, 1F);
//            }
//        }
//
//        cap.setRageValue(Math.min(cap.rageValue(), maxRage));
//        PacketHandler.sendToAllClients(new s2cSyncCapabilitiesPacket(player.getId(), player));
//    }
//}
