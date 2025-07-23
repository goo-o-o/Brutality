package net.goo.brutality.item.weapon.tome;

import net.goo.brutality.magic.SpellCastingHandler;
import net.goo.brutality.magic.SpellStorage;
import net.goo.brutality.magic.spells.daemonium.DaemonicPickaxeSpell;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DaemonicTome extends BaseMagicTome {

    public DaemonicTome(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        SpellStorage.addSpell(stack, new DaemonicPickaxeSpell(), 3);
        return stack;
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

}