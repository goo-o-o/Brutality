package net.goo.brutality.item.weapon.scythe;

import net.goo.brutality.item.base.BrutalityScytheItem;
import net.goo.brutality.particle.providers.FlatParticleData;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.goo.brutality.util.phys.OrientedBoundingBox;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class Schism extends BrutalityScytheItem {
    public Schism(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    public static final List<OrientedBoundingBox> ARC_SWEEP_45;

    static {
        Vec3 halfExtents = new Vec3(6.0, 0.125, 3.0).scale(0.5F);  // width 6, height 0.125, depth 3
        Vec3 center = Vec3.ZERO;

        List<OrientedBoundingBox> list = new ArrayList<>();

        list.add(new OrientedBoundingBox(center, halfExtents, 0f,   0f, 0));     // center
        list.add(new OrientedBoundingBox(center, halfExtents, 0f, -45f, 0));     // left
        list.add(new OrientedBoundingBox(center, halfExtents, 0f,  +45f, 0));    // right

        ARC_SWEEP_45 = List.copyOf(list);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && player.getAttackStrengthScale(0) == 1 && player.level() instanceof ServerLevel serverLevel)
            performVoidSlash(player, serverLevel);
        return super.onEntitySwing(stack, entity);
    }

    public void performVoidSlash(Player pPlayer, ServerLevel serverLevel) {
        if (pPlayer.getCooldowns().isOnCooldown(this)) return;
        FlatParticleData<?> data = new FlatParticleData<>(BrutalityModParticles.VOID_SLASH_PARTICLE.get(),
                3F, -pPlayer.getViewXRot(0), -pPlayer.getViewYRot(0) + 180F, 0);

//        OrientedBoundingBox.TargetResult<LivingEntity> targets = OrientedBoundingBox.findEntitiesHit(pPlayer, LivingEntity.class, ARC_SWEEP_45, new Vec3(0, 0, 8), false);
//
//        double x = targets.hitboxes.get(0).center.x();
//        double y = targets.hitboxes.get(0).center.y();
//        double z = targets.hitboxes.get(0).center.z();
//        int delay = (int) (pPlayer.getCurrentItemAttackStrengthDelay() * 0.25F);
//
//        DelayedTaskScheduler.queueServerWork(serverLevel, delay, () -> {
//
//            targets.entities.forEach(livingEntity -> livingEntity.hurt(livingEntity.damageSources().playerAttack(pPlayer), 5));
//
//
//            ModUtils.sendParticles(serverLevel, data, true,
//                    x, y, z, 1, 0);
//            serverLevel.playSound(null, pPlayer.getX(), pPlayer.getY(0.5), pPlayer.getZ(), BrutalityModSounds.VOID_SLASH.get(), SoundSource.PLAYERS, 10F, 1F);
//
//
//        });

        pPlayer.getCooldowns().addCooldown(this, (int) (pPlayer.getCurrentItemAttackStrengthDelay()));

    }

    public static float remapYaw(float playerYaw) {
        return ((-playerYaw + 180f) % 360f + 360f) % 360f - 180f;
    }
}
