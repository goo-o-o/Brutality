package net.goo.armament.event;

import net.goo.armament.Armament;
import net.goo.armament.entity.custom.BlackHoleEntity;
import net.goo.armament.item.custom.SupernovaSwordItem;
import net.goo.armament.particle.ModParticles;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientParticleEventsBus {
    private static int supernovaTickCount = 0;
    private static int blackHoleTickCount = 0;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();

        if (level.isClientSide()) {

        // SUPERNOVA PARTICLE HANDLER START //
            if (player.getMainHandItem().getItem() instanceof SupernovaSwordItem) {
                if (supernovaTickCount >= 4) {
                spawnSupernovaSwordParticles(player, level);
                    supernovaTickCount = 0;
                }
                supernovaTickCount++;
            }
        // SUPERNOVA PARTICLE HANDLER END //

        // BLACK HOLE PARTICLE HANDLER START //
                if (blackHoleTickCount >= 4) {
                    spawnBlackHoleEntityParticles(player, level);
                    blackHoleTickCount = 0;
                }
                blackHoleTickCount++;
        // BLACK HOLE PARTICLE HANDLER END //

        }

    }

    private static void spawnBlackHoleEntityParticles(Player player, Level level) {
        List<BlackHoleEntity> blackHoleEntities = level.getEntitiesOfClass(BlackHoleEntity.class, player.getBoundingBox().inflate(50));
        float particleSpawnRadius = 6;
        float particleSpeedFactor = 0.05F;
        for (BlackHoleEntity blackHoleEntity : blackHoleEntities) {

            double offsetX = (level.random.nextFloat() - 0.5) * particleSpawnRadius;
            double offsetY = (level.random.nextFloat() - 0.5) * particleSpawnRadius;
            double offsetZ = (level.random.nextFloat() - 0.5) * particleSpawnRadius;

            Vec3 particlePosition = blackHoleEntity.position().add(offsetX, offsetY, offsetZ);
            Vec3 particleDirection = (blackHoleEntity.position().subtract(particlePosition)).scale(particleSpeedFactor);

            level.addParticle(ModParticles.BLACK_HOLE_PARTICLE.get(),
                    particlePosition.x, particlePosition.y, particlePosition.z,
                    particleDirection.x, particleDirection.y, particleDirection.z);
        }
    }

    private static void spawnSupernovaSwordParticles(Player player, Level level) {
        Vec3 bodyDirection = player.getForward();
        Vec3 perpendicularDirection = bodyDirection.cross(new Vec3(0, 1, 0)).normalize();

        double offsetX = (level.random.nextFloat() - 0.5) * 3.0;
        double offsetY = (level.random.nextFloat()) * 3.0;
        double offsetZ = (level.random.nextFloat() - 0.5) * 3.0;

        Vec3 particlePosition = player.position().add(offsetX, offsetY, offsetZ);
        double velocityScale = 0.3;
        Vec3 velocity = perpendicularDirection.scale((level.random.nextFloat() - 0.5) * velocityScale);

        level.addParticle(ModParticles.SUPERNOVA_SWORD_PARTICLE.get(),
                particlePosition.x, particlePosition.y, particlePosition.z,
                velocity.x, velocity.y, velocity.z);
    }



}
