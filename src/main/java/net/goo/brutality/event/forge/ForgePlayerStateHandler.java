package net.goo.brutality.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.goo.brutality.item.weapon.axe.RhittaAxe;
import net.goo.brutality.item.weapon.generic.CreaseOfCreationItem;
import net.goo.brutality.item.weapon.lance.EventHorizonLance;
import net.goo.brutality.item.weapon.sword.SupernovaSword;
import net.goo.brutality.magic.SpellCooldownTracker;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.ModAttributes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSwapItemsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.goo.brutality.util.helpers.EnvironmentColorManager.resetAllColors;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
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
    public static void onChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {

        Player player = event.getEntity();
        if (player.level() instanceof ServerLevel serverLevel) {
            SupernovaSword.clearAsteroids(player, serverLevel);
            CreaseOfCreationItem.handleCreaseOfCreation(player);
        }

        resetAllColors();
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {


        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (player.level() instanceof ServerLevel serverLevel) {
                SupernovaSword.clearAsteroids(player, serverLevel);
                CreaseOfCreationItem.handleCreaseOfCreation(player);
            }

            resetAllColors();
        }

    }


//    @SubscribeEvent
//    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
//        if (event.getEntity() instanceof ServerPlayer player) {
//        }
//    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SupernovaSword.clearAsteroids(player, player.serverLevel());
            CreaseOfCreationItem.handleCreaseOfCreation(player);


        }
    }

    @SubscribeEvent
    public static void onPlaceInItemFrame(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!(event.getLevel() instanceof ServerLevel)) return;

        Entity target = event.getTarget();
        Player player = event.getEntity();

        if (target instanceof ItemFrame itemFrame) {
            ItemStack heldItem = player.getMainHandItem();

            if (itemFrame.getItem().isEmpty() && !heldItem.isEmpty()) {
                if (heldItem.getItem() instanceof EventHorizonLance eventHorizonLance) {
                    eventHorizonLance.despawnBlackHole(player, heldItem);
                }
                if (heldItem.getItem() instanceof RhittaAxe rhittaAxe) {
                    rhittaAxe.despawnCruelSun(player, heldItem);
                }
            }
        }
    }



    private static final Map<Player, Integer> lastSelectedSlot = new HashMap<>();

    private static final UUID CHOPSTICK_AS_UUID = UUID.fromString("cd9b5958-a78b-4391-af97-826d4379f7f0");
    private static final UUID CHOPSTICK_KB_UUID = UUID.fromString("3f0426a3-2e8a-4d29-b06e-7a470bf28015");


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Player player = event.player;
        int currentSlot = player.getInventory().selected;
        int lastSlot = lastSelectedSlot.getOrDefault(player, -1);

        if (currentSlot != lastSlot) {
            ItemStack deselected = lastSlot >= 0 && lastSlot < 9 ? player.getInventory().getItem(lastSlot) : ItemStack.EMPTY;

            if (deselected.getItem() instanceof BrutalityGeoItem item) {
                item.onDeselected(player, deselected);
            }
        }

        lastSelectedSlot.put(player, currentSlot);


        AttributeInstance knockback = player.getAttribute(Attributes.ATTACK_KNOCKBACK);
        AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
        boolean hasASMult = attackSpeed != null && attackSpeed.getModifier(CHOPSTICK_AS_UUID) != null;
        boolean hasKBMult = knockback != null && knockback.getModifier(CHOPSTICK_KB_UUID) != null;

        if (player.getMainHandItem().is(BrutalityModItems.CHOPSTICK_STAFF.get()) && player.getOffhandItem().is(BrutalityModItems.CHOPSTICK_STAFF.get())) {
            if (attackSpeed != null & !hasASMult) {
                System.out.println("adding Attack Speed");
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
                System.out.println("adding Knockback");

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

        SpellCooldownTracker.tick(event.player);

        event.player.getCapability(BrutalityCapabilities.PLAYER_MANA_CAP).ifPresent(cap -> {
            AttributeInstance maxManaAttr = event.player.getAttribute(ModAttributes.MAX_MANA.get());
            AttributeInstance manaRegenAttr = event.player.getAttribute(ModAttributes.MANA_REGEN.get());
            if (maxManaAttr != null && manaRegenAttr != null) {
                if (cap.manaValue() <= maxManaAttr.getValue()) {
                    cap.incrementMana(((float) (manaRegenAttr.getValue() / 20)));
                }
                cap.setManaValue((float) Mth.clamp(cap.manaValue(), 0, maxManaAttr.getValue()));
            }
        });

    }


}