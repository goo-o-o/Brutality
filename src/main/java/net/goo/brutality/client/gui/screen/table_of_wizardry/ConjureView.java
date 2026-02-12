package net.goo.brutality.client.gui.screen.table_of_wizardry;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.components.AbstractWidgetList;
import net.goo.brutality.client.gui.components.EnhancedTextAndImageButton;
import net.goo.brutality.client.gui.screen.TableOfWizardryScreen;
import net.goo.brutality.common.block.block_entity.TableOfWizardryBlockEntity;
import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.common.registry.BrutalitySpells;
import net.goo.brutality.util.ModResources;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class ConjureView extends TableOfWizardryView {

    public ConjureView(TableOfWizardryScreen screen) {
        super(screen);
    }

    @Override
    public void init() {
        int top = screen.height / 2 - 82;
        int schoolX = screen.width / 2 - 82;
        int spellX = screen.width / 2 + 6;

        TableOfWizardryBlockEntity blockEntity = screen.getBlockEntity();
        Minecraft minecraft = Minecraft.getInstance();
        // School list
        AbstractWidgetList schoolList = new AbstractWidgetList(minecraft, 76, 104, top, schoolX, 0, 5);
        for (IBrutalitySpell.MagicSchool school : IBrutalitySpell.MagicSchool.values()) {
            String name = school.name().toLowerCase(Locale.ROOT);
            EnhancedTextAndImageButton button = new EnhancedTextAndImageButton.Builder(Component.translatable("school." + Brutality.MOD_ID + "." + name)
                    .withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT)), TableOfWizardryScreen.BUTTON_TEXTURE, b -> {
                blockEntity.currentSchool = school;
                screen.rebuildWidgets();
                screen.updateServerAndRefresh();
            })
                    .yDiffTex(18).texStart(0, 0).offset(0, 0).size(70, 18)
                    .withIcon(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/spells/" + school.name().toLowerCase(Locale.ROOT) + "/icon.png"), 0, 0, 18, 18)
                    .dropShadow(false).alignLeft().textOffset(4, 0).defaultColor(TableOfWizardryScreen.DARK_GRAY).activeColor(TableOfWizardryScreen.GRAY).hoverColor(TableOfWizardryScreen.LIGHT_GRAY)
                    .textureSize(70, 54).usedTextureSize(70, 18).build();

            button.active = school != blockEntity.currentSchool;
            schoolList.add(button);
        }
        screen.addRenderableWidget(schoolList);

        // Spell list (only if school selected)
        if (blockEntity.currentSchool != null) {
            AbstractWidgetList spellList = new AbstractWidgetList(minecraft, 76, 104, top, spellX, 0, 5);
            BrutalitySpells.getSpellsFromSchool(blockEntity.currentSchool).forEach(spell -> {

                EnhancedTextAndImageButton button = new EnhancedTextAndImageButton.Builder(spell.getTranslatedSpellName()
                        .withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT)), TableOfWizardryScreen.BUTTON_TEXTURE, b -> {
                    blockEntity.currentState = TableOfWizardryBlockEntity.GuiState.SPELL_PAGE;
                    blockEntity.currentSpell = spell;
                    screen.rebuildWidgets();
                    screen.updateServerAndRefresh();
                })
                        .yDiffTex(18).texStart(0, 0).offset(0, 0).size(70, 18)
                        .dropShadow(false).alignCenter().defaultColor(TableOfWizardryScreen.DARK_GRAY).activeColor(TableOfWizardryScreen.GRAY).hoverColor(TableOfWizardryScreen.LIGHT_GRAY)
//                        .withIcon(spell.getIcon(), -2, 1, 16, 16)
                        .textureSize(70, 54).usedTextureSize(70, 18).build();

                button.active = spell != blockEntity.currentSpell;
                spellList.add(button);
            });
            screen.addRenderableWidget(spellList);
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
