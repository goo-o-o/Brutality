package net.goo.brutality.client.gui.screen.table_of_wizardry;

import net.goo.brutality.Brutality;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public enum TableOfWizardryBookSection {
    CONJURE, SYNTHESISE, AUGMENT, INSCRIBE, EXPUNGE, SIPHON;
    public final ResourceLocation bookmark;

    TableOfWizardryBookSection() {
        this.bookmark = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/" + this.name().toLowerCase(Locale.ROOT) + "_bookmark.png");
    }
}
