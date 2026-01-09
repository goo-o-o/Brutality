package net.goo.brutality.item.weapon.sword;

import net.goo.brutality.entity.projectile.generic.ShadowflameSlash;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;

import java.util.List;

public class ShadowflameScissorBlade extends BrutalitySwordItem {

    public ShadowflameScissorBlade(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }


    public void performShadowflameSlash(Player player) {
        Level level = player.level();
        ShadowflameSlash shadowflameSlash = new ShadowflameSlash(BrutalityModEntities.SHADOWFLAME_SLASH.get(), level);
        Vec3 loc = player.getEyePosition();
        shadowflameSlash.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 0);
        shadowflameSlash.setPos(loc);
        shadowflameSlash.setOwner(player);
        level.addFreshEntity(shadowflameSlash);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (!ModList.get().isLoaded("bettercombat") && entity instanceof Player player) {
            if (player.getAttackStrengthScale(0) >= 1)
                performShadowflameSlash(player);
        }
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.addEffect(new MobEffectInstance(TerramityModMobEffects.NYXIUM_FIRE.get(), 60));
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }


}
