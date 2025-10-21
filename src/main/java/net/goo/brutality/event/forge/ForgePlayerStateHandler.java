package net.goo.brutality.event.forge;

import com.google.common.base.Suppliers;
import com.lowdragmc.photon.client.fx.EntityEffect;
import net.goo.brutality.Brutality;
import net.goo.brutality.item.BrutalityArmorMaterials;
import net.goo.brutality.item.weapon.axe.RhittaAxe;
import net.goo.brutality.item.weapon.generic.CreaseOfCreation;
import net.goo.brutality.item.weapon.spear.EventHorizonSpear;
import net.goo.brutality.item.weapon.sword.BladeOfTheRuinedKingSword;
import net.goo.brutality.item.weapon.sword.DullKnifeSword;
import net.goo.brutality.item.weapon.sword.SupernovaSword;
import net.goo.brutality.item.weapon.tome.BaseMagicTome;
import net.goo.brutality.item.weapon.trident.ThunderboltTrident;
import net.goo.brutality.mob_effect.TheVoidEffect;
import net.goo.brutality.network.ClientboundSyncCapabilitiesPacket;
import net.goo.brutality.network.ClientboundSyncItemCooldownPacket;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.EnvironmentColorManager;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSwapItemsEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import oshi.util.tuples.Pair;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static net.goo.brutality.util.ModResources.*;
import static net.goo.brutality.util.helpers.EnvironmentColorManager.resetAllColors;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgePlayerStateHandler {

    public static final Supplier<List<Item>> cdPersistItems = Suppliers.memoize(() -> List.of(
            BrutalityModItems.EVIL_KING_BOOSTER_PACK.get(), BrutalityModItems.EVIL_KING_RESPAWN_CARD.get(),
            BrutalityModItems.DIAMOND_BOOSTER_PACK.get(), BrutalityModItems.DIAMOND_RESPAWN_CARD.get(),
            BrutalityModItems.SILVER_BOOSTER_PACK.get(), BrutalityModItems.SILVER_RESPAWN_CARD.get()
    ));
    public static final Supplier<List<MobEffect>> boosterPackEffects = Suppliers.memoize(() -> List.of(
            MobEffects.SATURATION, MobEffects.MOVEMENT_SPEED, MobEffects.ABSORPTION, MobEffects.JUMP, MobEffects.DAMAGE_RESISTANCE, TerramityModMobEffects.IMMUNITY.get()));

    @SubscribeEvent
    public static void onSwitchItemHands(LivingSwapItemsEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.isHolding(BrutalityModItems.LAST_PRISM_ITEM.get())) {
                if (player.isUsingItem()) {
                    event.setCanceled(true);
                }
            }
        }
    }


    @SubscribeEvent
    public static void onAddEffect(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();
        MobEffect effect = instance.getEffect();

        if (!entity.level().isClientSide) {
            if (effect instanceof TheVoidEffect) {
                if (entity instanceof Player player) {
                    PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(player.getId(), player));
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
        Player newPlayer = event.getEntity();
        Player oldPlayer = event.getOriginal();
        if (event.isWasDeath()) {
            ItemCooldowns newCooldowns = newPlayer.getCooldowns();
            ItemCooldowns oldCooldowns = oldPlayer.getCooldowns();

            boolean shouldSync = false;
            for (Map.Entry<Item, ItemCooldowns.CooldownInstance> entry : oldCooldowns.cooldowns.entrySet()) {
                Item item = entry.getKey();

                if (cdPersistItems.get().contains(item)) {
                    ItemCooldowns.CooldownInstance cooldown = entry.getValue();
                    newCooldowns.cooldowns.put(item, new ItemCooldowns.CooldownInstance(cooldown.startTime, cooldown.endTime));
                    newCooldowns.tickCount = oldCooldowns.tickCount;
                    shouldSync = true;
                }
            }

            if (shouldSync)
                DelayedTaskScheduler.queueServerWork(newPlayer.level(), 1, () ->
                        PacketHandler.sendToPlayer(new ClientboundSyncItemCooldownPacket(newCooldowns.cooldowns, newCooldowns.tickCount), ((ServerPlayer) newPlayer)));
        }
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
            PacketHandler.sendToPlayer(new ClientboundSyncItemCooldownPacket(itemCooldowns.cooldowns, itemCooldowns.tickCount), player);
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
                stack -> stack.getItem() instanceof CreaseOfCreation,
                new HoldToggleAction(
                        (player) -> {
                            if (!(player.level() instanceof ServerLevel)) {
                                EntityEffect effect = new EntityEffect(CREASE_OF_CREATION_FX, player.level(), player, EntityEffect.AutoRotate.NONE);
                                effect.start();
                            }
                        },
                        (player) -> {
                            if (!(player.level() instanceof ServerLevel)) {
                                ModUtils.removeFX(player, CREASE_OF_CREATION_FX);
                            }
                        }
                )));
        TOGGLE_ACTIONS.add(new Pair<>(
                stack -> stack.getItem() instanceof BladeOfTheRuinedKingSword,
                new HoldToggleAction(
                        (player) -> {
                            if (!(player.level() instanceof ServerLevel)) {
                                EntityEffect effect = new EntityEffect(RUINED_AURA_FX, player.level(), player, EntityEffect.AutoRotate.NONE);
                                effect.start();
                            }
                        },
                        (player) -> {
                            if (!(player.level() instanceof ServerLevel)) {
                                ModUtils.removeFX(player, RUINED_AURA_FX);
                            }
                        }
                )));

        TOGGLE_ACTIONS.add(new Pair<>(
                stack -> stack.getItem() instanceof DullKnifeSword,
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
                stack -> stack.getItem() instanceof ThunderboltTrident,
                new HoldToggleAction(
                        (player) -> {
                            if (!(player.level() instanceof ServerLevel)) {
                                EntityEffect effect = new EntityEffect(LIGHTNING_AURA_FX, player.level(), player, EntityEffect.AutoRotate.NONE);
                                effect.start();
                            }
                        },
                        (player) -> {
                            if (!(player.level() instanceof ServerLevel)) {
                                ModUtils.removeFX(player, LIGHTNING_AURA_FX);
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
        return item instanceof EventHorizonSpear
                || item instanceof RhittaAxe
                || item instanceof BaseMagicTome;
    }

    private static void triggerItemLeftInventory(Player player, ItemStack oldStack) {
        Item item = oldStack.getItem();
        if (item instanceof EventHorizonSpear lance) {
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
                    if (!player.hasEffect(BrutalityModMobEffects.STONEFORM.get()))
                        player.playSound(SoundEvents.STONE_PLACE);
                    player.addEffect(new MobEffectInstance(BrutalityModMobEffects.STONEFORM.get(), 20, 0, false, true));
                } else if (player.hasEffect(BrutalityModMobEffects.STONEFORM.get())) {
                    player.removeEffect(BrutalityModMobEffects.STONEFORM.get());
                }
            }

            player.getCapability(BrutalityCapabilities.PLAYER_MANA_CAP).
                    ifPresent(cap ->
                    {
                        AttributeInstance maxManaAttr = player.getAttribute(ModAttributes.MAX_MANA.get());
                        AttributeInstance manaRegenAttr = player.getAttribute(ModAttributes.MANA_REGEN.get());
                        if (maxManaAttr != null && manaRegenAttr != null) {
                            if (cap.manaValue() <= maxManaAttr.getValue()) {
                                float additionalBonus = 0;
                                if (CuriosApi.getCuriosInventory(player).isPresent()) {
                                    ICuriosItemHandler handler = CuriosApi.getCuriosInventory(player).orElse(null);

                                    if (handler.isEquipped(BrutalityModItems.FORBIDDEN_ORB.get())) {
                                        if (cap.manaValue() <= maxManaAttr.getValue() * 0.3F) {
                                            player.kill();
                                            return;
                                        }
                                    }
                                }
                                cap.incrementMana(((float) ((manaRegenAttr.getValue() + additionalBonus) / 20)));
                            }
                            cap.setManaValue((float) Mth.clamp(cap.manaValue(), 0, maxManaAttr.getValue()));
                        }
                    });

        }

    }
}