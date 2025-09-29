package net.goo.brutality.event.mod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.goo.brutality.Brutality;
import net.goo.brutality.item.curios.charm.DeckOfCards;
import net.goo.brutality.item.curios.charm.EmergencyMeeting;
import net.goo.brutality.item.curios.charm.LightSwitch;
import net.goo.brutality.item.curios.charm.VindicatorSteroids;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.mcreator.terramity.init.TerramityModKeyMappings;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

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

        LightSwitch.keyMappings.put(BrutalityTooltipHelper.ItemDescriptionComponents.ACTIVE, TerramityModKeyMappings.ACTIVE_ABILITY);
        EmergencyMeeting.keyMappings.put(BrutalityTooltipHelper.ItemDescriptionComponents.ACTIVE, TerramityModKeyMappings.ACTIVE_ABILITY);
        VindicatorSteroids.keyMappings.put(BrutalityTooltipHelper.ItemDescriptionComponents.ACTIVE, TerramityModKeyMappings.ACTIVE_ABILITY);
        DeckOfCards.keyMappings.put(BrutalityTooltipHelper.ItemDescriptionComponents.ACTIVE, TerramityModKeyMappings.ACTIVE_ABILITY);


    }




}