package net.goo.brutality.common.item.curios.anklet;

import net.goo.brutality.common.item.base.BrutalityAnkletItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.List;

public class DebugAnklet extends BrutalityAnkletItem {


    public DebugAnklet(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    @Override
    public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
        if (dodger instanceof Player wearer) {
            if (!wearer.getCooldowns().isOnCooldown(stack.getItem())) {
                wearer.addEffect(new MobEffectInstance(TerramityModMobEffects.MISPROGRAMMED.get(), 100));
                wearer.getCooldowns().addCooldown(stack.getItem(), 100);
            }
        }
    }
}
