package net.goo.brutality.entity.projectile.generic;

import net.goo.brutality.entity.base.BrutalityRay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RhongomyniadRay extends BrutalityRay implements IEntityAdditionalSpawnData {

    @Override
    public void setYRot(float pYRot) {
        if (pYRot == 0) return;
        super.setYRot(pYRot);
    }

    @Override
    public void setXRot(float pXRot) {
        if (pXRot == 0) return;
        super.setXRot(pXRot);
    }


    public RhongomyniadRay(EntityType<? extends BrutalityRay> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.shouldFollowOwner = false;
        this.noPhysics = true;
        this.hasImpulse = false;
    }


    @Override
    public void tick() {
        super.tick();
        if (tickCount > 5) discard();
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenLoop("spawn")))
        );
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeFloat(getXRot());
        buffer.writeFloat(getYRot());
        buffer.writeFloat(getYHeadRot());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buf) {
        float xRot = buf.readFloat();
        float yRot = buf.readFloat();
        float headRot = buf.readFloat();

        setXRot(xRot);
        setYRot(yRot);
        setYHeadRot(headRot);
        xRotO = xRot;
        yRotO = yRot;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
