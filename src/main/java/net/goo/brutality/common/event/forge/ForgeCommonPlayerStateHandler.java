package net.goo.brutality.common.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.item.ItemEquipUnequipTriggerable;
import net.goo.brutality.common.item.ItemLeftInventoryTriggerable;
import net.goo.brutality.common.item.armor.BrutalityArmorMaterials;
import net.goo.brutality.common.item.weapon.generic.CreaseOfCreation;
import net.goo.brutality.common.item.weapon.sword.SupernovaSword;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.clientbound.ClientboundEquipmentChangePacket;
import net.goo.brutality.common.network.clientbound.ClientboundSyncItemCooldownPacket;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.CooldownUtils;
import net.goo.brutality.util.ModUtils;
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
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingSwapItemsEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

import static net.goo.brutality.util.EnvironmentColorManager.resetAllColors;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeCommonPlayerStateHandler {

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
        if (event.getEntity() instanceof ServerPlayer player) {
            BrutalityCapabilities.sync(player, BrutalityCapabilities.LOADOUTS);
        }
    }


    @SubscribeEvent
    public static void onPlayerCommonLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SupernovaSword.clearAsteroids(player, player.serverLevel());
            CreaseOfCreation.handleCreaseOfCreation(player);

        }
    }

    @SubscribeEvent
    public static void onPlayerCommonLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemCooldowns itemCooldowns = player.getCooldowns();
            PacketHandler.sendToPlayerClient(new ClientboundSyncItemCooldownPacket(itemCooldowns.cooldowns, itemCooldowns.tickCount), player);
        }

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
                if (oldCount > newCount && oldStack.getItem() instanceof ItemLeftInventoryTriggerable itemLeftInventoryTriggerable) {
                    itemLeftInventoryTriggerable.onLeaveInventory(player, oldStack);
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
            if (event.getItemStack().getItem() instanceof ItemLeftInventoryTriggerable itemLeftInventoryTriggerable)
                itemLeftInventoryTriggerable.onLeaveInventory(event.getEntity(), event.getItemStack());
        }
    }

    @SubscribeEvent
    public static void onPlayerUnequipItem(LivingEquipmentChangeEvent event) {
        if (!event.getSlot().isArmor()) {
            ItemStack from = event.getFrom();
            ItemStack to = event.getTo();

            if (from.getItem() instanceof ItemEquipUnequipTriggerable triggerable) {
                triggerable.onLeaveMainHand(event.getEntity(), from);
                if (event.getEntity() instanceof ServerPlayer player)
                    PacketHandler.sendToPlayerClient(new ClientboundEquipmentChangePacket(event.getEntity(), from, true), player);
            }
            if (to.getItem() instanceof ItemEquipUnequipTriggerable triggerable) {
                triggerable.onEnterMainHand(event.getEntity(), to);
                if (event.getEntity() instanceof ServerPlayer player)
                    PacketHandler.sendToPlayerClient(new ClientboundEquipmentChangePacket(event.getEntity(), to, false), player);
            }
        }

    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {

            Player player = event.player;

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