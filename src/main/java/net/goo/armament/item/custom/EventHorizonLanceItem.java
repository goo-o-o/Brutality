package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.client.item.ArmaGlowingWeaponRenderer;
import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.entity.custom.BlackHole;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaTridentItem;
import net.goo.armament.registry.ModEntities;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.*;
import java.util.function.Consumer;

import static net.goo.armament.util.ModResources.EVENT_HORIZON_COLORS;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHorizonLanceItem extends ArmaTridentItem implements Vanishable {
    private static final String SPAWNED = "blackHoleSpawned";
    private static final String ACCRETION = "accretionActive";
    private static final Map<UUID, BlackHole> playerBlackHoleMap = new HashMap<>();
    private double spawnRadius;
    private int tickCount = 0;

    public EventHorizonLanceItem(Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pProperties, identifier, category, rarity, abilityCount);
        this.colors = EVENT_HORIZON_COLORS;
    }


    public static void handleHangingBlackHole(Player player) {
        BlackHole blackHoleEntity = playerBlackHoleMap.get(player.getUUID());
        if (blackHoleEntity != null) {
            playerBlackHoleMap.remove(player.getUUID());
            blackHoleEntity.remove(Entity.RemovalReason.DISCARDED);
            if (droppedInventory != null) {
                for (ItemEntity droppedItem : droppedInventory) {
                    ItemStack itemStack = droppedItem.getItem();
                    if (itemStack.getItem() instanceof EventHorizonLanceItem) {
                        itemStack.getOrCreateTag().putBoolean(SPAWNED, false);
                    }
                }
            }
        }
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, (livingEntity) -> {
            livingEntity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }


    public static Collection<ItemEntity> droppedInventory;

    @SubscribeEvent
    public static void getInventoryBeforeDeath(LivingDropsEvent event) {
        droppedInventory = event.getDrops();
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            handleHangingBlackHole(player);
        }
    }

    @Override
    public <T extends Item & ArmaGeoItem, R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, ArmaGlowingWeaponRenderer.class);
    }

    @SubscribeEvent
    public static void onChangeDimensionsOrDeath(PlayerEvent.PlayerChangedDimensionEvent event) {
        handleHangingBlackHole(event.getEntity());
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        handleHangingBlackHole(event.getEntity());
    }

    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof ServerPlayer player && !pLevel.isClientSide) {
            // Get the player's existing BlackHoleEntity
            BlackHole blackHoleEntity = playerBlackHoleMap.get(player.getUUID());
            setSpawnRadius(72000 - pTimeLeft);
            // Check if the black hole entity already exists
            if (blackHoleEntity != null) {
                // If it exists, toggle it off
                pStack.getOrCreateTag().putBoolean(SPAWNED, false);
                blackHoleEntity.discard();
                playerBlackHoleMap.remove(player.getUUID());
            } else {
                // If it does not exist, create a new black hole entity
                pStack.getOrCreateTag().putBoolean(SPAWNED, true);
                blackHoleEntity = new BlackHole(ModEntities.BLACK_HOLE_ENTITY.get(), pLevel);
                blackHoleEntity.setOwner(player.getUUID());
                blackHoleEntity.setPos(player.getX(), player.getY() + 2, player.getZ());
                pLevel.addFreshEntity(blackHoleEntity);
                playerBlackHoleMap.put(player.getUUID(), blackHoleEntity);
            }
        }
    }

    public void setSpawnRadius(double spawnRadius) {
        this.spawnRadius = Math.min(spawnRadius, 10);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (pPlayer.isCrouching()) {
            itemstack.getOrCreateTag().putBoolean(ACCRETION, true);
            return InteractionResultHolder.fail(itemstack);
        } else {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide && pEntity instanceof Player player) {
            BlackHole blackHoleEntity = playerBlackHoleMap.get(player.getUUID());
            if (blackHoleEntity != null) {
                if (pIsSelected && isSpawned(pStack)) {
                    float speed = 0.25F;
                    double plX = player.getX() - (Mth.sin((float) tickCount / (8 / speed)) * spawnRadius);
                    double plY = player.getY() + (Mth.sin((float) tickCount / (4 / speed)) * 0.25);
                    double plZ = player.getZ() - (Mth.cos((float) tickCount / (8 / speed)) * spawnRadius);
                    Vec3 plPos = new Vec3(plX, plY + 1F, plZ);
                    blackHoleEntity.setDeltaMovement(plPos.subtract(blackHoleEntity.position()));

                    if (tickCount % 20 == 0) pStack.hurtAndBreak(1, player, (consumer) -> {
                        consumer.broadcastBreakEvent(player.getUsedItemHand());
                    });

                } else {
                    blackHoleEntity.discard();
                    playerBlackHoleMap.remove(player.getUUID());
                    setSpawned(pStack, false);
                }
            }

            if (player.isCrouching() && pStack.getOrCreateTag().getBoolean(ACCRETION)) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, 2, false, false));
                player.setDeltaMovement(0,0,0);
                ((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(player));

                float suckFactor = 0.5F;

                AABB pullBox = new AABB(player.position(), player.position()).inflate(5);

                List<Entity> entities = pLevel.getEntities(player, pullBox, entity -> entity instanceof LivingEntity);

                for (Entity target : entities) {
                    Vec3 targetPosition = target.position();
                    Vec3 targetDirection = (player.position().subtract(targetPosition)).scale(suckFactor);

                    target.addDeltaMovement(targetDirection);

                    if (target instanceof ServerPlayer serverTarget) {
                        serverTarget.connection.send(new ClientboundSetEntityMotionPacket(target));
                    }

                    if (target.distanceTo(player) < 1 && tickCount % 4 == 0) {
                        target.hurt(target.damageSources().flyIntoWall(), 1);
                    }
                }
            } else pStack.getOrCreateTag().putBoolean(ACCRETION, false);
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        tickCount++;
    }

    public boolean isSpawned(ItemStack pStack) {
        return pStack.getOrCreateTag().getBoolean(SPAWNED);
    }

    public void setSpawned(ItemStack pStack, boolean pBoolean) {
        pStack.getOrCreateTag().putBoolean(SPAWNED, pBoolean);
    }

    private PlayState predicate(AnimationState<GeoAnimatable> animationState) {
        ItemStack stack = animationState.getData(DataTickets.ITEMSTACK);
        if (isSpawned(stack)) {
            animationState.getController().setAnimation(RawAnimation.begin().thenPlayAndHold("throw"));
        } else {
            animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }


}
