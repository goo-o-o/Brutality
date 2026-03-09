package net.goo.brutality.common.item.generic.augments;

import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.util.AugmentHelper;
import net.goo.brutality.util.magic.SpellStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BrutalityMagicAugmentItem extends BrutalityAugmentItem {
    public int spellSlotBonus = 0;

    public BrutalityMagicAugmentItem(Properties pProperties, BrutalityCategories... itemTypes) {
        super(pProperties, itemTypes);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        AugmentHelper.addAugmentItemTypes(stack, itemTypes);
        return stack;
    }

    @Override
    protected boolean shouldShowSection() {
        return super.shouldShowSection() || spellSlotBonus != 0;
    }

    public BrutalityAugmentItem withSpellSlotBonus(int spellSlotBonus) {
        this.spellSlotBonus = spellSlotBonus;
        return this;
    }

    @Override
    public void onAddedToItem(ItemStack parent) {
        SpellStorage.addSpellSlot(parent, spellSlotBonus);
    }

    // ran everytime a magic item is cast which has this item, ran after a spell is actually cast
    public void onAugmentedItemPostCast(Player caster, ItemStack parent, BrutalitySpell spell, int spellLevel, IBrutalitySpell.SpellCategory type) {
    }

    // ran everytime a magic item is cast which has this item, ran before a spell is actually cast, can also be used to modify spell level
    public int onAugmentedItemPreCast(Player caster, ItemStack parent, BrutalitySpell spell, int spellLevel, IBrutalitySpell.SpellCategory type) {
        return spellLevel;
    }


    @Override
    protected void addCustomTooltipLines(List<Component> components) {
        if (spellSlotBonus > 0) {
            components.add(Component.translatable("message.brutality.spell_slots", ("+" + spellSlotBonus)).withStyle(ChatFormatting.BLUE));
        } else if (spellSlotBonus < 0) {
            components.add(Component.translatable("message.brutality.spell_slots", spellSlotBonus).withStyle(ChatFormatting.RED));
        }
    }
}
