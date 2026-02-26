package net.goo.brutality.common.item.curios.feet;

import net.goo.brutality.client.particle.providers.WaveParticleData;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingFallEvent;

import java.util.List;

public class SeismicStompers extends BrutalityCurioItem {
    public SeismicStompers(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void onWearerFall(LivingFallEvent event, ItemStack curio) {
        float fallDistance = event.getDistance();

        LivingEntity wearer = event.getEntity();
        Level level = wearer.level();
        float radius = fallDistance * 0.35F;
        List<Entity> nearbyEntities = level.getEntitiesOfClass(Entity.class, wearer.getBoundingBox().inflate(radius), e -> e != wearer);
        DamageSource source = wearer instanceof Player player ? wearer.damageSources().playerAttack(player) : wearer.damageSources().mobAttack(wearer);

        WaveParticleData<?> waveParticleData = new WaveParticleData<>(BrutalityParticles.SEISMIC_SHOCKWAVE.get(), radius, 10);

        if (level instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(
                    waveParticleData,
                    wearer.getX(),
                    wearer.getY(0.1),
                    wearer.getZ(),
                    1,
                    0, 0, 0,
                    0
            );

        for (Entity nearbyEntity : nearbyEntities) {
            Vec3 from = nearbyEntity.getPosition(0).subtract(wearer.getPosition(0));
            float intensity = (float) (radius - from.length()); // closer should be stronger

            from.scale(fallDistance * intensity);

            nearbyEntity.push(from.x, from.y + intensity * 0.5, from.z);
            nearbyEntity.hurt(source, intensity * 3);
        }
    }
}
