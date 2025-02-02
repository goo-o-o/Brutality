package net.goo.armament.event;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientTooltipEventsBus {
private static Font customFont;

    @SubscribeEvent
    public static void tooltipFontHandler(RenderTooltipEvent.Pre event) {
        loadCustomFont();
    }

    @SubscribeEvent
    public static void tooltipColorHandler(RenderTooltipEvent.Color event) {
        @NotNull Item stack = event.getItemStack().getItem();

        if (stack instanceof EventHorizonLanceItem) {
            event.setBorderStart(toARGB(255, 250, 140, 20));
            event.setBorderEnd(toARGB(255, 25, 25, 25));
        }

        if (stack instanceof TerraBladeSwordItem) {
            event.setBorderStart(toARGB(255, 174, 229, 58));
            event.setBorderEnd(toARGB(255, 0, 82, 60));
        }

        if (stack instanceof SupernovaSwordItem) {
            event.setBorderStart(toARGB(255, 255, 255, 222));
            event.setBorderEnd(toARGB(255, 90, 37, 131));
        }

        if (stack instanceof DoomfistGauntletItem) {
            event.setBorderStart(toARGB(255, 237, 205, 140));
            event.setBorderEnd(toARGB(255, 118, 118, 118));
        }

        if (stack instanceof JackpotHammerItem) {
            event.setBorderStart(toARGB(255, 85, 219, 220));
            event.setBorderEnd(toARGB(255, 26, 231, 0));
        }

        if (stack instanceof DivineRhittaAxeItem) {
            event.setBorderStart(toARGB(255, 255, 253, 112));
            event.setBorderEnd(toARGB(255, 86, 73, 191));
        }

        if (stack instanceof TruthseekerSwordItem) {
            event.setBorderStart(toARGB(255, 128, 244, 58));
            event.setBorderEnd(toARGB(255, 93, 33, 0));
        }

        if (stack instanceof LeafBlowerItem) {
            event.setBorderStart(toARGB(255, 212, 6, 6));
            event.setBorderEnd(toARGB(255, 255, 255, 255));
        }

        if (stack instanceof TerratonHammerItem) {
            event.setBorderStart(toARGB(255, 186, 198, 195));
            event.setBorderEnd(toARGB(255, 19, 26, 25));
        }

        if (stack instanceof ZeusThunderboltItem) {
            event.setBorderStart(toARGB(255, 255, 215, 86));
            event.setBorderEnd(toARGB(255, 164, 92, 0));
        }

    }

    public static int toARGB(int a, int r, int g, int b) {
        // Ensure values are clamped between 0 and 255
        a = Math.max(0, Math.min(255, a));
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));

        // Combine the components into a single ARGB integer
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static void loadCustomFont() {
        try {
            // Load the font using a .ttf file from your assets
            ResourceLocation fontPath = new ResourceLocation(Armament.MOD_ID, "fonts/alagard.ttf");
            File fontFile = new File(fontPath.getPath());

            // Create the font from file
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(12f); // Adjust size as needed

            // Register the font
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace(); // Handle exceptions appropriately
            customFont = null; // Reset to null if there's an error
        }
    }
}
