package net.goo.brutality.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.item.armor.VampireLordArmorItem;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.item.curios.charm.BoosterPack;
import net.goo.brutality.common.item.curios.charm.RespawnCard;
import net.goo.brutality.common.item.curios.charm.SelfRepairNexus;
import net.goo.brutality.common.item.generic.StatTrakkerItem;
import net.goo.brutality.common.item.weapon.generic.CreaseOfCreation;
import net.goo.brutality.common.item.weapon.hammer.AtomicJudgementHammer;
import net.goo.brutality.common.item.weapon.spear.EventHorizon;
import net.goo.brutality.common.item.weapon.sword.SupernovaSword;
import net.goo.brutality.common.registry.BrutalityDamageTypes;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.attribute.AttributeCalculationHelper;
import net.goo.brutality.util.item.ItemCategoryUtils;
import net.goo.brutality.util.item.SealUtils;
import net.goo.brutality.util.item.StatTrakUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import static net.goo.brutality.common.item.curios.charm.Gluttony.SOULS;
import static net.goo.brutality.util.EnvironmentColorManager.resetAllColors;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class LivingEntityEventHandler {

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        VampireLordArmorItem.resetBloodMeterOnUnequip(event);
    }

    @SubscribeEvent
    public static void onItemUseStart(LivingEntityUseItemEvent.Start event) {
        VampireLordArmorItem.preventEating(event);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player newPlayer = event.getEntity();

        BoosterPack.handleRespawn(newPlayer);
    }

    @SubscribeEvent
    public static void entityVisibility(LivingEvent.LivingVisibilityEvent event) {
        AttributeCalculationHelper.handleEntityVisibility(event);
    }

    @SubscribeEvent
    public static void onPlayerCrit(CriticalHitEvent event) {
        AttributeCalculationHelper.handleCrits(event);
    }


    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity healed = event.getEntity();
        float healing = event.getAmount();
        healing = BrutalityCurioItem.Hooks.applyOnWearerHeal(healed, healing);
        event.setAmount(healing);
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (event.getProjectile().getOwner() instanceof LivingEntity owner) {
            event.getProjectile().getCapability(BrutalityCapabilities.SEAL_TYPE).ifPresent(cap -> {
                Vec3 location;
                if (event.getRayTraceResult() instanceof EntityHitResult entityHitResult) {
                    location = entityHitResult.getEntity().getPosition(1).add(0, entityHitResult.getEntity().getY(0.5F), 0);
                } else {
                    location = event.getRayTraceResult().getLocation();
                }

                SealUtils.handleSealProcOffensive(owner.level(), owner, location, cap.getSealType());
            });

            if (!event.getEntity().isAlive()) {
                if (ItemCategoryUtils.isRangedWeapon(owner.getMainHandItem())) {
                    StatTrakUtils.incrementStatTrakIfPossible(owner.getMainHandItem());
                } else if (ItemCategoryUtils.isRangedWeapon(owner.getOffhandItem())) {
                    StatTrakUtils.incrementStatTrakIfPossible(owner.getOffhandItem());
                }
            }
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

            if (item instanceof EventHorizon) {
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

            BrutalityCurioItem.Hooks.applyOnWearerFall(event);


            if (player.getMainHandItem().getItem() instanceof AtomicJudgementHammer) {
                event.setDamageMultiplier(0.15F);
                if (event.getDistance() > 10 && !player.level().isClientSide() && player.isShiftKeyDown()) {
                    AtomicJudgementHammer.doExplosion(player, player.getPosition(1));
                }
            }


        }

    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        StatTrakkerItem.incrementOnBlockBreak(event);
    }

    @SubscribeEvent
    public static void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
        StatTrakkerItem.incrementOnShear(event);
    }

    @SubscribeEvent
    public static void onShieldBlock(ShieldBlockEvent event) {
        StatTrakkerItem.incrementOnShieldBlock(event);
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();

        SelfRepairNexus.processNearbyDeath(victim);
        RespawnCard.tryCheatDeath(event);
        BoosterPack.handleDeath(event);

        if (event.getSource().getEntity() instanceof LivingEntity killer) {
            StatTrakkerItem.incrementOnKill(killer);
            BrutalityCurioItem.Hooks.applyOnWearerKill(killer, victim, event.getSource());

            CuriosApi.getCuriosInventory(killer).ifPresent(handler -> {
                handler.findFirstCurio(BrutalityItems.GLUTTONY.get()).ifPresent(slot -> {
                    ItemStack stack = slot.stack();
                    CompoundTag tag = stack.getOrCreateTag();
                    int souls = tag.getInt(SOULS) + 1;
                    tag.putInt(SOULS, souls);
                });

                handler.findFirstCurio(BrutalityItems.RAMPAGE_CLOCK.get()).ifPresent(slot ->
                        ModUtils.modifyEffect(killer, BrutalityEffects.ENRAGED.get(),
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

            if (victimPlayer.hasEffect(BrutalityEffects.ENRAGED.get())) {
                CuriosApi.getCuriosInventory(victimPlayer).ifPresent(handler -> {
                    handler.findFirstCurio(BrutalityItems.GRUDGE_TOTEM.get()).ifPresent(slot -> {
                        event.setCanceled(true);
                        victimPlayer.setHealth(0.5F);
                    });
                });
            }
        }
    }


}
