package net.goo.brutality.item.curios.anklet;

import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ExodiumAnklet extends BrutalityAnkletItem {


    public ExodiumAnklet(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    @Override
    public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
        if (dodger instanceof Player player && !player.getCooldowns().isOnCooldown(this))
            ForgeRegistries.MOB_EFFECTS.getValues().stream().filter(MobEffect::isBeneficial).skip(dodger.getRandom().nextIntBetweenInclusive(0, ForgeRegistries.MOB_EFFECTS.getValues().size() - 1)).findFirst().ifPresent(effect -> {
                dodger.addEffect(new MobEffectInstance(effect, 60, 0));
                player.getCooldowns().addCooldown(this, 100);
            });
    }
}
