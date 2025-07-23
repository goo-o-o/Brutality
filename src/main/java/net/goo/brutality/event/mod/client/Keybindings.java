package net.goo.brutality.event.mod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.goo.brutality.Brutality;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Keybindings {

    private static final String CATEGORY = "key.categories." + Brutality.MOD_ID;

    public static final Lazy<KeyMapping> RAGE_ACTIVATE_KEY =
            Lazy.of(() ->
                    new KeyMapping(
                            "key." + Brutality.MOD_ID + ".rage_activate_key",
                            KeyConflictContext.IN_GAME,
                            InputConstants.Type.KEYSYM,
                            GLFW.GLFW_KEY_N,
                            CATEGORY));

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(Keybindings.RAGE_ACTIVATE_KEY.get());
    }




}