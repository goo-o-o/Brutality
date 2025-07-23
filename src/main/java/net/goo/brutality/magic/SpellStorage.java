package net.goo.brutality.magic;

import net.goo.brutality.Brutality;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SpellStorage {
    private static final String SPELLS_TAG = "Spells";
    private static final String SPELL_ID_TAG = "SpellId";
    private static final String SPELL_LEVEL_TAG = "SpellLevel";

    public static List<SpellEntry> getSpells(ItemStack stack) {
        List<SpellEntry> spells = new ArrayList<>();
        if (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains(SPELLS_TAG)) {
            return spells;
        }

        ListTag spellsTag = stack.getTag().getList(SPELLS_TAG, Tag.TAG_COMPOUND);
        for (int i = 0; i < spellsTag.size(); i++) {
            CompoundTag spellTag = spellsTag.getCompound(i);
            ResourceLocation spellId = ResourceLocation.parse(spellTag.getString(SPELL_ID_TAG));
            int level = spellTag.getInt(SPELL_LEVEL_TAG);
            
            IBrutalitySpell spell = SpellRegistry.getSpell(spellId);
            if (spell != null) {
                spells.add(new SpellEntry(spell, level));
            }
        }
        
        return spells;
    }

    public static boolean addSpell(ItemStack stack, IBrutalitySpell spell, int level) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }

        ListTag spellsTag = stack.getTag().contains(SPELLS_TAG) ? 
                          stack.getTag().getList(SPELLS_TAG, Tag.TAG_COMPOUND) : 
                          new ListTag();

        // Check if spell already exists (could implement upgrade logic here)
        for (int i = 0; i < spellsTag.size(); i++) {
            CompoundTag spellTag = spellsTag.getCompound(i);
            if (spellTag.getString(SPELL_ID_TAG).equals(getSpellId(spell).toString())) {
                return false;
            }
        }

        CompoundTag newSpellTag = new CompoundTag();
        newSpellTag.putString(SPELL_ID_TAG, getSpellId(spell).toString());
        newSpellTag.putInt(SPELL_LEVEL_TAG, level);
        spellsTag.add(newSpellTag);
        
        stack.getTag().put(SPELLS_TAG, spellsTag);
        return true;
    }

    private static ResourceLocation getSpellId(IBrutalitySpell spell) {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, spell.getSpellName().toLowerCase(Locale.ROOT));
    }

    public record SpellEntry(IBrutalitySpell spell, int level) {}
}