package net.goo.brutality.item.weapon.spear;

import net.goo.brutality.item.base.BrutalitySpearItem;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.goo.brutality.util.phys.HitboxUtils;
import net.goo.brutality.util.phys.OrientedBoundingBox;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.mcreator.terramity.init.TerramityModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;

import java.util.List;

public class Rhongomyniad extends BrutalitySpearItem {
    public Rhongomyniad(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    public static final OrientedBoundingBox HITBOX = new OrientedBoundingBox(Vec3.ZERO, new Vec3(1 / 8F, 1 / 8F, 40).scale(0.5F), 0, 0, 0);
    public static final Vec3 OFFSET = new Vec3(0, 0, 3 + HITBOX.halfExtents.z);

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (!ModList.get().isLoaded("bettercombat") && entity instanceof Player player) {
            if (player.getAttackStrengthScale(0) >= 1) {
                performRayAttack(player);
            }
        }
        return super.onEntitySwing(stack, entity);
    }

    public void performRayAttack(Player player) {
        if (player.level() instanceof ServerLevel serverLevel) {
            HitboxUtils.handleRay(player, LivingEntity.class, Rhongomyniad.HITBOX, OFFSET, BrutalityModEntities.RHONGOMYNIAD_RAY.get(), true).entitiesHit.forEach(e -> {
                e.hurt(e.damageSources().playerAttack(player), 5);

                for (int i = 0; i < 5; i++)
                    serverLevel.sendParticles(TerramityModParticleTypes.HOLY_GLINT.get(), e.getX(), e.getY(0.5), e.getZ(), 3, 1, 1, 1, 0);
            });
        }
        player.level().playSound(null, player.blockPosition(), ModUtils.getRandomSound(TerramityModSounds.VIRTUE_ATTACK.get(), TerramityModSounds.VIRTUE_HURT.get(), TerramityModSounds.VIRTUE_DEATH.get()), SoundSource.PLAYERS, 1, Mth.nextFloat(player.getRandom(), 0.8F, 1.2F));

    }
}
