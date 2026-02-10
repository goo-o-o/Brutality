package net.goo.brutality.client.gui.screen.table_of_wizardry;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.components.AbstractWidgetList;
import net.goo.brutality.client.gui.components.EnhancedStringWidget;
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
        if (this.screen.getBlockEntity().currentSpell == null) return;
        Minecraft mc = Minecraft.getInstance();

        ImageButton btn = new ImageButton(screen.width / 2 + 70, screen.height / 2 - 83,
                12, 10, 0, 0, 10, BACK_BUTTON,
                12, 30, b -> {
            screen.showSection(TableOfWizardryBookSection.CONJURE);
            screen.getBlockEntity().currentSpell = null;
        });

        screen.addRenderableWidget(btn);

        int top = screen.height / 2 - 82;
        int leftX = screen.width / 2 - 82;
        int rightX  = screen.width / 2 + 6;

        TableOfWizardryBlockEntity blockEntity = screen.getBlockEntity();

        MutableComponent spellName = blockEntity.currentSpell.getTranslatedSpellName()
                .withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT));

        MutableComponent categories = Component.empty();
        blockEntity.currentSpell.getCategories().forEach(c -> categories.append(c.icon));

        MutableComponent desc = Component.empty();
        IntStream.rangeClosed(1, blockEntity.currentSpell.getDescriptionCount()).forEach(i ->
                desc.append(blockEntity.currentSpell.getSpellDescription(i)).append(". ")
        );
        MutableComponent formattedDesc = desc.withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT));

        AbstractWidgetList leftList = new AbstractWidgetList(mc, 76, 104, top, leftX, 0, 5);

        StringWidget title = new StringWidget(78, mc.font.lineHeight, spellName, mc.font).alignCenter();
        StringWidget categoriesWidget = new StringWidget(78, mc.font.lineHeight, categories, mc.font).alignCenter();
        EnhancedStringWidget description = new EnhancedStringWidget(78, formattedDesc, mc.font, true);
        leftList.add(title);
        leftList.add(categoriesWidget);
        leftList.add(description);
        screen.addRenderableWidget(leftList);
    }

    @Override
    public void setFocused(boolean pFocused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
