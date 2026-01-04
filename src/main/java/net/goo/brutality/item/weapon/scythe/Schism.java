package net.goo.brutality.item.weapon.scythe;

import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.item.base.BrutalityScytheItem;
import net.goo.brutality.particle.providers.FlatParticleData;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.goo.brutality.util.phys.ArcCylindricalBoundingBox;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Schism extends BrutalityScytheItem {
    public Schism(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    public static final ArcCylindricalBoundingBox HITBOX = new ArcCylindricalBoundingBox(Vec3.ZERO, 0.5F, 5.5F, 2.5F, 200);
    public static final Vec3 OFFSET = new Vec3(0, 0, 5);


    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && player.getAttackStrengthScale(0) == 1 && player.level() instanceof ServerLevel serverLevel)
            performVoidSlash(player, serverLevel, 0);
        return super.onEntitySwing(stack, entity);
    }

    public void performVoidSlash(Player pPlayer, ServerLevel serverLevel, int combo) {
        if (pPlayer.getCooldowns().isOnCooldown(this)) return;


        int delay = (int) (pPlayer.getCurrentItemAttackStrengthDelay() * 0.25F);
        DelayedTaskScheduler.queueServerWork(serverLevel, delay, () -> {
            FlatParticleData<?> data = new FlatParticleData<>(BrutalityModParticles.VOID_SLASH_PARTICLE.get(),
                    5F, -pPlayer.getViewXRot(0), -pPlayer.getViewYRot(0) + 180F, combo * 180);

            ArcCylindricalBoundingBox hitbox = (ArcCylindricalBoundingBox) HITBOX.inWorld(pPlayer, OFFSET);

            hitbox.findEntitiesHit(pPlayer, LivingEntity.class, null)
                    .forEach(livingEntity -> livingEntity.hurt(livingEntity.damageSources().playerAttack(pPlayer), 5));


            Vec3 lookAng = pPlayer.getLookAngle().scale(2); // For some manual offset
            Vec3 pos = hitbox.center.add(lookAng);

            ModUtils.sendParticles(serverLevel, data, true,
                    pos.x, pos.y, pos.z, 1, 0);
            serverLevel.playSound(null, pPlayer.getX(), pPlayer.getY(0.5), pPlayer.getZ(), BrutalityModSounds.VOID_SLASH.get(), SoundSource.PLAYERS, 10F, 1F);


        });

        pPlayer.getCooldowns().addCooldown(this, (int) (pPlayer.getCurrentItemAttackStrengthDelay()));

    }

}
