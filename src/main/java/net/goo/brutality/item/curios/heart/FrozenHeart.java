package net.goo.brutality.item.curios.heart;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class FrozenHeart extends BrutalityCurioItem {

    public FrozenHeart(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HEART;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);

        LivingEntity entity = slotContext.entity();
        Level level = entity.level();

        List<LivingEntity> nearbyEntities = level.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, entity, entity.getBoundingBox().inflate(10));

        for (LivingEntity nearbyEntity : nearbyEntities) {
            nearbyEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3, 0));
            nearbyEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 3, 0));
        }

    }

}
