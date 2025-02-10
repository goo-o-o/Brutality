package net.goo.armament.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.goo.armament.Armament;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public final class Keybindings {

    private static final String CATEGORY = "key.categories." + Armament.MOD_ID;

    public static final Lazy<KeyMapping> DASH_ABILITY_KEY =
            Lazy.of(() ->
                    new KeyMapping(
                            "key." + Armament.MOD_ID + ".dash_ability_key",
                            KeyConflictContext.IN_GAME,
                            InputConstants.Type.KEYSYM,
                            GLFW.GLFW_KEY_LEFT_ALT,
                            CATEGORY));
}