package net.goo.brutality.common.item.base;

import com.mojang.blaze3d.platform.InputConstants;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.util.AugmentHelper;
import net.goo.brutality.util.NBTUtils;
import net.goo.brutality.util.magic.SpellStorage;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.goo.brutality.util.tooltip.SpellTooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static net.goo.brutality.util.magic.SpellStorage.SPELL_MOD;

public class BrutalityMagicItem extends BrutalityGenericItem {
    public int baseSpellSlots, baseAugmentSlots;
    public BrutalityCategories.ItemType.MagicItemType type;


    public BrutalityMagicItem(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents, int baseSpellSlots, int baseAugmentSlots, BrutalityCategories.MagicItemType type) {
        super(rarity, descriptionComponents);
        this.baseSpellSlots = baseSpellSlots;
        this.baseAugmentSlots = baseAugmentSlots;
        this.type = type;
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        AugmentHelper.forceAddAugmentSlot(stack, baseAugmentSlots);
        return stack;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        Minecraft mc = Minecraft.getInstance();
        boolean showExtendedView = InputConstants.isKeyDown(mc.getWindow().getWindow(), mc.options.keyShift.getKey().getValue());
        boolean showAllSpells = InputConstants.isKeyDown(mc.getWindow().getWindow(), mc.options.keySprint.getKey().getValue());

        List<SpellStorage.SpellEntry> spellEntries = SpellStorage.getSpells(stack);
        if (showAllSpells) {
            for (SpellStorage.SpellEntry spellEntry : spellEntries) {
                SpellTooltipRenderer.renderSpellEntry(spellEntry, tooltip, showExtendedView);
            }
        } else {
            SpellStorage.SpellEntry spellEntry = SpellStorage.getCurrentSpellEntry(stack);
            if (spellEntry != null) {
                SpellTooltipRenderer.renderSpellEntry(spellEntry, tooltip, showExtendedView);
            }
        }

        tooltip.add(Component.translatable("message." + Brutality.MOD_ID + ".press_for_more_info",
                Component.keybind(Minecraft.getInstance().options.keyShift.getName())));

        int extraSlots = NBTUtils.getInt(stack, SPELL_MOD, 0);

        tooltip.add(Component.translatable("message." + Brutality.MOD_ID + ".press_to_show_all_spells",
                        Component.keybind(Minecraft.getInstance().options.keySprint.getName()))
                .append(" [" + spellEntries.size() + "/" + baseSpellSlots + (extraSlots > 0 ? " + " + extraSlots : "") + "]"));

    }

}
