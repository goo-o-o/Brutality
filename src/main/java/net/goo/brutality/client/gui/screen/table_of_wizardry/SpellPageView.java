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
        if (this.screen.getBlockEntity().currentSpell == null) return;
        Minecraft mc = Minecraft.getInstance();

        ImageButton btn = new ImageButton(screen.width / 2 + 70, screen.height / 2 - 83,
                12, 10, 0, 0, 10, BACK_BUTTON,
                12, 30, b -> screen.showSection(TableOfWizardryBookSection.CONJURE));

        screen.addRenderableWidget(btn);

        int top = screen.height / 2 - 82;
        int leftX = screen.width / 2 - 82;
        int rightX  = screen.width / 2 + 6;

        TableOfWizardryBlockEntity blockEntity = screen.getBlockEntity();

        MutableComponent spellName = blockEntity.currentSpell.getTranslatedSpellName()
                .withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT));

        StringBuilder catBuilder = new StringBuilder();
        blockEntity.currentSpell.getCategories().forEach(c -> catBuilder.append(c.icon));
        Component categories = Component.literal(catBuilder.toString());

        MutableComponent desc = Component.empty();
        IntStream.rangeClosed(1, blockEntity.currentSpell.getDescriptionCount()).forEach(i ->
                desc.append(blockEntity.currentSpell.getSpellDescription(i)).append(". ")
        );
        MutableComponent formattedDesc = desc.withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT));

        AbstractWidgetList leftList = new AbstractWidgetList(mc, 76, 104, top, leftX, 0, 5);

        StringWidget title = new StringWidget(spellName, mc.font);
        StringWidget categoriesWidget = new StringWidget(categories, mc.font);
        StringWidget description = new StringWidget(formattedDesc, mc.font);
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
