package net.goo.brutality.common.item.weapon.hammer;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.base.BrutalityHammerItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class WoodenRulerHammer extends BrutalityHammerItem {


    public WoodenRulerHammer(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public float hurtEnemyModifiable(Player attacker, LivingEntity victim, ItemStack weapon, DamageSource source, float amount) {
        return amount * (victim.getBbHeight() * 0.75F);
    }
}
