package net.goo.brutality.event.mod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.meters.CooldownMeter;
import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.serverbound.ServerboundActivateRagePacket;
import net.goo.brutality.common.network.serverbound.ServerboundActiveAbilityPressPacket;
import net.goo.brutality.common.network.serverbound.ServerboundArmorSetBonusAbilityPressPacket;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.BrutalityTags;
import net.mcreator.terramity.init.TerramityModKeyMappings;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Keybindings {

    private static final String CATEGORY = "key.categories." + Brutality.MOD_ID;

    public static final KeyMapping RAGE_ACTIVATE_KEY =
            new KeyMapping(
                    "key." + Brutality.MOD_ID + ".rage_activate_key",
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_N,
                    CATEGORY);

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(Keybindings.RAGE_ACTIVATE_KEY);

    }

    public static Supplier<InputConstants.Key> getActiveAbilityKey() {
        return TerramityModKeyMappings.ACTIVE_ABILITY::getKey;
    }

    public static Supplier<InputConstants.Key> getArmorSetBonusAbilityKey() {
        return TerramityModKeyMappings.ARMOR_SET_BONUS_ABILITY::getKey;
    }

    public static Supplier<InputConstants.Key> getRageActivateKey() {
        return Keybindings.RAGE_ACTIVATE_KEY::getKey;
    }

    public static void handleKeyPress(InputEvent.Key event, LocalPlayer player) {
        if (event.getAction() == InputConstants.PRESS) {
            if (!player.hasEffect(BrutalityEffects.ENRAGED.get()))
                if (Keybindings.RAGE_ACTIVATE_KEY.consumeClick()) {
                    CuriosApi.getCuriosInventory(player).ifPresent(
                            handler -> {
                                if (!handler.findCurios(stack -> stack.is(BrutalityTags.Items.RAGE_ITEMS)).isEmpty()) {
                                    player.getCapability(BrutalityCapabilities.RAGE).ifPresent(cap -> {
                                        if (handler.isEquipped(BrutalityItems.ANGER_MANAGEMENT.get())) {
                                            PacketHandler.sendToServer(new ServerboundActivateRagePacket());
                                        }
                                    });

                                }
                            });
                }

            if (TerramityModKeyMappings.ACTIVE_ABILITY.consumeClick()) {
                PacketHandler.sendToServer(new ServerboundActiveAbilityPressPacket());
                CooldownMeter.AbilityCooldownMeter.updateActiveAbilityIcon(player);
            }

            if (TerramityModKeyMappings.ARMOR_SET_BONUS_ABILITY.consumeClick()) {
                PacketHandler.sendToServer(new ServerboundArmorSetBonusAbilityPressPacket());
            }
        }
    }
}