package net.goo.armament.item.custom;

import net.goo.armament.item.ArmaGeoItem;
import net.goo.armament.item.ArmaSwordItem;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.registry.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.*;

import java.util.List;
import java.util.function.Consumer;


public class SupernovaSword extends ArmaSwordItem {
    public SupernovaSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category);
        this.colors = new int[][] {{65, 0, 125}, {25, 25, 25}};
        this.identifier = "supernova";
    }

    @Override
    public String geoIdentifier() {
        return "supernova";
    }

    @Override
    public <T extends Item & ArmaGeoItem> void initGeo(Consumer<IClientItemExtensions> consumer, int rendererID) {
        super.initGeo(consumer, 1);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        Level level = pAttacker.level();

        causeStarburstExplosion(pTarget, pAttacker);
        spawnStarburstExplosionParticles(pAttacker, pTarget, level);

        return super.hurtEnemy(pStack, pTarget, pAttacker);

    }

    private static void causeStarburstExplosion(LivingEntity target, LivingEntity player) {
        // Get the radius within which to damage entities (e.g., 5 blocks)
        double radius = 2.5;

        // Get all entities within the radius around the target
        List<Entity> nearbyEntities = target.level().getEntities(target, target.getBoundingBox().inflate(radius));

        // Apply damage to each nearby entity
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity && entity != target) { // Don't damage the target itself
                entity.hurt(entity.damageSources().explosion(player, entity), 7.5F); // Adjust damage as needed
            }
        }
    }

    private void spawnStarburstExplosionParticles(LivingEntity player, LivingEntity pTarget, Level level) {
        ((ServerLevel) level).sendParticles(ModParticles.STARBURST_PARTICLE.get(),
                pTarget.getX(), pTarget.getY() + pTarget.getBbHeight() / 2, pTarget.getZ(),
                0,
                level.random.nextFloat() * pTarget.getBbWidth(),
                level.random.nextFloat() * pTarget.getBbHeight(),
                level.random.nextFloat() * pTarget.getBbWidth(), 0);
    }

}
