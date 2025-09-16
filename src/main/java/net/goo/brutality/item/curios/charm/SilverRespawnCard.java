package net.goo.brutality.item.curios.charm;

import net.goo.brutality.entity.capabilities.EntityCapabilities;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class SilverRespawnCard extends BrutalityCurioItem {


    public SilverRespawnCard(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (slotContext.entity() != null) {
            slotContext.entity().getCapability(BrutalityCapabilities.RESPAWN_CAP).ifPresent(cap ->
                    cap.setCardType(EntityCapabilities.RespawnCap.CARD_TYPE.SILVER));
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (slotContext.entity() != null) {
            if (slotContext.entity().isDeadOrDying()) return;
            slotContext.entity().getCapability(BrutalityCapabilities.RESPAWN_CAP).ifPresent(cap ->
                    cap.setCardType(EntityCapabilities.RespawnCap.CARD_TYPE.NONE));
        }
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack =  new ItemStack(this);
        stack.enchant(Enchantments.VANISHING_CURSE, 1);
        return stack;
    }
}
