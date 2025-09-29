package net.goo.brutality.item.curios.charm;

import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.goo.brutality.event.forge.ForgePlayerStateHandler;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class SilverBoosterPack extends BrutalityCurioItem {


    public SilverBoosterPack(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

    private final Object2BooleanOpenHashMap<UUID> IN_RANGE_MAP = new Object2BooleanOpenHashMap<>();

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {

        if (slotContext.entity() instanceof Player player) {
            boolean wasInRange = IN_RANGE_MAP.getOrDefault(player.getUUID(), false);
            boolean isInRange = false;

            if (player.getLastDeathLocation().isPresent()) {
                var loc = player.getLastDeathLocation().get();
                if (player.distanceToSqr(loc.pos().getCenter()) < 100) {
                    isInRange = true;
                }
            }

            if (!wasInRange && isInRange) {
                ForgePlayerStateHandler.boosterPackEffects.get().forEach(player::removeEffect);
            }

            IN_RANGE_MAP.put(player.getUUID(), isInRange);
        }

    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack =  new ItemStack(this);
        stack.enchant(Enchantments.VANISHING_CURSE, 1);
        return stack;
    }
}
