package net.goo.brutality.util.magic;

import net.goo.brutality.common.item.base.BrutalityMagicItem;
import net.goo.brutality.common.item.generic.SpellScroll;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.registry.BrutalitySpells;
import net.goo.brutality.util.NBTUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SpellStorage {
    private static final String SPELLS_TAG = "Spells";
    public static final String SPELL_MOD = "SpellMod";
    private static final String SPELL_ID_TAG = "SpellId";
    private static final String SPELL_LEVEL_TAG = "SpellLevel";
    private static final String SELECTED_SPELL_INDEX = "SelectedSpellIndex";

    public record SpellEntry(BrutalitySpell spell, int level) {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof SpellEntry spellEntry && spellEntry.spell() == spell && spellEntry.level() == level;
        }
    }

    public static void addSpellSlot(ItemStack stack, int amount) {
        if (stack.getItem() instanceof BrutalityMagicItem) {
            stack.getOrCreateTag().putInt(SPELL_MOD, stack.getOrCreateTag().getInt(SPELL_MOD) + amount);
        }
    }

    public static int getSpellSlotCount(ItemStack stack) {
        if (stack.getItem() instanceof BrutalityMagicItem magicItem) {
            return magicItem.baseSpellSlots + NBTUtils.getInt(stack, SPELL_MOD, 0);
        }
        return -1;
    }

    public static ItemStack getScrollFromSpell(BrutalitySpell spell, int level) {
        Item scrollItem = spell.getSchool().getScrollItem();
        ItemStack scroll = new ItemStack(scrollItem);
        addSpell(scroll, spell, level);
        return scroll;
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
            BrutalitySpell spell = BrutalitySpells.getSpell(spellId);
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

    public static List<SpellEntry> addSpells(ItemStack stack, ItemStack... scrolls) {
        int maxSlots = getSpellSlotCount(stack);
        int currentSpellCount = getSpells(stack).size();
        int availableSlots = maxSlots - currentSpellCount;

        List<SpellEntry> allPossibleEntries = new ArrayList<>();
        // Flatten all entries from all scrolls
        for (ItemStack scroll : scrolls) {
            if (scroll.getItem() instanceof SpellScroll) {
                allPossibleEntries.addAll(getSpells(scroll));
            }
        }

        List<SpellEntry> successfullyAdded = new ArrayList<>();
        int slotsUsed = 0;

        for (SpellEntry entry : allPossibleEntries) {
            if (slotsUsed < availableSlots) {
                if (addSpell(stack, entry)) {
                    successfullyAdded.add(entry);
                    slotsUsed++;
                }
            } else {
                // WE EXCEEDED SLOTS. Remove the spell we just tried to add,
                // but didn't actually successfully add to the NBT.
                removeSpell(stack, entry.spell(), entry.level());
                break; // Stop trying to add more
            }
        }
        return successfullyAdded;
    }

    public static boolean addSpell(ItemStack stack, SpellEntry spellEntry) {
        return addSpell(stack, spellEntry.spell(), spellEntry.level());
    }

    public static boolean addSpell(ItemStack stack, BrutalitySpell spell, int level) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag spellsTag = tag.contains(SPELLS_TAG) ? tag.getList(SPELLS_TAG, Tag.TAG_COMPOUND) : new ListTag();

        // Check capacity if it's not a scroll
        if (!(stack.getItem() instanceof SpellScroll) && spellsTag.size() >= getSpellSlotCount(stack)) {
            return false;
        }

        ResourceLocation spellId = BrutalitySpells.getIdFromSpell(spell);
        String spellIdString = spellId.toString();

        // 1. Check if the exact same spell at the same level already exists
        for (int i = 0; i < spellsTag.size(); i++) {
            CompoundTag existingSpell = spellsTag.getCompound(i);
            if (existingSpell.getString(SPELL_ID_TAG).equals(spellIdString) &&
                    existingSpell.getInt(SPELL_LEVEL_TAG) == level) {
                // Already exists at this level, do nothing
                return false;
            }
        }

        // 2. Add the new spell entry
        CompoundTag newSpellTag = new CompoundTag();
        newSpellTag.putString(SPELL_ID_TAG, spellIdString);
        newSpellTag.putInt(SPELL_LEVEL_TAG, level);
        spellsTag.add(newSpellTag);
        tag.put(SPELLS_TAG, spellsTag);

        // Initialize selected index if first spell
        if (spellsTag.size() == 1) {
            tag.putInt(SELECTED_SPELL_INDEX, 0);
        }
        return true;
    }

    public static boolean removeSpell(ItemStack stack, BrutalitySpell spell, Integer spellLevel) {
        if (!stack.hasTag()) return false;

        CompoundTag tag = stack.getTag();
        if (tag == null) return false;
        ListTag spellsTag = tag.contains(SPELLS_TAG) ? tag.getList(SPELLS_TAG, Tag.TAG_COMPOUND) : new ListTag();
        ResourceLocation spellId = BrutalitySpells.getIdFromSpell(spell);

        int removedIndex = -1;
        for (int i = 0; i < spellsTag.size(); i++) {
            if (spellsTag.getCompound(i).getString(SPELL_ID_TAG).equals(spellId.toString())) {
                if (spellLevel != null && spellsTag.getCompound(i).getInt(SPELL_LEVEL_TAG) == spellLevel)
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

    public static void cycleSelectedSpell(ItemStack stack, int direction) {
        if (!stack.hasTag() || !stack.getTag().contains(SPELLS_TAG)) return;

        ListTag spellsTag = stack.getTag().getList(SPELLS_TAG, Tag.TAG_COMPOUND);
        if (spellsTag.isEmpty()) return;

        int currentIndex = stack.getTag().getInt(SELECTED_SPELL_INDEX);
        int newIndex = currentIndex + direction;
        if (newIndex >= spellsTag.size()) newIndex = 0;
        if (newIndex < 0) newIndex = spellsTag.size() - 1;
        stack.getTag().putInt(SELECTED_SPELL_INDEX, newIndex);
    }
}