package net.goo.armament.event;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.SupernovaSwordItem;
import net.goo.armament.particle.ModParticles;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeEventBusClientEvents {
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();

        if (level.isClientSide()) {
            if (player.getMainHandItem().getItem() instanceof SupernovaSwordItem) {
                if (tickCounter >= 4) {
                spawnSwordParticles(player, level);
                    tickCounter = 0;
                }
                tickCounter++;
            }
        }
    }

    // Handle particle spawning logic
    private static void spawnSwordParticles(Player player, Level level) {
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
