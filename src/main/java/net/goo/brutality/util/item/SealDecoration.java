package net.goo.brutality.util.item;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.generic.augments.BrutalityAugmentItem;
import net.goo.brutality.common.item.generic.augments.BrutalitySealAugmentItem;
import net.goo.brutality.util.AugmentHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

public class SealDecoration implements IItemDecorator {

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        for (BrutalityAugmentItem augment : AugmentHelper.getAugmentsFromItem(stack)) {
            if (augment instanceof BrutalitySealAugmentItem sealAugmentItem) {
                ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/seals/" + sealAugmentItem + ".png");
                guiGraphics.blit(texture, xOffset, yOffset, 0, 0, 16, 16, 16, 16);
                break;
            }
        }
        return false;
    }
}