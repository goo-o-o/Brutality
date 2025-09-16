package net.goo.brutality.item.weapon.unused;

import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ViperRapierItem extends BrutalitySwordItem implements GeoItem {


    public ViperRapierItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 2, 0, false, true));
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        int amtOfAttacks = 10;

            if (!level.isClientSide)
            {
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                for(int i = 0; i < amtOfAttacks; ++i) {
                    executor.schedule(() -> performBurstAttack(player), 50 * i, TimeUnit.MILLISECONDS);
                }
            }
            player.getCooldowns().addCooldown(player.getItemInHand(usedHand).getItem(), 20);
        return super.use(level, player, usedHand);
    }

    public void performBurstAttack(Player player) {
        Vec3 viewVector = player.getViewVector(1.0F).normalize();
        Vec3 eyePosition = player.getEyePosition();
        Vec3 targetPosition = eyePosition.add(viewVector.scale(3));
        AABB searchBox = new AABB(eyePosition, targetPosition).inflate(1.0F);

        List<Entity> entities = player.level().getEntities(player, searchBox, entity -> entity instanceof LivingEntity);

        if (!entities.isEmpty()) {
            for (Entity entity : entities) {
                if (player.hasLineOfSight(entity) && entity instanceof LivingEntity target) {
                    target.invulnerableTime = 0;
                    target.knockback(0.15F, -viewVector.x, -viewVector.z);
                    target.hurt(target.damageSources().generic(), 1);
                }
            }
        }
    }


}
