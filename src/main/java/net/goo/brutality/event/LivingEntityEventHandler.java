package net.goo.brutality.event;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.capabilities.EntityCapabilities;
import net.goo.brutality.entity.spells.cosmic.StarStreamEntity;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.event.forge.ForgePlayerStateHandler;
import net.goo.brutality.item.BrutalityArmorMaterials;
import net.goo.brutality.item.curios.charm.Sine;
import net.goo.brutality.item.weapon.generic.CreaseOfCreation;
import net.goo.brutality.item.weapon.hammer.AtomicJudgementHammer;
import net.goo.brutality.item.weapon.spear.EventHorizonSpear;
import net.goo.brutality.item.weapon.sword.SupernovaSword;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.magic.IBrutalitySpell;
import net.goo.brutality.magic.SpellCastingHandler;
import net.goo.brutality.magic.SpellCooldownTracker;
import net.goo.brutality.magic.spells.celestia.HolyMantleSpell;
import net.goo.brutality.network.ClientboundSyncCapabilitiesPacket;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.registry.*;
import net.goo.brutality.util.ModTags;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.SealUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static net.goo.brutality.item.curios.charm.Gluttony.SOULS;
import static net.goo.brutality.item.curios.charm.Greed.GREED_BONUS;
import static net.goo.brutality.item.curios.charm.Sum.SUM_DAMAGE;
import static net.goo.brutality.util.helpers.EnvironmentColorManager.resetAllColors;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class LivingEntityEventHandler {

    @SubscribeEvent
    public static void onSpellCast(SpellCastEvent.Post event) {
        Player player = event.getPlayer();
        IBrutalitySpell spell = event.getSpell();
        int level = event.getSpellLevel();
        ItemStack tome = event.getStack();
        float manaCost = IBrutalitySpell.getActualManaCost(player, spell, level);

        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            handler.findFirstCurio(BrutalityModItems.HELLSPEC_TIE.get()).ifPresent(slotResult -> {
                if (spell.getSchool() == IBrutalitySpell.MagicSchool.BRIMWIELDER) {
                    SpellCastingHandler.addMana(player, manaCost * 0.25F);
                }
            });
            handler.findFirstCurio(BrutalityModItems.SOUL_STONE.get()).ifPresent(slotResult -> {
                float chance = ModUtils.getSyncedSeededRandom(player).nextFloat(0, 1);
                if (chance < 0.05F) {
                    SpellCastingHandler.addMana(player, manaCost);
                } else {
                    SpellCastingHandler.addMana(player, manaCost * 0.15F);
                }
            });
        });
    }

    @SubscribeEvent
    public static void onConsumeMana(ConsumeManaEvent event) {
        float amount = event.getAmount();
        int level = event.getSpellLevel();
        IBrutalitySpell spell = event.getSpell();
        Player player = event.getPlayer();

        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            handler.findFirstCurio(BrutalityModItems.ONYX_IDOL.get()).ifPresent(slot -> {
                ItemStack stack = slot.stack();
                CompoundTag tag = stack.getOrCreateTag();
                tag.putFloat("mana", tag.getFloat("mana") + amount);
                if (tag.getFloat("mana") > 200) {
                    SpellCooldownTracker.resetCooldowns(player);
                    tag.putFloat("mana", amount % 200);
                }
            });
        });
    }


    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player newPlayer = event.getEntity();
        ItemCooldowns newCooldowns = newPlayer.getCooldowns();

        newPlayer.getCapability(BrutalityCapabilities.PLAYER_MANA_CAP).ifPresent(cap -> {
            cap.setManaValue((float) newPlayer.getAttributeValue(ModAttributes.MAX_MANA.get()));
        });

        Item diamondBooster = BrutalityModItems.DIAMOND_BOOSTER_PACK.get();
        Item evilKingBooster = BrutalityModItems.EVIL_KING_BOOSTER_PACK.get();

        DelayedTaskScheduler.queueServerWork(event.getEntity().level(), 2, () ->

                newPlayer.getCapability(BrutalityCapabilities.RESPAWN_CAP).ifPresent(cap -> {

                    if (cap.getBoosterType() == EntityCapabilities.RespawnCap.BOOSTER_TYPE.EVIL_KING && !newCooldowns.isOnCooldown(evilKingBooster)) {
                        newCooldowns.addCooldown(evilKingBooster, 20 * 15 * 60);

                        for (int i = 0; i < 6; i++) {
                            newPlayer.addEffect(new MobEffectInstance(ForgePlayerStateHandler.boosterPackEffects.get().get(i), 20 * 5 * 60, 1));
                        }
                    }

                    if (cap.getBoosterType() == EntityCapabilities.RespawnCap.BOOSTER_TYPE.DIAMOND && !newCooldowns.isOnCooldown(diamondBooster)) {
                        newCooldowns.addCooldown(diamondBooster, 20 * 30 * 60);

                        for (int i = 0; i < 5; i++) {
                            newPlayer.addEffect(new MobEffectInstance(ForgePlayerStateHandler.boosterPackEffects.get().get(i), 20 * 120, 1));
                        }
                    }

                    if (cap.getBoosterType() == EntityCapabilities.RespawnCap.BOOSTER_TYPE.SILVER) {
                        for (int i = 0; i < 4; i++) {
                            newPlayer.addEffect(new MobEffectInstance(ForgePlayerStateHandler.boosterPackEffects.get().get(i), 20 * 30, 1));
                        }
                    }

                    if (cap.getBoosterType() != EntityCapabilities.RespawnCap.BOOSTER_TYPE.NONE)
                        cap.setBoosterType(EntityCapabilities.RespawnCap.BOOSTER_TYPE.NONE);
                    if (cap.getKitType() != EntityCapabilities.RespawnCap.KIT_TYPE.NONE)
                        cap.setKitType(EntityCapabilities.RespawnCap.KIT_TYPE.NONE);
                }));
    }

    @SubscribeEvent
    public static void entityVisibility(LivingEvent.LivingVisibilityEvent event) {
        if (ModUtils.hasFullArmorSet(event.getEntity(), BrutalityArmorMaterials.NOIR)) {
            event.modifyVisibility(0);
        }

        if (event.getEntity() instanceof Player player) {
            double visibility = player.getAttributeValue(ModAttributes.ENTITY_VISIBILITY.get());
            event.modifyVisibility(visibility);
        }
    }


    @SubscribeEvent
    public static void onPlayerCrit(CriticalHitEvent event) {
        Player player = event.getEntity();

        boolean flag = player.getAttackStrengthScale(0.5F) > 0.9;
        float critChance = (float) player.getAttributeValue(ModAttributes.CRITICAL_STRIKE_CHANCE.get());

        boolean crit = player.getRandom().nextFloat() < (critChance - 1);


        if (crit && flag) {
            event.setResult(Event.Result.ALLOW);
            event.setDamageModifier((float) event.getEntity().getAttributeValue(ModAttributes.CRITICAL_STRIKE_DAMAGE.get()));

            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                if (handler.isEquipped(BrutalityModItems.FUZZY_DICE.get())) {
                    if (player.getRandom().nextFloat() < (critChance - 1)) {
                        event.setDamageModifier(event.getDamageModifier() * 2F);
                    }
                }
            });

            if (event.getEntity().hasEffect(BrutalityModMobEffects.PRECISION.get())) {
                event.getEntity().removeEffect(BrutalityModMobEffects.PRECISION.get());
            }
        } else {
            event.setResult(Event.Result.DENY);
        }

    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity healed = event.getEntity();

        CuriosApi.getCuriosInventory(healed).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityModItems.HEMOMATIC_LOCKET.get())) {
                healed.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0));
            }
        });
    }

//    @SubscribeEvent
//    public static void onArrowLoose(ArrowLooseEvent event) {
//
//    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (event.getProjectile().getOwner() instanceof LivingEntity owner) {
            event.getProjectile().getCapability(BrutalityCapabilities.SEAL_TYPE_CAP).ifPresent(cap -> {
                Vec3 location;
                if (event.getRayTraceResult() instanceof EntityHitResult entityHitResult) {
                    location = entityHitResult.getEntity().getPosition(1).add(0, entityHitResult.getEntity().getY(0.5F), 0);
                } else {
                    location = event.getRayTraceResult().getLocation();
                }

                SealUtils.handleSealProc(owner.level(), owner, location, cap.getSealType());

            });
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity victim = event.getEntity();
        Entity attacker = event.getSource().getEntity();
        Level level = victim.level();
        final float[] modifiedAmount = {event.getAmount()};

        victim.getArmorSlots().forEach(stack -> {
            SealUtils.SEAL_TYPE sealType = SealUtils.getSealType(stack);
            RandomSource random = level.getRandom();
            if (sealType != null) {
                switch (sealType) {
                    case BOMB ->
                        //                        BombFlowerItemProjectileEntity bombFlower =
                        //                                new BombFlowerItemProjectileEntity(TerramityModEntities.BOMB_FLOWER_ITEM_PROJECTILE.get(),
                        //                                        victim.getRandomX(0.5F), victim.getY(0.5F), victim.getRandomZ(0.5F), level);
                        //                        bombFlower.setOwner(attacker);
                        //                        bombFlower.setSilent(true);
                        //                        bombFlower.setBaseDamage(4.0F);
                        //                        bombFlower.setKnockback(5);
                        //                        bombFlower.setCritArrow(false);
                        //                        bombFlower.setDeltaMovement(
                        //                                Mth.randomBetween(random, -0.5F, 0.5F),
                        //                                Mth.randomBetween(random, -0.5F, 0.5F),
                        //                                Mth.randomBetween(random, -0.5F, 0.5F)
                        //                        );
                        //                        level.addFreshEntity(bombFlower);
                        //                        level.playSound(null, victim.getX(), victim.getY(), victim.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 1.0F, 1.0F / (RandomSource.create().nextFloat() * 0.5F + 1.0F));
                            level.explode(victim, victim.getRandomX(2), victim.getY(0.5), victim.getZ(2), 2, Level.ExplosionInteraction.NONE);
                    case COSMIC -> {
                        StarStreamEntity spellEntity = new StarStreamEntity(BrutalityModEntities.STAR_STREAM_ENTITY.get(), level);
                        spellEntity.setSpellLevel(0);
                        Vec3 randomPos;
                        if (attacker == null) {
                            randomPos = new Vec3(
                                    victim.getRandomX(2),
                                    victim.getY(0.5F) + Mth.nextFloat(random, 7.5F, 12.5F),
                                    victim.getRandomZ(2));
                        } else {
                            randomPos = new Vec3(
                                    attacker.getRandomX(2),
                                    attacker.getY(0.5F) + Mth.nextFloat(random, 7.5F, 12.5F),
                                    attacker.getRandomZ(2));
                        }


                        spellEntity.setPos(randomPos);
                        spellEntity.setOwner(victim);
                        Vec3 targetPos;
                        if (attacker == null) {
                            targetPos = new Vec3(victim.getRandomX(10), victim.getY(), victim.getRandomZ(10));
                        } else {
                            targetPos = attacker.getPosition(1).add(0, attacker.getBbHeight() * 0.5, 0);
                        }

                        Vec3 direction = targetPos.subtract(randomPos).normalize();

                        spellEntity.shoot(direction.x, direction.y, direction.z, 1.5F, 1.5F);

                        level.addFreshEntity(spellEntity);
                        level.playSound(null, victim.getX(), victim.getY(0.5), victim.getZ(),
                                BrutalityModSounds.BASS_BOP.get(), SoundSource.AMBIENT,
                                1.5F, Mth.nextFloat(random, 0.7F, 1.2F));
                    }

                }
            }
        });


        if (attacker instanceof LivingEntity livingAttacker) {

            if (attacker instanceof Player playerAttacker) {

                AttributeInstance omnivampAttr = playerAttacker.getAttribute(ModAttributes.OMNIVAMP.get());
                if (omnivampAttr != null) {
                    playerAttacker.heal(((float) (modifiedAmount[0] * (omnivampAttr.getValue() - 1))));
                }

                playerAttacker.getCapability(BrutalityCapabilities.PLAYER_COMBO_CAP).ifPresent(cap -> {
                    if (cap.lastVictimId() == victim.getId() && System.currentTimeMillis() - cap.lastHitTime() < 5000) {
                        cap.setHitCount((cap.hitCount() + 1) % 15);
                    } else {
                        cap.setHitCount(1);
                    }

                    cap.setLastVictimId(victim.getId());
                    cap.setLastHitTime(System.currentTimeMillis());
                    PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(playerAttacker.getId(), playerAttacker));


                    CuriosApi.getCuriosInventory(playerAttacker).ifPresent(handler ->
                            handler.findFirstCurio(BrutalityModItems.EXPONENTIAL_CHARM.get()).ifPresent(slot -> {
                                modifiedAmount[0] = (float) Math.pow(modifiedAmount[0], (0.01 * cap.hitCount()) + 1);
                            }));
                });

            }


            // Attacker is Living Entity
            CuriosApi.getCuriosInventory(livingAttacker).ifPresent(handler -> {
                if (handler.isEquipped(BrutalityModItems.CROWBAR.get())) {
                    if (victim.getHealth() / victim.getMaxHealth() > 0.9F) {
                        modifiedAmount[0] *= 1.4F;
                    }
                }

                if (handler.isEquipped(BrutalityModItems.DUELING_GLOVE.get())) {
                    LivingEntity nearestEntity = level.getNearestEntity(
                            LivingEntity.class, TargetingConditions.DEFAULT, livingAttacker, livingAttacker.getX(), livingAttacker.getY(), livingAttacker.getZ(), livingAttacker.getBoundingBox().inflate(100));

                    if (nearestEntity == victim) {
                        modifiedAmount[0] *= 1.5F;
                    }
                }

                if (handler.isEquipped(BrutalityModItems.OLD_GUILLOTINE.get())) {
                    if (victim.getHealth() <= 5) {
                        victim.kill();
                        return;
                    }
                }

                if (handler.isEquipped(BrutalityModItems.BLOOD_PULSE_GAUNTLETS.get())) {
                    if (livingAttacker.hasEffect(BrutalityModMobEffects.ENRAGED.get())) {
                        livingAttacker.level().explode(livingAttacker, livingAttacker.damageSources().explosion(livingAttacker, null),
                                null, victim.position(), 0.25F, false, Level.ExplosionInteraction.MOB
                        );
                    }
                }
                if (handler.isEquipped(BrutalityModItems.THE_OATH.get())) {
                    victim.invulnerableTime = 0;
                    victim.hurt(victim.damageSources().indirectMagic(livingAttacker, null), modifiedAmount[0] * 0.1F);
                }

                handler.findFirstCurio(BrutalityModItems.SUM_CHARM.get()).ifPresent(slot -> {
                    float damageStored = slot.stack().getOrCreateTag().getFloat(SUM_DAMAGE);
                    modifiedAmount[0] += damageStored;
                    slot.stack().getOrCreateTag().putFloat(SUM_DAMAGE, 0);
                });

                if (level instanceof ServerLevel serverLevel)
                    if (handler.isEquipped(BrutalityModItems.SINE_CHARM.get())) {
                        modifiedAmount[0] += Sine.getCurrentBonus(serverLevel);
                    }


                handler.findFirstCurio(BrutalityModItems.GLUTTONY_CHARM.get()).ifPresent(slot -> {
                    modifiedAmount[0] += slot.stack().getOrCreateTag().getInt(SOULS) * 0.01F;
                });

                if (handler.isEquipped(BrutalityModItems.CROWN_OF_TYRANNY.get())) {
                    float missingHealthRatio = victim.getHealth() / victim.getMaxHealth();
                    modifiedAmount[0] *= (1 + (1 - missingHealthRatio)) * 0.75F;
                }

                if (handler.isEquipped(BrutalityModItems.PORTABLE_QUANTUM_THINGAMABOB.get())) {
                    livingAttacker.addEffect(new MobEffectInstance(TerramityModMobEffects.MIRRORING.get(), 20));
                }

                //===========================================================================================//
                // GASTRONOMY START

                final int[] fridgeMult = {1};

                handler.findFirstCurio(BrutalityModItems.FRIDGE_CHARM.get()).ifPresent(slot -> {
                    fridgeMult[0] = 2;
                });
                handler.findFirstCurio(BrutalityModItems.SMART_FRIDGE_CHARM.get()).ifPresent(slot -> {
                    fridgeMult[0] = 3;
                });

                handler.findFirstCurio(BrutalityModItems.PIZZA_SLOP_CHARM.get()).ifPresent(slot -> {
                    victim.addEffect(new
                            MobEffectInstance(BrutalityModMobEffects.SLICKED.get(), 120 * fridgeMult[0], 1, false, false));
                });
                handler.findFirstCurio(BrutalityModItems.THE_SMOKEHOUSE.get()).ifPresent(slot -> {
                    victim.addEffect(new
                            MobEffectInstance(BrutalityModMobEffects.SMOKED.get(), 80 * fridgeMult[0], 1, false, false));
                });
                handler.findFirstCurio(BrutalityModItems.EXTRA_VIRGIN_OLIVE_OIL_CHARM.get()).ifPresent(slot -> {
                    victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.OILED.get(), 120 * fridgeMult[0], 3));
                });
                handler.findFirstCurio(BrutalityModItems.CARAMEL_CRUNCH_MEDALLION.get()).ifPresent(slot -> {
                    victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CANDIED.get(), 80 * fridgeMult[0], 1));
                    victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CARAMELIZED.get(), 80 * fridgeMult[0], 1));
                });
                handler.findFirstCurio(BrutalityModItems.DUNKED_DONUT.get()).ifPresent(slot -> {
                    victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.GLAZED.get(), 80 * fridgeMult[0], 1));
                    victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SPRINKLED.get(), 80 * fridgeMult[0], 1));
                });
                handler.findFirstCurio(BrutalityModItems.LOLLIPOP_OF_ETERNITY.get()).ifPresent(slot -> {
                    victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.GLAZED.get(), 120 * fridgeMult[0], 2));
                    victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CARAMELIZED.get(), 120 * fridgeMult[0], 2));
                    victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SPRINKLED.get(), 120 * fridgeMult[0], 2));
                    victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.CANDIED.get(), 120 * fridgeMult[0], 2));
                });
                handler.findFirstCurio(BrutalityModItems.SALT_AND_PEPPER_CHARM.get()).ifPresent(slot -> {
                    victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.SALTED.get(), 120 * fridgeMult[0], 1));
                    victim.addEffect(new MobEffectInstance(BrutalityModMobEffects.PEPPERED.get(), 120 * fridgeMult[0], 1));
                });
                // GASTRONOMY END
                //===========================================================================================//


            });


            // LIVING VICTIM FROM LIVING ATTACKER START ===============================================
            if (victim.hasEffect(BrutalityModMobEffects.STONEFORM.get())) {
                int amp = Objects.requireNonNull(victim.getEffect(BrutalityModMobEffects.STONEFORM.get())).getAmplifier();
                modifiedAmount[0] *= (1 - 0.1F * (amp + 1F));
            }


            CuriosApi.getCuriosInventory(victim).ifPresent(handler -> {
                handler.findFirstCurio(BrutalityModItems.YATA_NO_KAGAMI.get()).ifPresent(slot -> {
                    float negated = modifiedAmount[0] * 0.25F;
                    modifiedAmount[0] = Math.max(0, modifiedAmount[0] - negated);
                    livingAttacker.hurt(livingAttacker.damageSources().indirectMagic(victim, null), negated);
                });
                handler.findFirstCurio(BrutalityModItems.BLOODSTAINED_MIRROR.get()).ifPresent(slot -> {
                    livingAttacker.hurt(livingAttacker.damageSources().indirectMagic(victim, null), modifiedAmount[0] * 0.1F);
                });

                handler.findFirstCurio(BrutalityModItems.DUELING_GLOVE.get()).ifPresent(slot -> {

                    List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, victim.getBoundingBox().inflate(100),
                            e -> e != victim && !(e instanceof ArmorStand));

                    LivingEntity nearestEntity = nearbyEntities.stream()
                            .min(Comparator.comparingDouble(e -> e.distanceToSqr(victim)))
                            .orElse(null);

//                        System.out.println("NEARBY ENTITIES: " + nearbyEntities);
//                        System.out.println("NEAREST: " + nearestEntity);
//                        System.out.println("ATTACKER: " + livingAttacker);

                    if (nearestEntity != livingAttacker) {
                        modifiedAmount[0] *= 1.5F;
                    }
                });


            });


            CuriosApi.getCuriosInventory(livingAttacker).ifPresent(handler -> {
                handler.findFirstCurio(BrutalityModItems.GREED_CHARM.get()).ifPresent(slot -> {
                    modifiedAmount[0] *= 1 + slot.stack().getOrCreateTag().getInt(GREED_BONUS) * 0.01F;
                });
            });
        }

        if (victim.hasEffect(BrutalityModMobEffects.GRACE.get())) {
            if (modifiedAmount[0] <= victim.getEffect(BrutalityModMobEffects.GRACE.get()).getAmplifier() *
                    BrutalitySpell.getStat(HolyMantleSpell.class, BrutalityTooltipHelper.SpellStatComponents.DEFENSE).levelDelta()) {
                event.setCanceled(true);
                victim.removeEffect(BrutalityModMobEffects.GRACE.get());
                return;
            }
        }


        if (event.getSource().getEntity() instanceof Player attackerPlayer) {
            if (!attackerPlayer.hasEffect(BrutalityModMobEffects.ENRAGED.get()))
                CuriosApi.getCuriosInventory(attackerPlayer).ifPresent(handler -> {
                    if (!handler.findCurios(stack -> stack.is(ModTags.Items.RAGE_ITEMS)).isEmpty()) {
                        attackerPlayer.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP).ifPresent(cap -> {
                            float damageDealt = modifiedAmount[0]; // Final amount dealt
                            float rageGain = damageDealt / 5F;

                            AttributeInstance rageGainAttr = attackerPlayer.getAttribute(ModAttributes.RAGE_GAIN_MULTIPLIER.get());
                            if (rageGainAttr != null) rageGain *= (float) rageGainAttr.getValue();

                            cap.incrementRage(Math.max(0, rageGain));
                            tryTriggerRage(attackerPlayer, handler, cap);
                        });
                    }
                });
        }

        if (victim instanceof Player victimPlayer) {
            // If Victim is Player
            if (victimPlayer.hasEffect(BrutalityModMobEffects.SAD.get())) {
                int foodLevel = victimPlayer.getFoodData().getFoodLevel();
                if (foodLevel > 0) {
                    float foodReduction = modifiedAmount[0] / 2;
                    victimPlayer.getFoodData().setFoodLevel((int) Math.max(0, foodLevel - foodReduction));
                    modifiedAmount[0] = foodReduction;
                }
            }


            CuriosApi.getCuriosInventory(victimPlayer).ifPresent(handler -> {


                handler.findFirstCurio(BrutalityModItems.HEART_OF_GOLD.get()).ifPresent(slot -> {
                    float toHeal = modifiedAmount[0] * 0.25F;
                    float maxPossible = Math.max(victimPlayer.getMaxHealth() - victimPlayer.getAbsorptionAmount(), 0);
                    float finalToHeal = Math.min(toHeal, maxPossible);
                    DelayedTaskScheduler.queueServerWork(level, 1, () -> victimPlayer.setAbsorptionAmount(victimPlayer.getAbsorptionAmount() + finalToHeal));
                });


                handler.findFirstCurio(BrutalityModItems.NANOMACHINES.get()).ifPresent(slot -> {
                    DamageSource source = event.getSource();
                    if (source.is(DamageTypeTags.BYPASSES_ARMOR)) return;

                    if (source.is(DamageTypeTags.IS_PROJECTILE) ||
                            source.is(DamageTypes.MOB_ATTACK) ||
                            source.is(DamageTypes.PLAYER_ATTACK) ||
                            source.is(DamageTypes.MOB_ATTACK_NO_AGGRO)) {
                        modifiedAmount[0] = Math.max(0, modifiedAmount[0] / 2);
                    }
                });


                if (handler.isEquipped(BrutalityModItems.SUBTRACTION_CHARM.get())) {
                    modifiedAmount[0] = Math.max(0, modifiedAmount[0] - 2);
                }

                if (handler.isEquipped(BrutalityModItems.DIVISION_CHARM.get())) {
                    modifiedAmount[0] = Math.max(0, modifiedAmount[0] / 1.1F);
                }

                handler.findFirstCurio(BrutalityModItems.SUM_CHARM.get()).ifPresent(slot -> {
                    float damageStored = slot.stack().getOrCreateTag().getFloat(SUM_DAMAGE);
                    slot.stack().getOrCreateTag().putFloat(SUM_DAMAGE, Math.min(150, damageStored + Math.min(10, modifiedAmount[0] / 2)));
                });

                if (handler.isEquipped(BrutalityModItems.BLOOD_ORB.get())) {
                    SpellCastingHandler.addMana(victimPlayer, modifiedAmount[0] * 7.5F);
                }
                if (!victimPlayer.hasEffect(BrutalityModMobEffects.ENRAGED.get()))
                    if (!handler.findCurios(stack -> stack.is(ModTags.Items.RAGE_ITEMS)).isEmpty()) {
                        victimPlayer.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP).ifPresent(cap -> {
                            // Before modification
                            float rageGain = modifiedAmount[0];

                            AttributeInstance rageGainAttr = victimPlayer.getAttribute(ModAttributes.RAGE_GAIN_MULTIPLIER.get());
                            if (rageGainAttr != null) rageGain *= (float) rageGainAttr.getValue();

                            cap.incrementRage(Math.max(0, rageGain));
                            tryTriggerRage(victimPlayer, handler, cap);
                        });
                    }
            });
        }

        AttributeInstance attributeInstance = victim.getAttribute(ModAttributes.DAMAGE_TAKEN.get());
        if (attributeInstance != null) {
            modifiedAmount[0] = (float) (modifiedAmount[0] * attributeInstance.getValue());
        }

        event.setAmount(Math.max(0, modifiedAmount[0]));
    }

    @SubscribeEvent
    public static void onLivingKnockback(LivingKnockBackEvent event) {
        LivingEntity victim = event.getEntity();
        Entity lastAttacker = victim.getLastAttacker();
        if (lastAttacker instanceof Player player) {
            Item item = player.getMainHandItem().getItem();

            if (victim.getLastDamageSource() != null && victim.getLastDamageSource().is(BrutalityDamageTypes.DEATHSAW)) {
                event.setCanceled(true);
            }

            if (item instanceof EventHorizonSpear) {
                event.setCanceled(true);

                Vec3 direction = lastAttacker.getPosition(1f).subtract(victim.getPosition(1F)).normalize();

                victim.addDeltaMovement(direction.scale(1.2));

            }
        }

    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            CuriosApi.getCuriosInventory(entity).ifPresent(handler -> {
                handler.findFirstCurio(BrutalityModItems.CELESTIAL_STARBOARD.get()).ifPresent(slotResult ->
                        event.setCanceled(true));

                handler.findFirstCurio(BrutalityModItems.HYPERBOLIC_FEATHER.get()).ifPresent(slotResult -> {
                    if (!player.getCooldowns().isOnCooldown(slotResult.stack().getItem())) {
                        player.getCooldowns().addCooldown(slotResult.stack().getItem(), 80);

                        DelayedTaskScheduler.queueServerWork(event.getEntity().level(), 1, () ->
                                player.heal(2 * ModUtils.calculateFallDamage(player, event.getDistance(), event.getDamageMultiplier())));
                    }
                });

                handler.findFirstCurio(BrutalityModItems.TOPAZ_ANKLET.get()).ifPresent(slotResult -> {
                    Vec3 loc = entity.getPosition(1);
                    long gameTime = entity.level().getGameTime();
                    long seed = (Double.doubleToLongBits(loc.x) ^ Double.doubleToLongBits(loc.y) ^
                            Double.doubleToLongBits(loc.z) ^ gameTime);
                    Random seeded = new Random(seed);
                    float chance = seeded.nextFloat(0, 1);
                    if (chance < 0.9F) {
                        event.setCanceled(true);
                    }
                });
            });

            if (player.getMainHandItem().getItem() instanceof AtomicJudgementHammer) {
                event.setDamageMultiplier(0.15F);
                if (event.getDistance() > 10 && !player.level().isClientSide() && player.isShiftKeyDown()) {
                    AtomicJudgementHammer.doExplosion(player, player.getPosition(1));
                }
            }


        }

    }


    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        Level level = victim.level();

        level.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, victim, victim.getBoundingBox().inflate(3)).forEach(nearbyEntity -> {
            CuriosApi.getCuriosInventory(nearbyEntity).ifPresent(handler ->
                    handler.findFirstCurio(BrutalityModItems.SELF_REPAIR_NEXUS.get()).ifPresent(slot -> nearbyEntity.heal(2)));
        });

        if (event.getSource().getEntity() instanceof Player attackerPlayer) {
            CuriosApi.getCuriosInventory(attackerPlayer).ifPresent(handler -> {
                handler.findFirstCurio(BrutalityModItems.GLUTTONY_CHARM.get()).ifPresent(slot -> {
                    ItemStack stack = slot.stack();
                    CompoundTag tag = stack.getOrCreateTag();
                    int souls = tag.getInt(SOULS) + 1;
                    tag.putInt(SOULS, souls);
                });

                handler.findFirstCurio(BrutalityModItems.RAMPAGE_CLOCK.get()).ifPresent(slot ->
                        ModUtils.modifyEffect(attackerPlayer, BrutalityModMobEffects.ENRAGED.get(),
                                new ModUtils.ModValue(20, false), null, null, null, null)
                );

            });
        }


        if (event.getEntity() instanceof Player victimPlayer) {
            if (victimPlayer.level() instanceof ServerLevel serverLevel) {
                SupernovaSword.clearAsteroids(victimPlayer, serverLevel);
                CreaseOfCreation.handleCreaseOfCreation(victimPlayer);
            }

            resetAllColors();

            if (victimPlayer.hasEffect(BrutalityModMobEffects.ENRAGED.get())) {
                CuriosApi.getCuriosInventory(victimPlayer).ifPresent(handler -> {
                    handler.findFirstCurio(BrutalityModItems.GRUDGE_TOTEM.get()).ifPresent(slot -> {
                        event.setCanceled(true);
                        victimPlayer.setHealth(0.5F);
                    });
                });
            }

            CuriosApi.getCuriosInventory(victimPlayer).ifPresent(handler -> {
                ItemCooldowns cooldowns = victimPlayer.getCooldowns();
                victimPlayer.getCapability(BrutalityCapabilities.RESPAWN_CAP).ifPresent(cap -> {
                    if (handler.isEquipped(BrutalityModItems.SILVER_BOOSTER_PACK.get())) {
                        cap.setBoosterType(EntityCapabilities.RespawnCap.BOOSTER_TYPE.SILVER);
                    }
                    if (handler.isEquipped(BrutalityModItems.DIAMOND_BOOSTER_PACK.get()) && !cooldowns.isOnCooldown(BrutalityModItems.DIAMOND_BOOSTER_PACK.get())) {
                        cap.setBoosterType(EntityCapabilities.RespawnCap.BOOSTER_TYPE.DIAMOND);
                    }
                    if (handler.isEquipped(BrutalityModItems.EVIL_KING_BOOSTER_PACK.get()) && !cooldowns.isOnCooldown(BrutalityModItems.EVIL_KING_BOOSTER_PACK.get())) {
                        cap.setBoosterType(EntityCapabilities.RespawnCap.BOOSTER_TYPE.EVIL_KING);
                    }


                });

                if (handler.isEquipped(BrutalityModItems.EVIL_KING_RESPAWN_CARD.get()) && !cooldowns.isOnCooldown(BrutalityModItems.EVIL_KING_RESPAWN_CARD.get())) {
                    cooldowns.addCooldown(BrutalityModItems.EVIL_KING_RESPAWN_CARD.get(), 15 * 60 * 20);
                    event.setCanceled(true);
                    victimPlayer.setHealth(6F);

                } else if (handler.isEquipped(BrutalityModItems.DIAMOND_RESPAWN_CARD.get()) && !cooldowns.isOnCooldown(BrutalityModItems.DIAMOND_RESPAWN_CARD.get())) {
                    cooldowns.addCooldown(BrutalityModItems.DIAMOND_RESPAWN_CARD.get(), 30 * 60 * 20);
                    event.setCanceled(true);
                    victimPlayer.setHealth(4F);

                } else {
                    handler.findFirstCurio(BrutalityModItems.SILVER_RESPAWN_CARD.get()).ifPresent(slot -> {
                        slot.stack().hurtAndBreak(slot.stack().getMaxDamage(), victimPlayer, (player) -> {
                        });
                        event.setCanceled(true);
                        victimPlayer.setHealth(2F);

                    });
                }

            });


        }
    }

    public static void tryTriggerRage(Player player, ICuriosItemHandler handler, EntityCapabilities.PlayerRageCap
            cap) {
        int maxRage = (int) player.getAttributeValue(ModAttributes.MAX_RAGE.get());

        if (cap.rageValue() >= maxRage) {
            // Don't trigger if wearing Anger Management
            if (handler.findFirstCurio(BrutalityModItems.ANGER_MANAGEMENT.get()).isEmpty()) {
                int duration = 40;
                int rageLevel = (int) Math.floor(cap.rageValue() / 100);
                AttributeInstance rageTimeAttr = player.getAttribute(ModAttributes.RAGE_TIME_MULTIPLIER.get());
                if (rageTimeAttr != null) {
                    duration = (int) (duration * rageTimeAttr.getValue());
                }
                AttributeInstance rageLevelAttr = player.getAttribute(ModAttributes.RAGE_LEVEL.get());
                if (rageLevelAttr != null) {
                    rageLevel += (int) rageLevelAttr.getValue();
                }

                player.addEffect(new MobEffectInstance(BrutalityModMobEffects.ENRAGED.get(), duration, rageLevel, false, true));
                cap.setRageValue(0);

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1F, 1F);
            }
        }

        cap.setRageValue(Math.min(cap.rageValue(), maxRage));
        PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(player.getId(), player));
    }


}
