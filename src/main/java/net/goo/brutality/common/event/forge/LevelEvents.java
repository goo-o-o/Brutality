package net.goo.brutality.common.event.forge;

import com.github.stephengold.joltjni.PhysicsSystem;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.velthoric.CoinContactListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xmx.velthoric.physics.world.VxPhysicsWorld;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LevelEvents {

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            VxPhysicsWorld world = VxPhysicsWorld.get(serverLevel.dimension());

            if (world != null) {
                PhysicsSystem physicsSystem = world.getPhysicsSystem();

                if (!CoinContactListener.isRegistered(world.getDimensionKey())) {
                    CoinContactListener listener = new CoinContactListener(world);
                    if (physicsSystem != null) {
                        physicsSystem.setContactListener(listener);
                    }
                    CoinContactListener.markRegistered(world.getDimensionKey(), listener);
                    Brutality.LOGGER.info("Lazy-registered CoinContactListener for {}", world.getBodyManager());
                }
            }
        }

    }
}
