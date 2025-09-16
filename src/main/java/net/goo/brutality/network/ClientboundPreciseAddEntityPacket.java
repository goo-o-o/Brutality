package net.goo.brutality.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class ClientboundPreciseAddEntityPacket extends ClientboundAddEntityPacket {
    private final float xRot;
    private final float yRot;
    private final double yHeadRot;
    private static final Minecraft minecraft = Minecraft.getInstance();
    private final int xa, ya, za;

    public ClientboundPreciseAddEntityPacket(Entity pEntity) {
        this(pEntity, 0);
    }

    public ClientboundPreciseAddEntityPacket(Entity pEntity, int pData) {
        this(pEntity.getId(), pEntity.getUUID(), pEntity.getX(), pEntity.getY(), pEntity.getZ(), pEntity.getXRot(), pEntity.getYRot(), pEntity.getType(), pData, pEntity.getDeltaMovement(), pEntity.getYHeadRot());
    }

    public ClientboundPreciseAddEntityPacket(Entity pEntity, int pData, BlockPos pPos) {
        this(pEntity.getId(), pEntity.getUUID(), pPos.getX(), pPos.getY(), pPos.getZ(), pEntity.getXRot(), pEntity.getYRot(), pEntity.getType(), pData, pEntity.getDeltaMovement(), pEntity.getYHeadRot());
    }

    public ClientboundPreciseAddEntityPacket(int pId, UUID pUuid, double pX, double pY, double pZ, float pXRot, float pYRot, EntityType<?> pType, int pData, Vec3 pDeltaMovement, double pYHeadRot) {
        super(pId, pUuid, pX, pY, pZ, pXRot, pYRot, pType, pData, pDeltaMovement, pYHeadRot);
        this.xRot = pXRot;
        this.yRot = pYRot;
        this.yHeadRot = pYHeadRot;
        this.xa = (int) (Mth.clamp(pDeltaMovement.x, -3.9D, 3.9D) * 8000.0D);
        this.ya = (int) (Mth.clamp(pDeltaMovement.y, -3.9D, 3.9D) * 8000.0D);
        this.za = (int) (Mth.clamp(pDeltaMovement.z, -3.9D, 3.9D) * 8000.0D);
    }

    public ClientboundPreciseAddEntityPacket(FriendlyByteBuf pBuffer) {
        super(pBuffer);
        this.xRot = pBuffer.readFloat();
        this.yRot = pBuffer.readFloat();
        this.yHeadRot = pBuffer.readDouble();
        this.xa = pBuffer.readShort();
        this.ya = pBuffer.readShort();
        this.za = pBuffer.readShort();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeVarInt(this.getId());
        pBuffer.writeUUID(this.getUUID());
        pBuffer.writeId(BuiltInRegistries.ENTITY_TYPE, this.getType());
        pBuffer.writeDouble(this.getX());
        pBuffer.writeDouble(this.getY());
        pBuffer.writeDouble(this.getZ());
        pBuffer.writeFloat(this.getXRot());
        pBuffer.writeFloat(this.getYRot());
        pBuffer.writeDouble(this.getYHeadRot());
        pBuffer.writeVarInt(this.getData());
        pBuffer.writeShort(this.xa);
        pBuffer.writeShort(this.ya);
        pBuffer.writeShort(this.za);
    }

    @Override
    public boolean isSkippable() {
        return super.isSkippable();
    }

    @Override
    public float getYRot() {
        return this.yRot;
    }

    @Override
    public float getXRot() {
        return this.xRot;
    }
}