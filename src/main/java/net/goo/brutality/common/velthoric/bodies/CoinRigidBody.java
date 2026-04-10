package net.goo.brutality.common.velthoric.bodies;

import com.github.stephengold.joltjni.*;
import com.github.stephengold.joltjni.enumerate.EMotionType;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.common.velthoric.CoinContactListener;
import net.goo.brutality.common.velthoric.CoinflipResult;
import net.goo.brutality.util.ModUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.xmx.velthoric.network.VxByteBuf;
import net.xmx.velthoric.physics.VxPhysicsLayers;
import net.xmx.velthoric.physics.body.VxRemovalReason;
import net.xmx.velthoric.physics.body.network.synchronization.VxDataSerializers;
import net.xmx.velthoric.physics.body.network.synchronization.VxSynchronizedData;
import net.xmx.velthoric.physics.body.network.synchronization.accessor.VxServerAccessor;
import net.xmx.velthoric.physics.body.registry.VxBodyType;
import net.xmx.velthoric.physics.body.type.VxRigidBody;
import net.xmx.velthoric.physics.body.type.factory.VxRigidBodyFactory;
import net.xmx.velthoric.physics.world.VxPhysicsWorld;

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

    public Player getOwner() {
        if (this.ownerUUID == null) {
            UUID uuid = this.get(DATA_OWNER_UUID);
            if (uuid != null) {
                this.ownerUUID = uuid;
            }
        }
        return physicsWorld.getLevel().getPlayerByUUID(ownerUUID);
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

    @Override
    public void onBodyAdded(VxPhysicsWorld world) {
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

    public int age = 0;

    @Override
    public void onPhysicsTick(VxPhysicsWorld world) {
        BodyInterface bi = world.getPhysicsSystem().getBodyInterface();
        int id = this.getBodyId();

        if (!bi.isActive(id)) {
            if (get(DATA_DESPAWN_TICKS) < 0) {
                CoinflipResult result = getPhysicalResult(bi.getRotation(id));
                world.execute(() -> handleResult(result, bi.getPosition(id).toVec3()));
            }
        }
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

    private void handleResult(CoinflipResult result, Vec3 location) {
        // This runs on the Server Thread via world.executeMain
        Player player = getOwner();

        if (player != null && coinStack.getItem() instanceof BrutalityCoinItem coinItem) {
            switch (result) {
                case HEADS -> {
                    if (coinItem.spawnParticles()) {
                        ((ServerLevel) player.level()).sendParticles(BrutalityParticles.HEADS_PARTICLE.get(), location.getX(), location.getY() + 0.25, location.getZ(), 1, 0, 0, 0, 0);
                    }


                    coinItem.onHeads(player, coinStack);
                }
                case TAILS -> {
                    if (coinItem.spawnParticles()) {
                        ((ServerLevel) player.level()).sendParticles(BrutalityParticles.TAILS_PARTICLE.get(), location.getX(), location.getY() + 0.25, location.getZ(), 1, 0, 0, 0, 0);
                    }

                    coinItem.onTails(player, coinStack);

                }
                // removed EDGE
            }
            this.setServerData(DATA_DESPAWN_TICKS, 0);
        }

    }

    public CoinflipResult getPhysicalResult(Quat q) {
        // We extract the vertical component of the local 'Up' axis (Y)
        // from the quaternion.
        float x = q.getX();
        float z = q.getZ();

        // The formula for the Y-axis alignment in a unit quaternion:
        float alignment = 1.0f - 2.0f * (x * x + z * z);

        if (alignment > 0.85f) return CoinflipResult.HEADS;      // Pointing Up
        if (alignment < -0.85f) return CoinflipResult.TAILS;     // Pointing Down

        return CoinflipResult.EDGE;
    }


    @Override
    public int createJoltBody(VxRigidBodyFactory factory) {
        float radius = this.get(DATA_RADIUS);
        try (
                ShapeSettings shapeSettings = new CylinderShapeSettings(0.03125F, radius);
                BodyCreationSettings bcs = new BodyCreationSettings()
        ) {
            // Set basic physics properties.
            bcs.setMotionType(EMotionType.Dynamic);
            bcs.setObjectLayer(VxPhysicsLayers.MOVING); // Makes it collide with other dynamic objects and terrain.
            bcs.setRestitution(0.2f); // Bounciness
            bcs.setFriction(0.5f);
            bcs.setGravityFactor(2);

            // The factory handles the final Jolt object creation.
            return factory.create(shapeSettings, bcs);
        }
    }

    public static final VxServerAccessor<String> DATA_COIN;
    public static final VxServerAccessor<Float> DATA_RADIUS;
    public static final VxServerAccessor<UUID> DATA_OWNER_UUID;
    public static final VxServerAccessor<Integer> DATA_DESPAWN_TICKS;

    protected void defineSyncData(VxSynchronizedData.Builder builder) {
        builder.define(DATA_RADIUS, 0.15F);
        builder.define(DATA_COIN, "terramity:fateful_coin");
        builder.define(DATA_OWNER_UUID, null);
        builder.define(DATA_DESPAWN_TICKS, -1);
    }

    public void writePersistenceData(VxByteBuf buf) {
        String coinId = this.get(DATA_COIN);
        buf.writeUtf(coinId == null ? "terramity:fateful_coin" : coinId);
        buf.writeFloat(this.get(DATA_RADIUS));
        UUID ownerUUID = this.get(DATA_OWNER_UUID);
        buf.writeUUID(ownerUUID == null ? UUID.randomUUID() : ownerUUID);
        buf.writeInt(this.get(DATA_DESPAWN_TICKS));
    }

    public void readPersistenceData(VxByteBuf buf) {
        // Read the ID back as a String
        this.setServerData(DATA_COIN, buf.readUtf());
        this.setServerData(DATA_RADIUS, buf.readFloat());
        this.setServerData(DATA_OWNER_UUID, buf.readUUID());
        this.setServerData(DATA_DESPAWN_TICKS, buf.readInt());
    }

    static {
        DATA_COIN = VxServerAccessor.create(CoinRigidBody.class, VxDataSerializers.STRING);
        DATA_RADIUS = VxServerAccessor.create(CoinRigidBody.class, VxDataSerializers.FLOAT);
        DATA_OWNER_UUID = VxServerAccessor.create(CoinRigidBody.class, VxDataSerializers.UUID);
        DATA_DESPAWN_TICKS = VxServerAccessor.create(CoinRigidBody.class, VxDataSerializers.INTEGER);
    }
}
