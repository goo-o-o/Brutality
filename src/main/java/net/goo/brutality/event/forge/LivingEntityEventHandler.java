package net.goo.brutality.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.capabilities.EntityCapabilities;
import net.goo.brutality.event.ConsumeManaEvent;
import net.goo.brutality.event.SpellCastEvent;
import net.goo.brutality.item.BrutalityArmorMaterials;
import net.goo.brutality.item.armor.VampireLordArmorItem;
import net.goo.brutality.item.weapon.generic.CreaseOfCreation;
import net.goo.brutality.item.weapon.hammer.AtomicJudgementHammer;
import net.goo.brutality.item.weapon.spear.EventHorizonSpear;
import net.goo.brutality.item.weapon.sword.SupernovaSword;
import net.goo.brutality.magic.IBrutalitySpell;
import net.goo.brutality.magic.SpellCastingHandler;
import net.goo.brutality.magic.SpellCooldownTracker;
import net.goo.brutality.registry.*;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.SealUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
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

import java.util.Random;

import static net.goo.brutality.item.curios.charm.Gluttony.SOULS;
import static net.goo.brutality.util.helpers.EnvironmentColorManager.resetAllColors;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class LivingEntityEventHandler {

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!(event.getTo().getItem() instanceof VampireLordArmorItem)) {
            event.getEntity().getCapability(BrutalityCapabilities.PLAYER_BLOOD_CAP).ifPresent(cap -> cap.setBloodValue(0));
        }
    }

    @SubscribeEvent
    public static void onItemUseStart(LivingEntityUseItemEvent.Start event) {
        if (event.getEntity() instanceof Player player) {
            if (ModUtils.hasFullArmorSet(player, BrutalityArmorMaterials.VAMPIRE_LORD) && event.getItem().isEdible()) {
                event.setCanceled(true);
            }
        }
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

                SealUtils.handleSealProcOffensive(owner.level(), owner, location, cap.getSealType());

            });
        }
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
                handler.findFirstCurio(BrutalityModItems.GLUTTONY.get()).ifPresent(slot -> {
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


}
