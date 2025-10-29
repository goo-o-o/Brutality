package net.goo.brutality.item.curios.hands;

import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;

public class Handcuffs extends BrutalityCurioItem {
    public Handcuffs(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.enchant(Enchantments.BINDING_CURSE, 0);
        return stack;
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            ICuriosItemHandler handler = CuriosApi.getCuriosInventory(livingEntity).orElse(null);
            return !handler.isEquipped(this);
        }

        return super.canEquip(slotContext, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (livingEntity.tickCount % 20 == 0) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 21, 1, false, false, true));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 21, 2, false, false, true));
            }
        }
    }
}
