package net.goo.brutality.util;

import net.goo.brutality.Brutality;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

import java.util.Locale;

public class SealUtils implements IItemDecorator {
    private static final String SEAL = "seal";

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        SealUtils.SEAL_TYPE sealType = SealUtils.getSealType(stack);
        if (sealType != null) {
            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/seals/" + sealType.toString().toLowerCase(Locale.ROOT) + "_seal.png");
            guiGraphics.blit(texture, xOffset, yOffset, 0, 0, 16, 16, 16, 16);
            return false;
        }
        return false;
    }

    public enum SEAL_TYPE {
        BLACK,
        BLUE,
        GREEN,
        ORANGE,
        PINK,
        PURPLE,
        RED,
        TEAL,
        YELLOW,
    }

    public static SEAL_TYPE getSealType(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.hasTag()) return null;
        String seal = stack.getOrCreateTag().getString(SEAL);
        if (seal.isEmpty()) return null;
        try {
            return SEAL_TYPE.valueOf(seal.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }

    }

    public static void addSeal(ItemStack stack, SEAL_TYPE type) {
        stack.getOrCreateTag().putString(SEAL, type.name());
    }


}
