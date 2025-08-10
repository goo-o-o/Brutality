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
    private static final String SELECTED_SPELL_INDEX = "SelectedSpellIndex";

    public static List<SpellEntry> getSpells(ItemStack stack) {
        List<SpellEntry> spells = new ArrayList<>();
        if (!stack.hasTag() || !Objects.requireNonNull(stack.getTag()).contains(SPELLS_TAG)) {
            return spells;
        }

        ListTag spellsTag = stack.getTag().getList(SPELLS_TAG, Tag.TAG_COMPOUND);
        int selectedIndex = stack.getTag().getInt(SELECTED_SPELL_INDEX);

        for (int i = 0; i < spellsTag.size(); i++) {
            CompoundTag spellTag = spellsTag.getCompound(i);
            ResourceLocation spellId = ResourceLocation.parse(spellTag.getString(SPELL_ID_TAG));
            int level = spellTag.getInt(SPELL_LEVEL_TAG);

            IBrutalitySpell spell = SpellRegistry.getSpell(spellId);
            if (spell != null) {
                spells.add(new SpellEntry(spell, level, i == selectedIndex));
            }
        }

        return spells;
    }

    public static SpellEntry getCurrentSpellEntry(ItemStack stack) {
        List<SpellEntry> spells = getSpells(stack);
        if (spells.isEmpty()) return null;

        int selectedIndex = stack.getOrCreateTag().getInt(SELECTED_SPELL_INDEX);
        // Ensure index is within bounds
        selectedIndex = Math.min(selectedIndex, spells.size() - 1);
        selectedIndex = Math.max(selectedIndex, 0);

        return spells.get(selectedIndex);
    }

    public static boolean isSelected(ItemStack stack, SpellEntry spellEntry) {
        List<SpellEntry> spells = getSpells(stack);
        int index = spells.indexOf(spellEntry);
        if (index == -1) return false;

        return stack.getOrCreateTag().getInt(SELECTED_SPELL_INDEX) == index;
    }

    public static boolean addSpell(ItemStack stack, IBrutalitySpell spell, int level) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }

        if (stack.getTag() == null) return false;
        ListTag spellsTag = stack.getTag().contains(SPELLS_TAG) ?
                stack.getTag().getList(SPELLS_TAG, Tag.TAG_COMPOUND) :
                new ListTag();

        // Check if spell already exists
        for (int i = 0; i < spellsTag.size(); i++) {
            CompoundTag spellTag = spellsTag.getCompound(i);
            if (spellTag.getString(SPELL_ID_TAG).equals(getSpellId(spell).toString())) {
                // Optionally update spellLevel if spell exists
                spellTag.putInt(SPELL_LEVEL_TAG, level);
                stack.getTag().put(SPELLS_TAG, spellsTag);
                return true;
            }
        }

        CompoundTag newSpellTag = new CompoundTag();
        newSpellTag.putString(SPELL_ID_TAG, getSpellId(spell).toString());
        newSpellTag.putInt(SPELL_LEVEL_TAG, level);
        spellsTag.add(newSpellTag);

        stack.getTag().put(SPELLS_TAG, spellsTag);

        // If this is the first spell, select it automatically
        if (spellsTag.size() == 1) {
            setSelectedSpell(stack, 0);
        }

        return true;
    }


    public static boolean removeSpell(ItemStack stack, IBrutalitySpell spell) {
        if (!stack.hasTag()) {
            return false;
        }


        CompoundTag tag = stack.getTag();
        if (tag == null) return false;

        ListTag spellsTag = stack.getTag().contains(SPELLS_TAG) ?
                stack.getTag().getList(SPELLS_TAG, Tag.TAG_COMPOUND) :
                new ListTag();

        ResourceLocation spellId = getSpellId(spell);
        boolean removed = false;
        int removedIndex = -1;

        // Check if spell already exists
        for (int i = 0; i < spellsTag.size(); i++) {
            CompoundTag spellTag = spellsTag.getCompound(i);
            if (spellTag.getString(SPELL_ID_TAG).equals(spellId.toString())) {
                spellsTag.remove(i);
                removed = true;
                removedIndex = i;
                break;
            }
        }

        if (removed) {
            tag.put(SPELLS_TAG, spellsTag);
            if (tag.contains(SELECTED_SPELL_INDEX)) {
                int selectedIndex = tag.getInt(SELECTED_SPELL_INDEX);

                if (removedIndex <= selectedIndex) {
                    if (selectedIndex >= spellsTag.size()) {
                        selectedIndex = Math.max(0, spellsTag.size() - 1);
                        tag.putInt(SELECTED_SPELL_INDEX, selectedIndex);
                    }
                    // If removed spell == selected spell
                    else if (removedIndex == selectedIndex) {
                        tag.putInt(SELECTED_SPELL_INDEX, Math.min(selectedIndex, spellsTag.size() - 1));
                    }
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
        if (index < 0 || index >= spells.size()) {
            return false;
        }

        stack.getOrCreateTag().putInt(SELECTED_SPELL_INDEX, index);
        return true;
    }

    public static boolean cycleSelectedSpell(ItemStack stack, int direction) {
        if (!stack.hasTag()) return false;

        CompoundTag tag = stack.getTag();
        if (!tag.contains(SPELLS_TAG)) return false;

        ListTag spellsTag = tag.getList(SPELLS_TAG, Tag.TAG_COMPOUND);
        if (spellsTag.isEmpty()) return false;

        int currentIndex = tag.getInt(SELECTED_SPELL_INDEX);
        int newIndex = currentIndex + direction;

        // Wrap around
        if (newIndex >= spellsTag.size()) newIndex = 0;
        if (newIndex < 0) newIndex = spellsTag.size() - 1;

        tag.putInt(SELECTED_SPELL_INDEX, newIndex);

        return true;
    }

    private static ResourceLocation getSpellId(IBrutalitySpell spell) {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID,
                spell.getSpellName().toLowerCase(Locale.ROOT));
    }

    public record SpellEntry(IBrutalitySpell spell, int level, boolean selected) {
        public boolean isSelected() {
            return selected;
        }
    }

}