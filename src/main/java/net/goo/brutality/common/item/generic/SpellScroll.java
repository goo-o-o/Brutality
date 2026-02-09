package net.goo.brutality.common.item.generic;

import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.util.magic.SpellStorage;
import net.goo.brutality.util.tooltip.SpellTooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellScroll extends Item {
    IBrutalitySpell.MagicSchool school;
    public SpellScroll(Properties pProperties) {
        super(pProperties);
    }

    public SpellScroll withSchool(IBrutalitySpell.MagicSchool school) {
        this.school = school;
        return this;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        List<SpellStorage.SpellEntry> spellEntries = SpellStorage.getSpells(pStack);

        for (SpellStorage.SpellEntry spellEntry : spellEntries) {
            SpellTooltips.renderSpellEntry(spellEntry, pTooltipComponents);
        }
    }
}
