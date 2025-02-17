package net.goo.armament.client.event;

import net.goo.armament.Armament;
import net.goo.armament.entity.custom.BlackHole;
import net.goo.armament.entity.custom.ThunderboltProjectile;
import net.goo.armament.item.custom.SupernovaSword;
import net.goo.armament.item.custom.ThunderboltTrident;
import net.goo.armament.registry.ModParticles;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientParticleHandler {
    private static int tickCount = 0;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();
        Item mainHandItem = player.getMainHandItem().getItem();
        Item offHandItem = player.getOffhandItem().getItem();

        if (level.isClientSide()) {
            // SUPERNOVA PARTICLE HANDLER START //
            if (mainHandItem instanceof SupernovaSword || offHandItem instanceof SupernovaSword) {
                if (tickCount % 4 == 0) {
                    spawnSupernovaSwordParticles(player, level);
                }
            }
            // SUPERNOVA PARTICLE HANDLER END //

            // BLACK HOLE PARTICLE HANDLER START //
            if (tickCount % 4 == 0) {
                spawnBlackHoleEntityParticles(player, level);
            }
            // BLACK HOLE PARTICLE HANDLER END //

            // ZAP PARTICLE HANDLER START
            int random = level.random.nextIntBetweenInclusive(15, 35);
            if (tickCount % random == 0) {
                if (mainHandItem instanceof ThunderboltTrident || offHandItem instanceof ThunderboltTrident) {
                    spawnZeusThunderboltParticle(player, level);
                }
                    spawnThrownThunderboltEntityParticle(player, level);
            }
            // ZAP PARTICLE HANDLER END
        }
        tickCount++;
        if (tickCount == 100) { tickCount = 0; };

    }

    private static void spawnZeusThunderboltParticle(Player player, Level level) {
        double offsetX = (level.random.nextFloat() - 0.5) * 1.5;
        double offsetY = (level.random.nextFloat()) * 1.5;
        double offsetZ = (level.random.nextFloat() - 0.5) * 1.5;

        Vec3 particlePosition = player.position().add(offsetX, offsetY, offsetZ);

        level.addParticle(ModParticles.ZAP_PARTICLE.get(),
                particlePosition.x, particlePosition.y, particlePosition.z,
                0, 0, 0);
    }

    private static void spawnThrownThunderboltEntityParticle(Player player, Level level) {
        List<ThunderboltProjectile> thrownZeusThunderbolts = level.getEntitiesOfClass(ThunderboltProjectile.class, player.getBoundingBox().inflate(100));
        float particleSpawnRadius = 1F;
        if (!thrownZeusThunderbolts.isEmpty()) {

            for (ThunderboltProjectile thrownZeusThunderbolt : thrownZeusThunderbolts) {

                double offsetX = (level.random.nextFloat() - 0.5) * particleSpawnRadius;
                double offsetY = (level.random.nextFloat() - 0.5) * particleSpawnRadius;
                double offsetZ = (level.random.nextFloat() - 0.5) * particleSpawnRadius;

                Vec3 particlePosition = thrownZeusThunderbolt.position().add(offsetX, offsetY, offsetZ);

                level.addParticle(ModParticles.ZAP_PARTICLE.get(),
                        particlePosition.x, particlePosition.y, particlePosition.z,
                        0, 0, 0);
            }
        }
    }

    private static void spawnBlackHoleEntityParticles(Player player, Level level) {
        List<BlackHole> blackHoleEntities = level.getEntitiesOfClass(BlackHole.class, player.getBoundingBox().inflate(50));
        float particleSpawnRadius = 6;
        float particleSpeedFactor = 0.05F;
        for (BlackHole blackHoleEntity : blackHoleEntities) {

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
