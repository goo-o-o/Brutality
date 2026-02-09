package net.goo.brutality.client.gui.screen.table_of_wizardry;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.components.AbstractWidgetList;
import net.goo.brutality.client.gui.screen.TableOfWizardryScreen;
import net.goo.brutality.common.block.block_entity.TableOfWizardryBlockEntity;
import net.goo.brutality.util.ModResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.stream.IntStream;

public class SpellPageView extends TableOfWizardryView {
    public static final ResourceLocation BACK_BUTTON = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/back_arrow.png");

    public SpellPageView(TableOfWizardryScreen screen) {
        super(screen);
    }

    @Override
    public void init() {
        if (this.screen.block.currentSpell == null) return;

        ImageButton btn = new ImageButton(screen.width / 2 + 70, screen.height / 2 - 83,
                12, 10, 0, 0, 10, BACK_BUTTON,
                12, 30, b -> screen.showSection(TableOfWizardryBookSection.CONJURE));

        screen.addRenderableWidget(btn);

        int top = screen.height / 2 - 82;
        int leftX = screen.width / 2 - 82;
        int rightX  = screen.width / 2 + 6;

        TableOfWizardryBlockEntity block = screen.block;

        MutableComponent spellName = block.currentSpell.getTranslatedSpellName()
                .withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT));

        StringBuilder catBuilder = new StringBuilder();
        block.currentSpell.getCategories().forEach(c -> catBuilder.append(c.icon));
        Component categories = Component.literal(catBuilder.toString());

        MutableComponent desc = Component.empty();
        IntStream.rangeClosed(1, block.currentSpell.getDescriptionCount()).forEach(i ->
                desc.append(block.currentSpell.getSpellDescription(i)).append(". ")
        );
        MutableComponent formattedDesc = desc.withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT));

        Minecraft mc = Minecraft.getInstance();
        AbstractWidgetList schoolList = new AbstractWidgetList(mc, 76, 104, top, leftX, 0, 5);
    }

    @Override
    public void setFocused(boolean pFocused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
