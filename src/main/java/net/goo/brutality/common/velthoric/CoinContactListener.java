package net.goo.brutality.common.velthoric;

import com.github.stephengold.joltjni.Body;
import com.github.stephengold.joltjni.BodyFilter;
import com.github.stephengold.joltjni.FilteredContactListener;
import com.github.stephengold.joltjni.RVec3;
import com.github.stephengold.joltjni.enumerate.EFilterMode;
import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.common.velthoric.bodies.CoinRigidBody;
import net.goo.brutality.util.ModUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
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

    public enum CollisionType {
        COIN,      // Hit another coin
        VXBODY,    // Hit a non-coin dynamic/kinematic body
        STATIC     // Hit a block or static world geometry
    }

    @Override
    public void onContactAdded(long body1Va, long body2Va, long manifoldVa, long settingsVa) {
        Body b1 = new Body(body1Va);
        Body b2 = new Body(body2Va);

        VxBody vb1 = world.getBodyManager().getByJoltBodyId(b1.getId());
        VxBody vb2 = world.getBodyManager().getByJoltBodyId(b2.getId());

        // Process both sides: If B1 is a coin, handle its collision with B2, and vice versa.
        handleCoinCollision(vb1, b1, vb2, b2);
        handleCoinCollision(vb2, b2, vb1, b1);
    }

    private void handleCoinCollision(VxBody self, Body selfJolt, VxBody other, Body otherJolt) {
        // 1. Filter: Is 'self' actually a coin?
        if (!(self instanceof CoinRigidBody coinBody)) return;

        ItemStack stack = coinBody.getCoinStack();
        if (stack == null || !(stack.getItem() instanceof BrutalityCoinItem coinItem)) return;

        // 2. Identify the CollisionType of the 'other' object
        CollisionType type;
        if (other instanceof CoinRigidBody) {
            type = CollisionType.COIN;
        } else if (other != null) {
            type = CollisionType.VXBODY;
        } else {
            // If other is null, it's likely a static/landscape body not managed by VxBody
            type = CollisionType.STATIC;
        }

        // 3. Execute logic (Sound and Custom Callback)
        world.execute(() -> {
            // Impact Sound
            if (coinItem.shouldPlayImpactSounds()) {
                ThreadLocalRandom random = ThreadLocalRandom.current();
                RVec3 jPos = selfJolt.getPosition();
                world.getLevel().playSound(null, (double) jPos.getX(), (double) jPos.getY(), (double) jPos.getZ(),
                        ModUtils.getRandomSound(BrutalitySounds.COIN_IMPACT),
                        SoundSource.PLAYERS, 1F, random.nextFloat(0.8F, 1.2F));
            }

            // Custom Item Callback
            coinItem.onCollide(coinBody, stack, other, type);
        });
    }
}
