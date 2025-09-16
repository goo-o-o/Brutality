package net.goo.brutality.event.forge;

import com.lowdragmc.photon.client.fx.EntityEffect;
import net.goo.brutality.Brutality;
import net.goo.brutality.item.weapon.axe.RhittaAxe;
import net.goo.brutality.item.weapon.generic.CreaseOfCreationItem;
import net.goo.brutality.item.weapon.spear.EventHorizonSpear;
import net.goo.brutality.item.weapon.sword.BladeOfTheRuinedKingSword;
import net.goo.brutality.item.weapon.sword.DullKnifeSword;
import net.goo.brutality.item.weapon.sword.SupernovaSword;
import net.goo.brutality.item.weapon.tome.BaseMagicTome;
import net.goo.brutality.item.weapon.trident.ThunderboltTrident;
import net.goo.brutality.mob_effect.TheVoidEffect;
import net.goo.brutality.network.ClientboundSyncCapabilitiesPacket;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.EnvironmentColorManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
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

import static net.goo.brutality.util.ModResources.*;
import static net.goo.brutality.util.helpers.EnvironmentColorManager.resetAllColors;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgePlayerStateHandler {

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
            CreaseOfCreationItem.handleCreaseOfCreation(player);
        }

        resetAllColors();
    }


    @SubscribeEvent
    public static void onRespawn(PlayerEvent.Clone event) {
        Player newPlayer = event.getEntity();
//        if (event.isWasDeath()) {
//            ItemCooldowns cooldowns = newPlayer.getCooldowns();
//            if (cooldowns.isOnCooldown(BrutalityModItems.SILVER_RESPAWN_CARD.get())) return;
//            newPlayer.getCapability(BrutalityCapabilities.RESPAWN_CARD_CAP).ifPresent(cap -> {
//                if (cap.getCardType() == EntityCapabilities.EntityRespawnCardCap.CARD_TYPE.SILVER) {
//                    newPlayer.getInventory().armor.set(0, new ItemStack(Items.IRON_BOOTS));
//                    newPlayer.getInventory().armor.set(1, new ItemStack(Items.IRON_LEGGINGS));
//                    newPlayer.getInventory().armor.set(2, new ItemStack(Items.IRON_CHESTPLATE));
//                    newPlayer.getInventory().armor.set(3, new ItemStack(Items.IRON_HELMET));
//
//                    newPlayer.getInventory().add(new ItemStack(Items.GOLDEN_APPLE, 2));
//                    newPlayer.getInventory().add(new ItemStack(Items.COOKED_BEEF, 8));
//                    newPlayer.getInventory().add(new ItemStack(Items.IRON_SWORD));
//                    newPlayer.getInventory().add(new ItemStack(Items.IRON_PICKAXE));
//                    newPlayer.getInventory().add(new ItemStack(Items.IRON_AXE));
//                } else if (cap.getCardType() == EntityCapabilities.EntityRespawnCardCap.CARD_TYPE.DIAMOND) {
//                    newPlayer.getInventory().armor.set(0, new ItemStack(Items.IRON_BOOTS));
//                    newPlayer.getInventory().armor.set(1, new ItemStack(Items.IRON_LEGGINGS));
//                    newPlayer.getInventory().armor.set(2, new ItemStack(Items.IRON_CHESTPLATE));
//                    newPlayer.getInventory().armor.set(3, new ItemStack(Items.IRON_HELMET));
//
//                    newPlayer.getInventory().add(new ItemStack(Items.GOLDEN_APPLE, 4));
//                    newPlayer.getInventory().add(new ItemStack(Items.COOKED_BEEF, 16));
//                    newPlayer.getInventory().add(new ItemStack(Items.DIAMOND_SWORD));
//                    newPlayer.getInventory().add(new ItemStack(Items.DIAMOND_PICKAXE));
//                    newPlayer.getInventory().add(new ItemStack(Items.DIAMOND_AXE));
//                }
//            });
//        }
    }


    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SupernovaSword.clearAsteroids(player, player.serverLevel());
            CreaseOfCreationItem.handleCreaseOfCreation(player);


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
                stack -> stack.getItem() instanceof CreaseOfCreationItem,
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
                        (player) -> EnvironmentColorManager.resetAllColors()
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
    public static void onItemPutInFrame(PlayerInteractEvent.EntityInteract event) {
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

    private static final UUID CHOPSTICK_AS_UUID = UUID.fromString("cd9b5958-a78b-4391-af97-826d4379f7f0");
    private static final UUID CHOPSTICK_KB_UUID = UUID.fromString("3f0426a3-2e8a-4d29-b06e-7a470bf28015");

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


            AttributeInstance knockback = player.getAttribute(Attributes.ATTACK_KNOCKBACK);
            AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
            boolean hasASMult = attackSpeed != null && attackSpeed.getModifier(CHOPSTICK_AS_UUID) != null;
            boolean hasKBMult = knockback != null && knockback.getModifier(CHOPSTICK_KB_UUID) != null;

            if (player.getMainHandItem().

                    is(BrutalityModItems.CHOPSTICK_STAFF.get()) && player.getOffhandItem().

                    is(BrutalityModItems.CHOPSTICK_STAFF.get())) {
                if (attackSpeed != null & !hasASMult) {
                    attackSpeed.addTransientModifier(
                            new AttributeModifier(
                                    CHOPSTICK_AS_UUID,
                                    "Temporary AS Bonus",
                                    0.5,
                                    AttributeModifier.Operation.ADDITION
                            )
                    );
                }

                if (knockback != null && !hasKBMult) {
                    knockback.addTransientModifier(
                            new AttributeModifier(
                                    CHOPSTICK_KB_UUID,
                                    "Temporary KB Bonus",
                                    2,
                                    AttributeModifier.Operation.ADDITION
                            )
                    );
                }
            } else {
                if (attackSpeed != null) attackSpeed.removeModifier(CHOPSTICK_AS_UUID);
                if (knockback != null) knockback.removeModifier(CHOPSTICK_KB_UUID);
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