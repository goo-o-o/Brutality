package net.goo.brutality.util.build_archetypes;

import com.github.stephengold.joltjni.BodyInterface;
import com.github.stephengold.joltjni.Quat;
import com.github.stephengold.joltjni.RVec3;
import com.github.stephengold.joltjni.enumerate.EActivation;
import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.common.registry.BrutalityPhysicsBodies;
import net.goo.brutality.common.velthoric.bodies.CoinRigidBody;
import net.goo.brutality.util.EffectUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import net.xmx.velthoric.math.VxTransform;
import net.xmx.velthoric.physics.body.manager.VxChunkManager;
import net.xmx.velthoric.physics.body.manager.VxServerBodyDataStore;
import net.xmx.velthoric.physics.world.VxPhysicsWorld;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CoinHelper {
    public static void actuallySpawnAndLaunchCoin(BrutalityCoinItem coinItem, Player player, ItemStack coinStack, VxPhysicsWorld physicsWorld, float spreadIntensity) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // Spread intensity clamped 0 to 1
        float intensity = Mth.clamp(spreadIntensity, 0, 1);

        // 1. Jittered Spawn Position
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();

        // Calculate a "Right" and "Up" vector relative to player look to create a flat spawning plane
        Vec3 right = lookVec.cross(new Vec3(0, 1, 0)).normalize();
        if (right.lengthSqr() < 0.01) right = new Vec3(1, 0, 0); // Handle looking straight up/down
        Vec3 up = right.cross(lookVec).normalize();

        // Max delta of 0.5 blocks as requested
        float posDelta = intensity * 0.5F;
        Vec3 offset = right.scale(random.nextFloat(-posDelta, posDelta))
                .add(up.scale(random.nextFloat(-posDelta, posDelta)));

        Vec3 spawnPosMc = eyePos.add(lookVec.scale(0.8)).add(offset);

        VxTransform transform = new VxTransform(
                new RVec3(spawnPosMc.x, spawnPosMc.y, spawnPosMc.z),
                Quat.sIdentity()
        );

        // 2. Create the Body
        CoinRigidBody coinBody = physicsWorld.getBodyManager().createRigidBody(
                BrutalityPhysicsBodies.COIN,
                transform,
                EActivation.Activate,
                coin -> {
                    coin.setCoin(coinStack);
                    coin.setOwner(player);
                    coin.setServerData(CoinRigidBody.DATA_DIAMETER, coinItem.getDiameter(player, coinStack));
                }
        );

        if (coinBody == null) return;

        BodyInterface bodyInterface = physicsWorld.getPhysicsSystem().getBodyInterface();
        int bodyId = coinBody.getBodyId();

        // 3. Jittered Direction (Up to 160 degrees total spread)
        // Intensity 1.0 = 80 degrees left/right from center
        float yawRange = intensity * 80.0F;
        float randomYawOffset = random.nextFloat(-yawRange, yawRange);
        float finalYaw = player.getYRot() + randomYawOffset;

        // Pitch logic
        float playerPitch = player.getXRot();
        float launchPitch = (playerPitch < -35F && playerPitch > -60F) ? playerPitch : -50F;
        // Add a bit of vertical jitter based on intensity
        launchPitch += random.nextFloat(-15F, 15F) * intensity;

        float yawRad = finalYaw * ((float) Math.PI / 180F);
        float pitchRad = launchPitch * ((float) Math.PI / 180F);

        float f = -Mth.sin(yawRad) * Mth.cos(pitchRad);
        float f1 = -Mth.sin(pitchRad);
        float f2 = Mth.cos(yawRad) * Mth.cos(pitchRad);

        float strength = random.nextFloat(3, 6);
        com.github.stephengold.joltjni.Vec3 launchVelocity = new com.github.stephengold.joltjni.Vec3(f * strength, f1 * strength * 2, f2 * strength);

        // 4. Spin (Right vector for flipping)
        float sideYawRad = (finalYaw + 90) * ((float) Math.PI / 180F);
        Vec3 spinAxis = new Vec3(-Mth.sin(sideYawRad), 0, Mth.cos(sideYawRad));

        float spinSpeed = random.nextInt(25, 50);
        if (random.nextBoolean()) spinSpeed *= -1;

        com.github.stephengold.joltjni.Vec3 angularVel = new com.github.stephengold.joltjni.Vec3(
                (float) spinAxis.x * spinSpeed,
                random.nextFloat(-2.0f, 2.0f) * intensity, // Wobble increases with intensity
                (float) spinAxis.z * spinSpeed
        );

        bodyInterface.setLinearAndAngularVelocity(bodyId, launchVelocity, angularVel);
    }

    public static void spawnAndLaunchCoin(BrutalityCoinItem coinItem, Player player, ItemStack stack, VxPhysicsWorld physicsWorld) {
        spawnAndLaunchCoin(coinItem, player, stack, physicsWorld, 0.2F);
    }

    public static void spawnAndLaunchCoin(BrutalityCoinItem coinItem, Player player, ItemStack stack, VxPhysicsWorld physicsWorld, float spread) {
        physicsWorld.execute(() -> actuallySpawnAndLaunchCoin(coinItem, player, stack, physicsWorld, spread));

        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityItems.OVERDRAW_POUCH.get())) {
                List<ItemStack> coinStacksOffCooldown = getCoinsInInventoryOffCd(player);
                if (!coinStacksOffCooldown.isEmpty()) {
                    ItemStack coinStackOffCd = coinStacksOffCooldown.get(player.getRandom().nextInt(coinStacksOffCooldown.size()));
                    physicsWorld.execute(() -> actuallySpawnAndLaunchCoin(coinItem, player, coinStackOffCd, physicsWorld, spread));
                    BrutalityCoinItem coinItemOffCd = (BrutalityCoinItem) coinStackOffCd.getItem();
                    player.getCooldowns().addCooldown(coinItemOffCd, coinItemOffCd.getCooldownTime(player));
                }

            }

            if (handler.isEquipped(BrutalityItems.THE_GLUTTONS_PURSE.get())) {
                EffectUtils.modifyEffect(player, BrutalityEffects.AVARICE.get(), new EffectUtils.ModValue(400, true), new EffectUtils.ModValue(1, false), 100, e -> e.addEffect(new MobEffectInstance(BrutalityEffects.AVARICE.get(), 400, 0)), null);
            }

            if (handler.isEquipped(BrutalityItems.MIRRORED_MINT.get())) {
                physicsWorld.execute(() -> actuallySpawnAndLaunchCoin(coinItem, player, stack, physicsWorld, spread));
            }

        });
    }

    public static List<ItemStack> getCoinsInInventory(Player player) {
        List<ItemStack> coins = new ArrayList<>(player.getInventory().items);
        coins.removeIf(stack -> !(stack.getItem() instanceof BrutalityCoinItem));
        return coins;
    }

    public static List<ItemStack> getCoinsInInventoryOffCd(Player player) {
        List<ItemStack> coins = getCoinsInInventory(player);
        coins.removeIf(stack -> player.getCooldowns().isOnCooldown(stack.getItem()));
        return coins;
    }

    public static Set<BrutalityCoinItem> getUniqueCoinsInInventory(Player player) {
        return player.getInventory().items.stream()
                .map(ItemStack::getItem)
                .filter(BrutalityCoinItem.class::isInstance)
                .map(BrutalityCoinItem.class::cast)
                .collect(Collectors.toSet());
    }

    public static int getNearbyCoinCount(LivingEntity player, float radius) {
        VxPhysicsWorld world = VxPhysicsWorld.get(player.level().dimension());
        if (world == null) return 0;

        double radiusSquared = radius * radius;
        Vec3 playerPos = player.position();
        VxChunkManager chunkManager = world.getBodyManager().getChunkManager();

        // Get nearby chunks to search
        int chunkRadius = (int) Math.ceil(radius / 16.0);
        ChunkPos playerChunk = new ChunkPos(player.blockPosition());

        AtomicInteger coinCount = new AtomicInteger();

        for (int cx = -chunkRadius; cx <= chunkRadius; cx++) {
            for (int cz = -chunkRadius; cz <= chunkRadius; cz++) {
                ChunkPos chunkPos = new ChunkPos(playerChunk.x + cx, playerChunk.z + cz);

                chunkManager.forEachBodyInChunk(chunkPos, body -> {
                    if (!(body instanceof CoinRigidBody)) {
                        return;
                    }

                    int index = body.getDataStoreIndex();
                    if (index == -1) {
                        return;
                    }

                    VxServerBodyDataStore dataStore = world.getBodyManager().getDataStore();
                    double dx = dataStore.posX[index] - playerPos.x;
                    double dy = dataStore.posY[index] - playerPos.y;
                    double dz = dataStore.posZ[index] - playerPos.z;

                    double distanceSquared = dx * dx + dy * dy + dz * dz;

                    if (distanceSquared <= radiusSquared) {
                        coinCount.getAndIncrement();
                    }
                });
            }
        }

        return coinCount.get();
    }
}
