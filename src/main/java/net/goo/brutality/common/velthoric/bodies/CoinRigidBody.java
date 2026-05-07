package net.goo.brutality.common.velthoric.bodies;

import com.github.stephengold.joltjni.*;
import com.github.stephengold.joltjni.readonly.ConstNarrowPhaseQuery;
import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.common.item.curios.charm.ReverseCoin;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.event.CoinflipEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;
import net.xmx.velthoric.network.VxByteBuf;
import net.xmx.velthoric.physics.body.VxRemovalReason;
import net.xmx.velthoric.physics.body.network.synchronization.VxDataSerializers;
import net.xmx.velthoric.physics.body.network.synchronization.VxSynchronizedData;
import net.xmx.velthoric.physics.body.network.synchronization.accessor.VxServerAccessor;
import net.xmx.velthoric.physics.body.registry.VxBodyType;
import net.xmx.velthoric.physics.body.type.VxRigidBody;
import net.xmx.velthoric.physics.body.type.factory.VxRigidBodyFactory;
import net.xmx.velthoric.physics.world.VxPhysicsWorld;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class CoinRigidBody extends VxRigidBody {
    public int MAX_DESPAWN_TICKS = 80;
    private UUID ownerUUID;
    ItemStack coinStack = ItemStack.EMPTY;

    @OnlyIn(Dist.CLIENT)
    public CoinRigidBody(VxBodyType<? extends VxRigidBody> type, UUID id) {
        super(type, id);
    }

    public CoinRigidBody(VxBodyType<? extends VxRigidBody> type, VxPhysicsWorld world, UUID id) {
        super(type, world, id);
    }


    public void setOwner(Player player) {
        this.ownerUUID = player.getUUID();
        this.setServerData(DATA_OWNER_UUID, ownerUUID);
    }

    @Nullable
    public Player getOwner() {
        if (this.ownerUUID == null) {
            UUID uuid = this.get(DATA_OWNER_UUID);
            if (uuid != null) {
                this.ownerUUID = uuid;
            }
        }
        return physicsWorld != null ? physicsWorld.getLevel().getPlayerByUUID(ownerUUID) : null;
    }

    public void setCoin(ItemStack stack) {
        this.coinStack = stack.copy();
        // sync just the item to client for rendering
        ResourceLocation rl = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (rl != null) {
            this.setServerData(DATA_COIN, rl.toString());
        }
    }

    public ItemStack getCoinStack() {
        // If server-side stack is empty, attempt to reconstruct from synced data (Client-side use case)
        if (this.coinStack.isEmpty()) {
            String idStr = this.get(DATA_COIN);
            if (idStr != null && !idStr.isEmpty()) {
                Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(idStr));
                if (item != null) {
                    this.coinStack = new ItemStack(item);
                }
            }
        }
        return this.coinStack;
    }


    public int age = 0;

    @Override
    public void onPhysicsTick(VxPhysicsWorld world) {
        BodyInterface bi = world.getPhysicsSystem().getBodyInterface();
        int id = this.getBodyId();

        if (!bi.isActive(id)) {
            if (get(DATA_DESPAWN_TICKS) < 0) {
                // 1. Get current transform
                RVec3 pos = bi.getPosition(id);
                Quat rot = bi.getRotation(id);

                // 2. Perform a raycast to find the surface normal it's resting on
                Vec3 surfaceNormal = findSurfaceNormal(world, pos);

                // 3. Determine result relative to that normal
                CoinflipEvent.CoinflipResult result = getPhysicalResultRelative(rot, surfaceNormal);

                Vec3 jniVec = pos.toVec3();
                world.execute(() -> handleResult(result, new net.minecraft.world.phys.Vec3(jniVec.getX(), jniVec.getY(), jniVec.getZ())));
            }
        }
    }

    /**
     * Finds the surface normal of whatever surface the coin is resting on.
     * Casts a ray from the coin's center downward (in the direction opposite to the coin's local up).
     */
    private Vec3 findSurfaceNormal(VxPhysicsWorld world, RVec3 coinPos) {
        BodyInterface bi = world.getPhysicsSystem().getBodyInterface();
        int coinId = this.getBodyId();
        Quat coinRot = bi.getRotation(coinId);

        // Get the coin's local "down" vector in world space
        Quaternionf jomlQuat = new Quaternionf(coinRot.getX(), coinRot.getY(), coinRot.getZ(), coinRot.getW());
        Vector3f coinDown = new Vector3f(0, -1, 0).rotate(jomlQuat);

        // Cast a ray from slightly above the coin's center in the direction of coin's down
        double startX = coinPos.xx() - coinDown.x * 0.05;
        double startY = coinPos.yy() - coinDown.y * 0.05;
        double startZ = coinPos.zz() - coinDown.z * 0.05;

        double endX = coinPos.xx() + coinDown.x * 0.15;
        double endY = coinPos.yy() + coinDown.y * 0.15;
        double endZ = coinPos.zz() + coinDown.z * 0.15;

        RVec3 rayStart = new RVec3(startX, startY, startZ);

        // Create ray direction (Vec3, not RVec3)
        com.github.stephengold.joltjni.Vec3 rayDirection = new com.github.stephengold.joltjni.Vec3(
                (float) (endX - startX),
                (float) (endY - startY),
                (float) (endZ - startZ)
        );

        // Perform the raycast
        RRayCast ray = new RRayCast(rayStart, rayDirection);
        RayCastResult result = new RayCastResult();

        // Get narrow phase query
        ConstNarrowPhaseQuery npq = world.getPhysicsSystem().getNarrowPhaseQuery();

        // Use the simplest castRay without any filters
        if (npq.castRay(ray, result)) {
            int hitBodyId = result.getBodyId();

            // Skip if we hit ourselves
            if (hitBodyId == coinId) {
                // Fallback to world up
                return new Vec3(0, 1, 0);
            }

            // Get the contact point
            RVec3 hitPosition = ray.getPointOnRay(result.getFraction());

            // Get normal by querying the hit body's shape
            com.github.stephengold.joltjni.Vec3 normal = getNormalAtPoint(world, hitBodyId, hitPosition);

            return new Vec3(normal.getX(), normal.getY(), normal.getZ());
        }

        // Fallback to world up if no hit
        return new Vec3(0, 1, 0);
    }

    private com.github.stephengold.joltjni.Vec3 getNormalAtPoint(VxPhysicsWorld world, int bodyId, RVec3 point) {
        BodyInterface bi = world.getPhysicsSystem().getBodyInterface();

        // Get the body's position
        RVec3 bodyPos = bi.getPosition(bodyId);

        // Calculate vector from body center to hit point (approximation for convex shapes)
        double dx = point.xx() - bodyPos.xx();
        double dy = point.yy() - bodyPos.yy();
        double dz = point.zz() - bodyPos.zz();

        // Calculate length
        double lengthSquared = dx * dx + dy * dy + dz * dz;

        if (lengthSquared > 0.000001) {
            double length = Math.sqrt(lengthSquared);

            // Normalize and return
            return new com.github.stephengold.joltjni.Vec3(
                    (float) (dx / length),
                    (float) (dy / length),
                    (float) (dz / length)
            );
        }

        // Fallback
        return new com.github.stephengold.joltjni.Vec3(0, 1, 0);
    }

    public CoinflipEvent.CoinflipResult getPhysicalResultRelative(Quat q, Vec3 surfaceNormal) {
        // Get the coin's local Up vector (0, 1, 0) transformed by its rotation
        Quaternionf jomlQuat = new Quaternionf(q.getX(), q.getY(), q.getZ(), q.getW());
        Vector3f coinUp = new Vector3f(0, 1, 0).rotate(jomlQuat);

        // Calculate the Dot Product between Coin Up and Surface Normal
        Vector3f surfaceNormalVec = new Vector3f(surfaceNormal.getX(), surfaceNormal.getY(), surfaceNormal.getZ());
        float dot = coinUp.dot(surfaceNormalVec);

        float threshold = 0.6f;
        if (dot > threshold) return CoinflipEvent.CoinflipResult.HEADS;
        if (dot < -threshold) return CoinflipEvent.CoinflipResult.TAILS;

        return CoinflipEvent.CoinflipResult.EDGE;
    }


    @Override
    public void onServerTick(ServerLevel level) {
        age++;
        int currentDespawn = this.get(DATA_DESPAWN_TICKS);

        // If it hasn't landed yet, check for a timeout (10 seconds)
        if (currentDespawn < 0 && age > 200) {
            this.setServerData(DATA_DESPAWN_TICKS, 0);
        }

        // If we are in the despawn phase
        if (currentDespawn > -1) {
            int next = currentDespawn + 1;
            this.setServerData(DATA_DESPAWN_TICKS, next);

            if (next > MAX_DESPAWN_TICKS) {
                this.physicsWorld.getBodyManager().removeBody(physicsId, VxRemovalReason.DISCARD);
            }
        }
    }

    private enum FlipState {
        NORMAL, CANCELLED, RELAUNCHED
    }

    private void handleResult(CoinflipEvent.CoinflipResult result, net.minecraft.world.phys.Vec3 location) {
        Player player = getOwner();
        if (player == null || !(coinStack.getItem() instanceof BrutalityCoinItem coinItem)) return;

        CoinflipEvent event = new CoinflipEvent(player, coinStack, location, result);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;
        result = event.getFlipResult();
        FlipState flipState = FlipState.NORMAL;

        if (result == CoinflipEvent.CoinflipResult.TAILS) {
            Optional<ICuriosItemHandler> resolve = CuriosApi.getCuriosInventory(player).resolve();
            if (resolve.isPresent()) {
                ICuriosItemHandler handler = resolve.get();

                if (handler.isEquipped(BrutalityItems.REVERSE_COIN.get()) && player.getRandom().nextFloat() <= 0.25F) {
                    ReverseCoin.launchCoinIntoAir(this);
                    flipState = FlipState.RELAUNCHED;
                } else if (handler.isEquipped(BrutalityItems.MOBIUS_STRIP.get()) && player.getRandom().nextFloat() <= 0.5F) {
                    flipState = FlipState.CANCELLED;
                }
            }
        }

        if (flipState == FlipState.NORMAL) {
            ServerLevel level = (ServerLevel) player.level();

            if (result == CoinflipEvent.CoinflipResult.HEADS) {
                if (coinItem.spawnParticles()) {
                    level.sendParticles(BrutalityParticles.HEADS_PARTICLE.get(), location.x(), location.y + 0.25, location.z, 1, 0, 0, 0, 0);
                }
                coinItem.onHeads(player, coinStack, location);
            } else {
                if (coinItem.spawnParticles()) {
                    level.sendParticles(BrutalityParticles.TAILS_PARTICLE.get(), location.x(), location.y() + 0.25, location.z(), 1, 0, 0, 0, 0);
                }
                coinItem.onTails(player, coinStack, location);
            }
        }

        if (flipState != FlipState.RELAUNCHED) {
            this.setServerData(DATA_DESPAWN_TICKS, 0);
        }
    }


    public CoinflipEvent.CoinflipResult getPhysicalResult(Quat q) {
        // We extract the vertical component of the local 'Up' axis (Y)
        // from the quaternion.
        float x = q.getX();
        float z = q.getZ();

        // The formula for the Y-axis alignment in a unit quaternion:
        float alignment = 1.0f - 2.0f * (x * x + z * z);
        float threshold = 0.6F;
        if (alignment > threshold) return CoinflipEvent.CoinflipResult.HEADS;      // Pointing Up
        if (alignment < -threshold) return CoinflipEvent.CoinflipResult.TAILS;     // Pointing Down

        return CoinflipEvent.CoinflipResult.EDGE;
    }


    @Override
    public int createJoltBody(VxRigidBodyFactory factory) {
        float diameter = this.get(DATA_DIAMETER);
        try (
                // 0.03125 = 1 pixel
                ShapeSettings shapeSettings = new CylinderShapeSettings(0.03125F, diameter * 0.5F);
        ) {
            return factory.create(shapeSettings, ((BrutalityCoinItem) this.getCoinStack().getItem()).bcs);
        }
    }

    public static final VxServerAccessor<String> DATA_COIN;
    public static final VxServerAccessor<Float> DATA_DIAMETER;
    public static final VxServerAccessor<UUID> DATA_OWNER_UUID;
    public static final VxServerAccessor<Integer> DATA_DESPAWN_TICKS;

    protected void defineSyncData(VxSynchronizedData.Builder builder) {
        builder.define(DATA_DIAMETER, 0.15F);
        builder.define(DATA_COIN, "terramity:fateful_coin");
        builder.define(DATA_OWNER_UUID, null);
        builder.define(DATA_DESPAWN_TICKS, -1);
    }

    public void writePersistenceData(VxByteBuf buf) {
        String coinId = this.get(DATA_COIN);
        buf.writeUtf(coinId == null ? "terramity:fateful_coin" : coinId);
        buf.writeFloat(this.get(DATA_DIAMETER));
        UUID ownerUUID = this.get(DATA_OWNER_UUID);
        buf.writeUUID(ownerUUID == null ? UUID.randomUUID() : ownerUUID);
        buf.writeInt(this.get(DATA_DESPAWN_TICKS));
    }

    public void readPersistenceData(VxByteBuf buf) {
        // Read the ID back as a String
        this.setServerData(DATA_COIN, buf.readUtf());
        this.setServerData(DATA_DIAMETER, buf.readFloat());
        this.setServerData(DATA_OWNER_UUID, buf.readUUID());
        this.setServerData(DATA_DESPAWN_TICKS, buf.readInt());
    }

    static {
        DATA_COIN = VxServerAccessor.create(CoinRigidBody.class, VxDataSerializers.STRING);
        DATA_DIAMETER = VxServerAccessor.create(CoinRigidBody.class, VxDataSerializers.FLOAT);
        DATA_OWNER_UUID = VxServerAccessor.create(CoinRigidBody.class, VxDataSerializers.UUID);
        DATA_DESPAWN_TICKS = VxServerAccessor.create(CoinRigidBody.class, VxDataSerializers.INTEGER);
    }
}
