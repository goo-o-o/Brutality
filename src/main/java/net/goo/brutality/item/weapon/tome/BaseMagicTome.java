package net.goo.brutality.item.weapon.tome;

import net.goo.brutality.item.base.BrutalityGenericItem;
import net.goo.brutality.magic.SpellCastingHandler;
import net.goo.brutality.magic.SpellStorage;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class BaseMagicTome extends BrutalityGenericItem {

    public BaseMagicTome(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            List<SpellStorage.SpellEntry> spells = SpellStorage.getSpells(stack);
            if (!spells.isEmpty()) {
                // For simplicity, cast the first spell
                SpellStorage.SpellEntry spellEntry = spells.get(0);
                SpellCastingHandler.tryCastSpell(player, stack, spellEntry.spell(), spellEntry.level());

            }
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        List<SpellStorage.SpellEntry> spells = SpellStorage.getSpells(stack);
        for (SpellStorage.SpellEntry entry : spells) {
            tooltip.add(Component.literal(Component.translatable(entry.spell().getSpellName()) + " (Lvl " + entry.level() + ")"));
            tooltip.add(Component.literal("Mana Cost: " + entry.spell().getBaseManaCost()));
            tooltip.add(Component.literal("Damage: " + entry.spell().getBaseDamage()));
        }
    }
}