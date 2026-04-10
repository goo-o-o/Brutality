package net.goo.brutality.common.velthoric;

import com.github.stephengold.joltjni.Body;
import com.github.stephengold.joltjni.BodyFilter;
import com.github.stephengold.joltjni.FilteredContactListener;
import com.github.stephengold.joltjni.Vec3;
import com.github.stephengold.joltjni.enumerate.EFilterMode;
import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.common.velthoric.bodies.CoinRigidBody;
import net.goo.brutality.util.ModUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.xmx.velthoric.physics.body.type.VxBody;
import net.xmx.velthoric.physics.world.VxPhysicsWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class CoinContactListener extends FilteredContactListener {
    private final VxPhysicsWorld world;
    private static final Map<ResourceKey<Level>, CoinContactListener> REGISTERED_LISTENERS = new HashMap<>();

    public static class AllPassFilter extends BodyFilter {
        @Override
        public boolean shouldCollide(int bodyId) {
            // Return true so we don't filter out ANY collisions
            return true;
        }
    }

    public static boolean isRegistered(ResourceKey<Level> dimension) {
        return REGISTERED_LISTENERS.containsKey(dimension);
    }

    public static void markRegistered(ResourceKey<Level> dimension, CoinContactListener listener) {
        REGISTERED_LISTENERS.put(dimension, listener);
    }

    public CoinContactListener(VxPhysicsWorld world) {
        this.world = world;
        this.setEnableAdded(true);
        this.setBodyFilterMode(EFilterMode.Both);
        this.setBodyFilter(new AllPassFilter());
    }

    @Override
    public void onContactAdded(long body1Va, long body2Va, long manifoldVa, long settingsVa) {
        Body b1 = new Body(body1Va);
        Body b2 = new Body(body2Va);

        // Get indices for the body manager
        int id1 = b1.getId();
        int id2 = b2.getId();

        VxBody vb1 = world.getBodyManager().getByJoltBodyId(id1);
        VxBody vb2 = world.getBodyManager().getByJoltBodyId(id2);

        if (vb1 instanceof CoinRigidBody coinRigidBody && ((BrutalityCoinItem) coinRigidBody.getCoinStack().getItem()).playImpactSounds()) world.execute(() -> {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            Vec3 position = b1.getPosition().toVec3();
            world.getLevel().playSound(null, position.getX(), position.getY(), position.getZ(), ModUtils.getRandomSound(BrutalitySounds.COIN_IMPACT), SoundSource.PLAYERS, 1F, random.nextFloat(0.8F, 1.2F));
        });

        if (vb2 instanceof CoinRigidBody coinRigidBody && ((BrutalityCoinItem) coinRigidBody.getCoinStack().getItem()).playImpactSounds()) world.execute(() -> {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            Vec3 position = b2.getPosition().toVec3();
            world.getLevel().playSound(null, position.getX(), position.getY(), position.getZ(), ModUtils.getRandomSound(BrutalitySounds.COIN_IMPACT), SoundSource.PLAYERS, 1F, random.nextFloat(0.8F, 1.2F));
        });

//        if (isB1Coin || isB2Coin) {
        // Check for Block (Static) or another Coin
//            if (isB1Coin && isB2Coin) {
//                System.out.println("Coin-on-Coin collision!");
//            } else {
//                System.out.println("Coin hit something else (ID: " + (isB1Coin ? id2 : id1) + ")");
//            }
    }
}
