package net.goo.brutality.common.item.curios.charm;

import com.github.stephengold.joltjni.BodyInterface;
import com.github.stephengold.joltjni.Vec3;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.velthoric.bodies.CoinRigidBody;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.item.Rarity;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ReverseCoin extends BrutalityCurioItem {
    public ReverseCoin(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    public static void launchCoinIntoAir(CoinRigidBody coinRigidBody) {
        assert coinRigidBody.getPhysicsWorld() != null;
        assert coinRigidBody.getPhysicsWorld().getPhysicsSystem() != null;
        BodyInterface bodyInterface = coinRigidBody.getPhysicsWorld().getPhysicsSystem().getBodyInterface();
        int bodyId = coinRigidBody.getBodyId();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        bodyInterface.setLinearAndAngularVelocity(bodyId,
                new Vec3(
                        random.nextFloat(-0.2F, 0.2F),
                        random.nextFloat(4F, 8F),
                        random.nextFloat(-0.2F, 0.2F)
                ),
                new Vec3(
                        random.nextInt(-25, 25),
                        random.nextInt(-25, 25),
                        random.nextInt(-25, 25)
                ));

        coinRigidBody.setServerData(CoinRigidBody.DATA_DESPAWN_TICKS, -1); // reallow the next coinflip + refresh timer
    }
}
