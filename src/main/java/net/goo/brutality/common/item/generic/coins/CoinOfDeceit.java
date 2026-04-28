package net.goo.brutality.common.item.generic.coins;

import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CoinOfDeceit extends BrutalityCoinItem {


    public CoinOfDeceit(Properties pProperties, int cooldownTime, List<ItemDescriptionComponent> descriptionComponents) {
        super(pProperties, cooldownTime, descriptionComponents);
    }

    @Override
    protected float getBasePixelDiameter() {
        return 12;
    }

    @Override
    public void onHeads(Player player, ItemStack stack, Vec3 location) {
        playBuffSounds(player);
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 400, 0));
        for (LivingEntity nearbyEntity : player.level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, player, new AABB(location, location).inflate(5))) {
            nearbyEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 400, 0));
        }
    }

    @Override
    public void onTails(Player player, ItemStack stack, Vec3 location) {
        playDebuffSounds(player);
        player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));
        for (LivingEntity nearbyEntity : player.level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, player, new AABB(location, location).inflate(5))) {
            nearbyEntity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 100, 0));
        }
    }

}
