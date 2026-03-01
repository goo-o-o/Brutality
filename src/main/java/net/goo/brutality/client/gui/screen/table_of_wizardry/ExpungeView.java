package net.goo.brutality.client.gui.screen.table_of_wizardry;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.components.AbstractWidgetList;
import net.goo.brutality.client.gui.components.EnhancedStringWidget;
import net.goo.brutality.client.gui.components.EnhancedTextAndImageButton;
import net.goo.brutality.client.gui.screen.TableOfWizardryScreen;
import net.goo.brutality.common.block.block_entity.TableOfWizardryBlockEntity;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.util.ModResources;
import net.goo.brutality.util.magic.SpellStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class ExpungeView extends TableOfWizardryView {

    public ExpungeView(TableOfWizardryScreen screen) {
        super(screen);
    }

    @Override
    public void init() {
        int top = screen.height / 2 - 82;
        int leftColumnX = screen.width / 2 - 82;
        int rightX = screen.width / 2 + 6;

        TableOfWizardryBlockEntity blockEntity = screen.getBlockEntity();
        // School list
        AbstractWidgetList descriptionList = new AbstractWidgetList(mc, 76, 104, top, leftColumnX, 0, 0, 5);

        descriptionList.add(new EnhancedStringWidget.Builder(70, null,
                Component.translatable("message." + Brutality.MOD_ID + ".table_of_wizardry.expunge.description.1").withStyle(s -> s.withFont(ModResources.ICON_FONT).withColor(ChatFormatting.WHITE)).withStyle(style),
                mc.font, 0).alignCenter().wordWrap().dropShadow(false).build());

        screen.addRenderableWidget(descriptionList);

        if (blockEntity.craftingItem != null) {
            AbstractWidgetList expungeList = new AbstractWidgetList(mc, 76, 104, top, rightX, 0,2, 5);
            SpellStorage.getSpells(blockEntity.craftingItem).forEach(spellEntry -> {
                BrutalitySpell spell = spellEntry.spell();
                int level = spellEntry.level();
                EnhancedTextAndImageButton button = new EnhancedTextAndImageButton.Builder(spell.getTranslatedSpellName().append(" | " + level)
                        .withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT)), TableOfWizardryScreen.BUTTON_TEXTURE, b -> {
                    blockEntity.expungeEntry = spellEntry;
                    screen.expungeListScroll = expungeList.getScrollDistance();
                    screen.updateServerAndRefresh();
                    screen.onClose();
                    blockEntity.tryStartCrafting(mc.player, blockEntity.craftingItem);
                })
                        .yDiffTex(18).texStart(0, 0).offset(0, 0).size(70, 18)
                        .dropShadow(false).alignCenter().defaultColor(TableOfWizardryScreen.DARK_GRAY).activeColor(TableOfWizardryScreen.GRAY).hoverColor(TableOfWizardryScreen.LIGHT_GRAY)
//                        .withIcon(spell.getIcon(), -2, 1, 16, 16)
                        .textureSize(70, 54).usedTextureSize(70, 18).build();

                button.active = spellEntry != blockEntity.expungeEntry;
                expungeList.add(button);
            });
            expungeList.setScrollDistance(screen.expungeListScroll);
            screen.addRenderableWidget(expungeList);
        }
    }

    @Override
    public void setFocused(boolean pFocused) {

    }

    @Override
    public boolean isFocused() {
        return true;
    }

}
