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

public class SpellStorage {
    private static final String SPELLS_TAG = "Spells";
    private static final String SPELL_ID_TAG = "SpellId";
    private static final String SPELL_LEVEL_TAG = "SpellLevel";
    private static final String SELECTED_SPELL_INDEX = "SelectedSpellIndex";

    public record SpellEntry(IBrutalitySpell spell, int level) {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof SpellEntry spellEntry && spellEntry.spell() == spell && spellEntry.level() == level;
        }
    }



    public static List<SpellEntry> getSpells(ItemStack stack) {
        List<SpellEntry> spells = new ArrayList<>();
        if (!stack.hasTag() || !stack.getTag().contains(SPELLS_TAG)) {
            return spells;
        }

        ListTag spellsTag = stack.getTag().getList(SPELLS_TAG, Tag.TAG_COMPOUND);
        for (int i = 0; i < spellsTag.size(); i++) {
            CompoundTag spellTag = spellsTag.getCompound(i);
            ResourceLocation spellId = ResourceLocation.parse(spellTag.getString(SPELL_ID_TAG));
            int level = spellTag.getInt(SPELL_LEVEL_TAG);
            IBrutalitySpell spell = BrutalityModSpells.getSpell(spellId);
            if (spell != null) {
                spells.add(new SpellEntry(spell, level));
            }
        }
        return spells;
    }

    public static SpellEntry getCurrentSpellEntry(ItemStack stack) {
        List<SpellEntry> spells = getSpells(stack);
        if (spells.isEmpty()) return null;

        int selectedIndex = stack.getOrCreateTag().getInt(SELECTED_SPELL_INDEX);
        selectedIndex = Math.max(0, Math.min(selectedIndex, spells.size() - 1));
        return spells.get(selectedIndex);
    }

    public static boolean addSpell(ItemStack stack, IBrutalitySpell spell, int level) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag spellsTag = tag.contains(SPELLS_TAG) ? tag.getList(SPELLS_TAG, Tag.TAG_COMPOUND) : new ListTag();

        ResourceLocation spellId = getSpellId(spell);
        for (int i = 0; i < spellsTag.size(); i++) {
            if (spellsTag.getCompound(i).getString(SPELL_ID_TAG).equals(spellId.toString())) {
                spellsTag.getCompound(i).putInt(SPELL_LEVEL_TAG, level);
                tag.put(SPELLS_TAG, spellsTag);
                return true;
            }
        }

        CompoundTag newSpellTag = new CompoundTag();
        newSpellTag.putString(SPELL_ID_TAG, spellId.toString());
        newSpellTag.putInt(SPELL_LEVEL_TAG, level);
        spellsTag.add(newSpellTag);
        tag.put(SPELLS_TAG, spellsTag);

        if (spellsTag.size() == 1) {
            tag.putInt(SELECTED_SPELL_INDEX, 0);
        }
        return true;
    }

    public static boolean removeSpell(ItemStack stack, IBrutalitySpell spell) {
        if (!stack.hasTag()) return false;

        CompoundTag tag = stack.getTag();
        ListTag spellsTag = tag.contains(SPELLS_TAG) ? tag.getList(SPELLS_TAG, Tag.TAG_COMPOUND) : new ListTag();
        ResourceLocation spellId = getSpellId(spell);

        int removedIndex = -1;
        for (int i = 0; i < spellsTag.size(); i++) {
            if (spellsTag.getCompound(i).getString(SPELL_ID_TAG).equals(spellId.toString())) {
                spellsTag.remove(i);
                removedIndex = i;
                break;
            }
        }

        if (removedIndex != -1) {
            tag.put(SPELLS_TAG, spellsTag);
            if (tag.contains(SELECTED_SPELL_INDEX)) {
                int selectedIndex = tag.getInt(SELECTED_SPELL_INDEX);
                if (removedIndex <= selectedIndex) {
                    tag.putInt(SELECTED_SPELL_INDEX, Math.max(0, Math.min(selectedIndex, spellsTag.size() - 1)));
                }
            }
            if (spellsTag.isEmpty()) {
                tag.remove(SELECTED_SPELL_INDEX);
            }
            return true;
        }
        return false;
    }

    public static boolean setSelectedSpell(ItemStack stack, int index) {
        List<SpellEntry> spells = getSpells(stack);
        if (index < 0 || index >= spells.size()) return false;
        stack.getOrCreateTag().putInt(SELECTED_SPELL_INDEX, index);
        return true;
    }

    public static boolean cycleSelectedSpell(ItemStack stack, int direction) {
        if (!stack.hasTag() || !stack.getTag().contains(SPELLS_TAG)) return false;

        ListTag spellsTag = stack.getTag().getList(SPELLS_TAG, Tag.TAG_COMPOUND);
        if (spellsTag.isEmpty()) return false;

        int currentIndex = stack.getTag().getInt(SELECTED_SPELL_INDEX);
        int newIndex = currentIndex + direction;
        if (newIndex >= spellsTag.size()) newIndex = 0;
        if (newIndex < 0) newIndex = spellsTag.size() - 1;
        stack.getTag().putInt(SELECTED_SPELL_INDEX, newIndex);
        return true;
    }

    private static ResourceLocation getSpellId(IBrutalitySpell spell) {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, spell.getSpellName().toLowerCase(Locale.ROOT));
    }
}