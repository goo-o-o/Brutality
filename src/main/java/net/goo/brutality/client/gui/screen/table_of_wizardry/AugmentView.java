package net.goo.brutality.client.gui.screen.table_of_wizardry;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.components.AbstractWidgetList;
import net.goo.brutality.client.gui.components.EnhancedStringWidget;
import net.goo.brutality.client.gui.screen.TableOfWizardryScreen;
import net.goo.brutality.util.ModResources;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class AugmentView extends TableOfWizardryView {
    public AugmentView(TableOfWizardryScreen screen) {
        super(screen);
    }

    @Override
    public void init() {
        int top = screen.height / 2 - 82;
        int leftColumnX = screen.width / 2 - 82;
        int midColumnX = screen.width / 2 + 6;

        AbstractWidgetList descriptionList = new AbstractWidgetList(mc, 76, 104, top, leftColumnX, 0, 0, 5);

        descriptionList.add(new EnhancedStringWidget.Builder(70, null,
                Component.translatable("message." + Brutality.MOD_ID + ".table_of_wizardry.augment.description.1").withStyle(s -> s.withFont(ModResources.ICON_FONT).withColor(ChatFormatting.WHITE)).withStyle(style),
                mc.font, 0).alignCenter().wordWrap().dropShadow(false).build());

        screen.addRenderableWidget(descriptionList);

        AbstractWidgetList descriptionListTwo = new AbstractWidgetList(mc, 76, 104, top, midColumnX, 0, 0, 5);

        descriptionListTwo.add(new EnhancedStringWidget.Builder(70, null,
                Component.translatable("message." + Brutality.MOD_ID + ".table_of_wizardry.augment.description.2").withStyle(s -> s.withFont(ModResources.ICON_FONT).withColor(ChatFormatting.WHITE)).withStyle(style),
                mc.font, 0).alignCenter().wordWrap().dropShadow(false).build());

        screen.addRenderableWidget(descriptionListTwo);
    }


    @Override
    public void setFocused(boolean pFocused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

}
