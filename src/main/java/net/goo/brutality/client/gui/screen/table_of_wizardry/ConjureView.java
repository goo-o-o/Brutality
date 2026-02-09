package net.goo.brutality.client.gui.screen.table_of_wizardry;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.components.AbstractWidgetList;
import net.goo.brutality.client.gui.components.TextImageButton;
import net.goo.brutality.client.gui.screen.TableOfWizardryScreen;
import net.goo.brutality.common.block.block_entity.TableOfWizardryBlockEntity;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.common.registry.BrutalitySpells;
import net.goo.brutality.util.ModResources;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class ConjureView extends TableOfWizardryView {
    public float schoolListScroll = 0, spellListScroll = 0;

    public ConjureView(TableOfWizardryScreen screen) {
        super(screen);
    }

    @Override
    public void init() {
        int top = screen.height / 2 - 82;
        int schoolX = screen.width / 2 - 82;
        int spellX  = screen.width / 2 + 6;

        Minecraft minecraft = Minecraft.getInstance();
        // School list
        AbstractWidgetList schoolList = new AbstractWidgetList(minecraft, 76, 104, top, schoolX, 0, 5);
        for (IBrutalitySpell.MagicSchool school : IBrutalitySpell.MagicSchool.values()) {
            String name = school.name().toLowerCase(Locale.ROOT);
            TextImageButton btn = new TextImageButton(
                    0, 0, 70, 18,
                    0, 0, 18,
                    TableOfWizardryScreen.BUTTON_TEXTURE,
                    70, 18 * 3,
                    b -> {
                        screen.block.currentSchool = school;
                        screen.rebuildWidgets();
                    },
                    Component.translatable("school." + Brutality.MOD_ID + "." + name)
                            .withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT))
            );
            btn.icon = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/spells/" + name + "/icon.png");
            btn.hoveredOrFocusedTextColor = TableOfWizardryScreen.LIGHT_GRAY;
            btn.notHoveredOrFocusedTextColor = TableOfWizardryScreen.GRAY;
            btn.inactiveTextColor = TableOfWizardryScreen.DARK_GRAY;
            btn.dropShadow = false;
            btn.active = school != screen.block.currentSchool;

            schoolList.add(btn);
        }
        screen.addRenderableWidget(schoolList);

        // Spell list (only if school selected)
        if (screen.block.currentSchool != null) {
            AbstractWidgetList spellList = new AbstractWidgetList(minecraft, 76, 104, top, spellX, 0, 5);
            BrutalitySpells.getSpellsFromSchool(screen.block.currentSchool).forEach(spell -> {
                TextImageButton btn = new TextImageButton(
                        0, 0, 70, 18,
                        0, 0, 18,
                        TableOfWizardryScreen.BUTTON_TEXTURE,
                        70, 54,
                        b -> showSpellPage(screen, spell),
                        spell.getTranslatedSpellName().copy()
                                .withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT))
                );
                // copy colors, shadow, etc. from above
                btn.hoveredOrFocusedTextColor = TableOfWizardryScreen.LIGHT_GRAY;
                btn.notHoveredOrFocusedTextColor = TableOfWizardryScreen.GRAY;
                btn.inactiveTextColor = TableOfWizardryScreen.DARK_GRAY;
                btn.dropShadow = false;

                spellList.add(btn);
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

    public void showSpellPage(TableOfWizardryScreen screen, BrutalitySpell spell) {
        screen.block.currentSpell = spell;
        screen.block.currentState = TableOfWizardryBlockEntity.GuiState.SPELL_PAGE;
        screen.updateServerAndRefresh();
    }
}
