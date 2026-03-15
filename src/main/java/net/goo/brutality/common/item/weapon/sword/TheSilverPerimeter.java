package net.goo.brutality.common.item.weapon.sword;

import net.goo.brutality.client.particle.providers.FlatParticleData;
import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.common.item.weapon.RotatingAttackWeapon;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.util.math.phys.hitboxes.ArcCylindricalBoundingBox;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TheSilverPerimeter extends BrutalitySwordItem implements RotatingAttackWeapon {


    public TheSilverPerimeter(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, List<ItemDescriptionComponent> descriptionComponents, Properties properties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, descriptionComponents, properties);
    }

    @Override
    public float getMaxRotationsPerSecond() {
        return 20;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }


    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.CUSTOM;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pLivingEntity instanceof Player player) {
            ArcCylindricalBoundingBox arc = RotatingAttackWeapon.getHitbox(player, 0.5F, 4.5F, pStack, this, BrutalitySounds.SWORD_SWING.get());
            arc.findEntitiesHit(player, LivingEntity.class).forEach(e -> {
                e.invulnerableTime = 0;
                player.attack(e);
            });
        }
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pUsedHand != InteractionHand.MAIN_HAND)
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
        pPlayer.startUsingItem(pUsedHand);
        if (pLevel.isClientSide()) {
            FlatParticleData<?> data = new FlatParticleData<>(BrutalityParticles.SILVER_SLASH_PARTICLE.get(),
                    4.5F, 0, pPlayer.getViewYRot(0), 0,0,0,0, pPlayer.getId());

            pLevel.addParticle(data, pPlayer.getX(), pPlayer.getY(0.75F), pPlayer.getZ(), 0, 0, 0);
        }
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }


}
