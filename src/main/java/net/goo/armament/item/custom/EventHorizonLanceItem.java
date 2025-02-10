package net.goo.armament.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.armament.Armament;
import net.goo.armament.client.item.renderer.EventHorizonLanceItemRenderer;
import net.goo.armament.entity.ModEntities;
import net.goo.armament.entity.custom.BlackHoleEntity;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.util.ModUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.*;
import java.util.function.Consumer;

import static net.goo.armament.util.ModResources.SPACE;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHorizonLanceItem extends TridentItem implements Vanishable, GeoItem {
    private static final String SPAWNED = "blackHoleSpawned";
    private static final UUID SPEED_BOOST_UUID = UUID.fromString("f9d0a647-4999-4637-b4a0-7f768a65b5db");  // Unique UUID for speed boost modifier
    private static final Map<UUID, BlackHoleEntity> playerBlackHoleMap = new HashMap<>();
    private double spawnRadius;
    int[] color1 = new int[]{250, 140, 20};
    int[] color2 = new int[]{50, 50, 50};
    private final ModItemCategories category;

    public EventHorizonLanceItem(Properties pProperties, ModItemCategories category) {
        super(pProperties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 8.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.9F, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(SPEED_BOOST_UUID, "Tool modifier",2, AttributeModifier.Operation.ADDITION));
        Multimap<Attribute, AttributeModifier> defaultModifiers = builder.build();
        this.category = category;
    }

    public ModItemCategories getCategory() {
        return category;
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.event_horizon.desc.1", false, null, color2));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.event_horizon.desc.2", false, null, color1));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.event_horizon.desc.3", false, null, color2));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return ModUtils.tooltipHelper("item.armament.event_horizon", false, SPACE, color1, color2);
    }



    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
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
            }
            else {
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
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.fail(itemstack);
    }


    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide && pEntity instanceof ServerPlayer player && pIsSelected) {
            BlackHoleEntity blackHoleEntity = playerBlackHoleMap.get(player.getUUID());
            if (blackHoleEntity != null) {
                if (isSpawned(pStack) && pIsSelected) {

                    double distance = spawnRadius;
                    Vec3 playerLookVec = player.getViewVector(1F).normalize();
                    playerLookVec = playerLookVec.scale(distance).add(0, 1, 0);
                    Vec3 absVec = player.getPosition(1F).add(playerLookVec);

                    blackHoleEntity.lerpTo(absVec.x, absVec.y, absVec.z, 0, 0, 1, false);

                } else {
                    blackHoleEntity.discard();
                    playerBlackHoleMap.remove(player.getUUID());
                    setSpawned(pStack, false);
                }
            }
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    public boolean isSpawned(ItemStack pStack) {
        return pStack.getOrCreateTag().getBoolean(SPAWNED);
    }

    public void setSpawned(ItemStack pStack, boolean pBoolean) {
        pStack.getOrCreateTag().putBoolean(SPAWNED, pBoolean);
    }

    private PlayState predicate(AnimationState animationState) {
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

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private EventHorizonLanceItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(this.renderer == null) {
                    renderer = new EventHorizonLanceItemRenderer();
                }
                return this.renderer;
            }
        });
    }

}
