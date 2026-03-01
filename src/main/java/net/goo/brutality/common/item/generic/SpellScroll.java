package net.goo.brutality.common.item.generic;

import com.mojang.blaze3d.platform.InputConstants;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.util.magic.SpellStorage;
import net.goo.brutality.util.tooltip.SpellTooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        List<SpellStorage.SpellEntry> spellEntries = SpellStorage.getSpells(pStack);
        if (spellEntries.isEmpty()) {
            pTooltipComponents.add(Component.translatable("message." + Brutality.MOD_ID + ".no_spells"));
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        boolean shiftDown = InputConstants.isKeyDown(mc.getWindow().getWindow(), mc.options.keyShift.getKey().getValue());

        for (SpellStorage.SpellEntry spellEntry : spellEntries) {
            SpellTooltipRenderer.renderSpellEntry(spellEntry, pTooltipComponents, shiftDown);
        }

        pTooltipComponents.add(Component.translatable("message." + Brutality.MOD_ID + ".press_for_more_info",
                Component.keybind(Minecraft.getInstance().options.keyShift.getName())));
    }
}
