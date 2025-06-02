package net.goo.armament.client.event;

import net.goo.armament.Armament;
import net.goo.armament.entity.base.ArmaThrownTrident;
import net.goo.armament.entity.custom.BlackHole;
import net.goo.armament.item.weapon.custom.SupernovaSword;
import net.goo.armament.item.weapon.custom.ThunderboltTrident;
import net.goo.armament.registry.ModParticles;
import net.goo.armament.util.ModUtils;
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
    static float blackHoleParticleSpawnRadius = 6;
    static float blackHoleParticleSpeedFactor = 0.05F;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();
        Item mainHandItem = player.getMainHandItem().getItem();
        Item offHandItem = player.getOffhandItem().getItem();

        if (level.isClientSide()) {
            if (tickCount % 10 == 0) {
                // SUPERNOVA PARTICLE HANDLER START //
                if (mainHandItem instanceof SupernovaSword || offHandItem instanceof SupernovaSword) {
                    level.addParticle(ModUtils.getRandomParticle(ModParticles.SUPERNOVA_PARTICLE),
                            player.getRandomX(0.5), player.getRandomY(), player.getRandomZ(0.5),
                            0,0,0
                            );

                }
                // SUPERNOVA PARTICLE HANDLER END //


                // BLACK HOLE PARTICLE HANDLER START //
                spawnBlackHoleEntityParticles(player, level);
                // BLACK HOLE PARTICLE HANDLER END //
            }

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

        if (tickCount > 1000) tickCount = 0;
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
        List<ArmaThrownTrident> thrownZeusThunderbolts = level.getEntitiesOfClass(ArmaThrownTrident.class, player.getBoundingBox().inflate(100));
        float particleSpawnRadius = 1F;
        if (!thrownZeusThunderbolts.isEmpty()) {

            for (ArmaThrownTrident thrownZeusThunderbolt : thrownZeusThunderbolts) {

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
        for (BlackHole blackHoleEntity : blackHoleEntities) {

            double offsetX = (level.random.nextFloat() - 0.5) * blackHoleParticleSpawnRadius;
            double offsetY = (level.random.nextFloat() - 0.5) * blackHoleParticleSpawnRadius;
            double offsetZ = (level.random.nextFloat() - 0.5) * blackHoleParticleSpawnRadius;

            Vec3 particlePosition = blackHoleEntity.position().add(offsetX, offsetY, offsetZ);
            Vec3 particleDirection = (blackHoleEntity.position().subtract(particlePosition)).scale(blackHoleParticleSpeedFactor);

            level.addParticle(ModParticles.BLACK_HOLE_PARTICLE.get(),
                    particlePosition.x, particlePosition.y, particlePosition.z,
                    particleDirection.x, particleDirection.y, particleDirection.z);
        }
    }
    }
