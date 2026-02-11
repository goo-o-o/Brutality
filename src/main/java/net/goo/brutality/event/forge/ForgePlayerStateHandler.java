package net.goo.brutality.event.forge;

import com.lowdragmc.photon.client.fx.EntityEffect;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.BrutalityArmorMaterials;
import net.goo.brutality.common.item.weapon.axe.RhittaAxe;
import net.goo.brutality.common.item.weapon.generic.CreaseOfCreation;
import net.goo.brutality.common.item.weapon.spear.EventHorizon;
import net.goo.brutality.common.item.weapon.sword.DullKnifeSword;
import net.goo.brutality.common.item.weapon.sword.SupernovaSword;
import net.goo.brutality.common.item.weapon.tome.BaseMagicTome;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.clientbound.ClientboundSyncItemCooldownPacket;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.ClientModResources;
import net.goo.brutality.util.CooldownUtils;
import net.goo.brutality.util.EnvironmentColorManager;
import net.goo.brutality.util.ModUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSwapItemsEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static net.goo.brutality.util.EnvironmentColorManager.resetAllColors;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgePlayerStateHandler {
    // 仅客户端加载
    @OnlyIn(Dist.CLIENT)
    public class ClientFXUtils {
        // 封装FX的获取逻辑，仅客户端可用
        public static FX getCreaseOfCreationFX() {
            return FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "crease_of_creation_particle"));
        }
    }
    @SubscribeEvent
    public static void onSwitchItemHands(LivingSwapItemsEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.isHolding(BrutalityItems.LAST_PRISM_ITEM.get())) {
                if (player.isUsingItem()) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {

        Player player = event.getEntity();
        if (player.level() instanceof ServerLevel serverLevel) {
            SupernovaSword.clearAsteroids(player, serverLevel);
            CreaseOfCreation.handleCreaseOfCreation(player);
        }

        resetAllColors();
    }


    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
       CooldownUtils.persistCooldowns(event);
    }


    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SupernovaSword.clearAsteroids(player, player.serverLevel());
            CreaseOfCreation.handleCreaseOfCreation(player);

        }
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemCooldowns itemCooldowns = player.getCooldowns();
            PacketHandler.sendToPlayerClient(new ClientboundSyncItemCooldownPacket(itemCooldowns.cooldowns, itemCooldowns.tickCount), player);
        }

    }


    @FunctionalInterface
    public interface HoldEvent {
        void run(Player player);
    }

    public record HoldToggleAction(HoldEvent onHold, HoldEvent onRelease) {
    }

    private static final List<Pair<Predicate<ItemStack>, HoldToggleAction>> TOGGLE_ACTIONS = new ArrayList<>();

    static {
        TOGGLE_ACTIONS.add(new Pair<>(
                stack -> stack.is(BrutalityItems.CREASE_OF_CREATION.get()),
                new HoldToggleAction(
                        (player) -> {
                            if (!(player.level() instanceof ServerLevel)) {
                                // 直接引用CREASE_OF_CREATION_FX → 静态初始化阶段就触发FXHelper加载
                                EntityEffect effect = new EntityEffect(ClientModResources.CREASE_OF_CREATION_FX, player.level(), player, EntityEffect.AutoRotate.NONE);
                                effect.start();
                            }
                        },
                        (player) -> {
                            if (!(player.level() instanceof ServerLevel)) {
                                ModUtils.removeFX(player, ClientModResources.CREASE_OF_CREATION_FX);
                            }
                        }
                )));
//        TOGGLE_ACTIONS.add(new Pair<>(
//                stack -> stack.getItem() instanceof BladeOfTheRuinedKingSword,
//                new HoldToggleAction(
//                        (player) -> {
//                            if (!(player.level() instanceof ServerLevel)) {
//                                EntityEffect effect = new EntityEffect(RUINED_AURA_FX, player.level(), player, EntityEffect.AutoRotate.NONE);
//                                effect.start();
//                            }
//                        },
//                        (player) -> {
//                            if (!(player.level() instanceof ServerLevel)) {
//                                ModUtils.removeFX(player, RUINED_AURA_FX);
//                            }
//                        }
//                )));

        TOGGLE_ACTIONS.add(new Pair<>(
                stack -> stack.is(BrutalityItems.DULL_KNIFE_DAGGER.get()),
                new HoldToggleAction(
                        (player) -> {
                            if (player.level().isClientSide()) {
                                int emotionIndex = ModUtils.getTextureIdx(player.getMainHandItem());
                                DullKnifeSword.EmotionColor emotion = DullKnifeSword.EmotionColor.byId(emotionIndex);
                                EnvironmentColorManager.setColor(EnvironmentColorManager.ColorType.SKY, emotion.primaryColor);
                                EnvironmentColorManager.setColor(EnvironmentColorManager.ColorType.FOG, emotion.secondaryColor);
                            }
                        },
                        (player) -> resetAllColors()
                )));


        TOGGLE_ACTIONS.add(new Pair<>(
                stack -> stack.getItem() instanceof BaseMagicTome,
                new HoldToggleAction(
                        (player) -> {
                        },
                        (player) -> {
                            ItemStack main = player.getMainHandItem();
                            if (main.getItem() instanceof BaseMagicTome tome) {
                                tome.closeBook(player, main);
                            }
                        }
                )));

        TOGGLE_ACTIONS.add(new Pair<>(
                stack -> stack.is(BrutalityItems.THUNDERBOLT_TRIDENT.get()),
                new HoldToggleAction(
                        (player) -> {
                            if (!(player.level() instanceof ServerLevel)) {
                                EntityEffect effect = new EntityEffect(ClientModResources.LIGHTNING_AURA_FX, player.level(), player, EntityEffect.AutoRotate.NONE);
                                effect.start();
                            }
                        },
                        (player) -> {
                            if (!(player.level() instanceof ServerLevel)) {
                                ModUtils.removeFX(player, ClientModResources.LIGHTNING_AURA_FX);
                            }
                        }
                )));


    }


    @SubscribeEvent
    public static void onPlayerContainerOpen(PlayerContainerEvent.Open event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return; // server only

        AbstractContainerMenu menu = event.getContainer();

        // Track only player inventory slots
        Map<Integer, ItemStack> initialContents = new HashMap<>();
        for (int i = 0; i < menu.slots.size(); i++) {
            Slot slot = menu.getSlot(i);
            if (slot.container == player.getInventory()) {
                initialContents.put(i, slot.getItem().copy());
            }
        }

        menu.addSlotListener(new ContainerListener() {
            @Override
            public void slotChanged(AbstractContainerMenu menu, int slotIndex, ItemStack newStack) {

                if (!initialContents.containsKey(slotIndex)) return; // not a player slot

                ItemStack oldStack = initialContents.get(slotIndex);
                int oldCount = oldStack.getCount();
                int newCount = newStack.getCount();

                // If count decreased for our specific item
                if (oldCount > newCount && isSpecificItem(oldStack)) {
                    triggerItemLeftInventory(player, oldStack);
                }

                // update stored state
                initialContents.put(slotIndex, newStack.copy());
            }

            @Override
            public void dataChanged(AbstractContainerMenu menu, int slotIndex, int data) {
            }
        });
    }


    @SubscribeEvent
    public static void onItemPutInFrame(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getTarget() instanceof ItemFrame) {
            if (isSpecificItem(event.getItemStack())) {
                triggerItemLeftInventory(event.getEntity(), event.getItemStack());
            }
        }
    }

    private static boolean isSpecificItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Item item = stack.getItem();
        return item instanceof EventHorizon
                || item instanceof RhittaAxe
                || item instanceof BaseMagicTome;
    }

    private static void triggerItemLeftInventory(Player player, ItemStack oldStack) {
        Item item = oldStack.getItem();
        if (item instanceof EventHorizon lance) {
            lance.despawnBlackHole(player, oldStack);
        } else if (item instanceof RhittaAxe axe) {
            axe.despawnCruelSun(player, oldStack);
        } else if (item instanceof BaseMagicTome tome) {
            tome.tryCloseBook(player, oldStack);
        }
    }


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {

            Player player = event.player;

            for (var entry : TOGGLE_ACTIONS) {
                Predicate<ItemStack> matcher = entry.getA();
                HoldToggleAction action = entry.getB();

                boolean isHolding = matcher.test(player.getMainHandItem()) || matcher.test(player.getOffhandItem());
                String tagKey = "holding_action_" + TOGGLE_ACTIONS.indexOf(entry); // unique per entry

                boolean wasHolding = player.getPersistentData().getBoolean(tagKey);
                if (isHolding && !wasHolding) {
//                    System.out.println("running OnHold");
                    action.onHold().run(player);
                    player.getPersistentData().putBoolean(tagKey, true);
                } else if (!isHolding && wasHolding) {
//                    System.out.println("running onRelease");
                    action.onRelease().run(player);
                    player.getPersistentData().putBoolean(tagKey, false);
                }
            }

            if (ModUtils.hasFullArmorSet(player, BrutalityArmorMaterials.TERRA)) {
                if (player.isCrouching()) {
                    if (!player.hasEffect(BrutalityEffects.STONEFORM.get()))
                        player.playSound(SoundEvents.STONE_PLACE);
                    player.addEffect(new MobEffectInstance(BrutalityEffects.STONEFORM.get(), 20, 0, false, true));
                } else if (player.hasEffect(BrutalityEffects.STONEFORM.get())) {
                    player.removeEffect(BrutalityEffects.STONEFORM.get());
                }

            } else if (ModUtils.hasFullArmorSet(player, BrutalityArmorMaterials.VAMPIRE_LORD)) {
                if (player.tickCount % 20 == 0) {
                    player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 21, 1, true, false, false));
                }
            }

        }

    }
}