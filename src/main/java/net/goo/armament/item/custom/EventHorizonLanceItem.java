package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.entity.custom.BlackHoleEntity;
import net.goo.armament.item.ArmaTridentItem;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.registry.ModEntities;
import net.goo.armament.registry.ModParticles;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
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

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHorizonLanceItem extends ArmaTridentItem implements Vanishable {
    private static final String SPAWNED = "blackHoleSpawned";
    private static final String ACCRETION = "accretionActive";
    private static final Map<UUID, BlackHoleEntity> playerBlackHoleMap = new HashMap<>();
    private double spawnRadius;
    int tickCount = 0;

    public EventHorizonLanceItem(Properties pProperties, String identifier, ModItemCategories category, Rarity rarity) {
        super(pProperties, identifier, category, rarity);
        this.colors = new int[][]{{250, 140, 20}, {50, 50, 50}};
    }

    public static void handleHangingBlackHole(Player player) {
        BlackHoleEntity blackHoleEntity = playerBlackHoleMap.get(player.getUUID());
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
    public <T extends Item & ArmaGeoItem> void initGeo(Consumer<IClientItemExtensions> consumer, int rendererID) {
        super.initGeo(consumer, 1);
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
        if (pEntityLiving instanceof ServerPlayer player) {
            // Get the player's existing BlackHoleEntity
            BlackHoleEntity blackHoleEntity = playerBlackHoleMap.get(player.getUUID());
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
                blackHoleEntity = new BlackHoleEntity(ModEntities.BLACK_HOLE_ENTITY.get(), pLevel);
                blackHoleEntity.setOwner(player.getUUID());
                blackHoleEntity.setPos(player.getX(), player.getY() + 2, player.getZ());

                pLevel.addFreshEntity(blackHoleEntity);
                playerBlackHoleMap.put(player.getUUID(), blackHoleEntity);
            }
        }
    }

    public void setSpawnRadius(double spawnRadius) {
        this.spawnRadius = Math.min(spawnRadius, 20);
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
        if (!pLevel.isClientSide && pEntity instanceof ServerPlayer player && pIsSelected) {
            BlackHoleEntity blackHoleEntity = playerBlackHoleMap.get(player.getUUID());
            if (blackHoleEntity != null) {
                if (isSpawned(pStack) && pIsSelected) {
                    double distance = spawnRadius;
                    Vec3 playerLookVec = player.getViewVector(0.1F).normalize();
                    playerLookVec = playerLookVec.scale(distance).add(0, 1, 0);
                    Vec3 absVec = player.getPosition(0F).add(playerLookVec);

                    blackHoleEntity.lerpTo(absVec.x, absVec.y, absVec.z, 0, 0, 1, false);

                } else {
                    blackHoleEntity.discard();
                    playerBlackHoleMap.remove(player.getUUID());
                    setSpawned(pStack, false);
                }
            }

            if (player.isCrouching() && pStack.getOrCreateTag().getBoolean(ACCRETION)) {
                float suckFactor = 0.5F;
                if (tickCount % 4 == 0) {
                    player.connection.send(new ClientboundLevelParticlesPacket(ModParticles.BLACK_HOLE_PARTICLE.get(), true,
                            player.getX(), player.getY(), player.getZ(),
                            2, 2, 2, 10, 1));
                }


                AABB pullBox = new AABB(player.position(), player.position()).inflate(5);

                List<Entity> entities = pLevel.getEntities(player, pullBox, entity -> entity instanceof LivingEntity);

                for (Entity target : entities) {
                    // Check if the target is in line of sight and not underwater
                    Vec3 targetPosition = target.position();
                    Vec3 targetDirection = (player.position().subtract(targetPosition)).scale(suckFactor);

                    // Add delta movement to the target
                    target.addDeltaMovement(targetDirection);

                    // If the target is a ServerPlayer, send a motion packet to update its position on client
                    if (target instanceof ServerPlayer serverTarget) {
                        // Send the packet only for the target player, not for player
                        serverTarget.connection.send(new ClientboundSetEntityMotionPacket(target));
                    }

                    if (target.distanceTo(player) < 1 && tickCount % 4 == 0) {
                        target.hurt(target.damageSources().flyIntoWall(), 1);
                    }
                }
            } else pStack.getOrCreateTag().putBoolean(ACCRETION, false);
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);

    }

    public boolean isSpawned(ItemStack pStack) {
        return pStack.getOrCreateTag().getBoolean(SPAWNED);
    }

    public void setSpawned(ItemStack pStack, boolean pBoolean) {
        pStack.getOrCreateTag().putBoolean(SPAWNED, pBoolean);
    }

    private PlayState predicate(AnimationState<GeoAnimatable> animationState) {
        ItemStack stack = (ItemStack) animationState.getData(DataTickets.ITEMSTACK);
        if (isSpawned(stack)) {
            animationState.getController().setAnimation(RawAnimation.begin().thenPlayAndHold("throw"));
        } else {
            animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 0, this::predicate));
    }

}
