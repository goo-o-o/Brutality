package net.goo.brutality.item.curios.anklet;

import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.List;

public class DavysAnklet extends BrutalityAnkletItem {


    public DavysAnklet(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    @Override
    public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
        if (source.getEntity() instanceof LivingEntity attacker && dodger instanceof Player wearer) {
            if (!wearer.getCooldowns().isOnCooldown(this)) {
                attacker.addEffect(new MobEffectInstance(TerramityModMobEffects.VULNERABLE.get(), 200));
                wearer.getCooldowns().addCooldown(this, 100);
            }
        }
    }
}
