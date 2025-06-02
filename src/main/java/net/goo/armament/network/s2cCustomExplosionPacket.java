package net.goo.armament.network;

import com.google.common.collect.Lists;
import net.goo.armament.entity.explosion.ArmaExplosion;
import net.goo.armament.util.ModResources;
import net.goo.armament.util.helpers.ModExplosionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class s2cCustomExplosionPacket {
    private final double x;
    private final double y;
    private final double z;
    private final float power;
    private final List<BlockPos> toBlow;
    private final float knockbackX;
    private final float knockbackY;
    private final float knockbackZ;
    private final ModResources.EXPLOSION_TYPES explosionType; // Add the explosion type



    public s2cCustomExplosionPacket(double pX, double pY, double pZ, float pPower, List<BlockPos> pToBlow, @Nullable Vec3 pKnockback, ModResources.EXPLOSION_TYPES explosionType) {
        this.x = pX;
        this.y = pY;
        this.z = pZ;
        this.power = pPower;
        this.toBlow = Lists.newArrayList(pToBlow);
        if (pKnockback != null) {
            this.knockbackX = (float) pKnockback.x;
            this.knockbackY = (float) pKnockback.y;
            this.knockbackZ = (float) pKnockback.z;
        } else {
            this.knockbackX = 0.0F;
            this.knockbackY = 0.0F;
            this.knockbackZ = 0.0F;
        }
        this.explosionType = explosionType;


    }

    public s2cCustomExplosionPacket(FriendlyByteBuf pBuffer) {
        this.x = pBuffer.readDouble();
        this.y = pBuffer.readDouble();
        this.z = pBuffer.readDouble();
        this.power = pBuffer.readFloat();
        int i = Mth.floor(this.x);
        int j = Mth.floor(this.y);
        int k = Mth.floor(this.z);
        this.toBlow = pBuffer.readList((buf) -> {
            int l = buf.readByte() + i;
            int i1 = buf.readByte() + j;
            int j1 = buf.readByte() + k;
            return new BlockPos(l, i1, j1);
        });
        this.knockbackX = pBuffer.readFloat();
        this.knockbackY = pBuffer.readFloat();
        this.knockbackZ = pBuffer.readFloat();
        this.explosionType = pBuffer.readEnum(ModResources.EXPLOSION_TYPES.class);
    }

    public void encode(FriendlyByteBuf pBuffer) {
        pBuffer.writeDouble(this.x);
        pBuffer.writeDouble(this.y);
        pBuffer.writeDouble(this.z);
        pBuffer.writeFloat(this.power);
        int i = Mth.floor(this.x);
        int j = Mth.floor(this.y);
        int k = Mth.floor(this.z);
        pBuffer.writeCollection(this.toBlow, (buf, blockPos) -> {
            int l = blockPos.getX() - i;
            int i1 = blockPos.getY() - j;
            int j1 = blockPos.getZ() - k;
            buf.writeByte(l);
            buf.writeByte(i1);
            buf.writeByte(j1);
        });
        pBuffer.writeFloat(this.knockbackX);
        pBuffer.writeFloat(this.knockbackY);
        pBuffer.writeFloat(this.knockbackZ);
        pBuffer.writeEnum(this.explosionType);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null) {


                ArmaExplosion explosion = ModExplosionHelper.createExplosion(this.explosionType, Minecraft.getInstance().level, null,
                        this.x, this.y, this.z, this.power, false, Explosion.BlockInteraction.DESTROY);

                explosion.finalizeExplosion(true);

                // Apply knockback to the player
                Minecraft.getInstance().player.setDeltaMovement(
                        Minecraft.getInstance().player.getDeltaMovement().add(
                                this.knockbackX,
                                this.knockbackY,
                                this.knockbackZ
                        )
                );
            }
        });

        ctx.get().setPacketHandled(true);
    }


    public float getKnockbackX() {
        return this.knockbackX;
    }

    public float getKnockbackY() {
        return this.knockbackY;
    }

    public float getKnockbackZ() {
        return this.knockbackZ;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getPower() {
        return this.power;
    }

    public List<BlockPos> getToBlow() {
        return this.toBlow;
    }


}
