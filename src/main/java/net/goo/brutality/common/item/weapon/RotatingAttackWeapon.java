package net.goo.brutality.common.item.weapon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.util.math.phys.hitboxes.ArcCylindricalBoundingBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.WeakHashMap;

public interface RotatingAttackWeapon {
    default boolean acceleratesInfinitely() {
        return false;
    }

    default float accelerationRateInDegrees() {
        return 5F;
    }

    default float getMaxRotationsPerSecond() {
        return 1F;
    }

    default int getTicksTillMaxSpeed() {
        return 20;
    }

    Map<LivingEntity, Float> SPIN_ANCHORS = new WeakHashMap<>();

    static <T extends LivingEntity, M extends EntityModel<T>> void handleRenderEvent(RenderLivingEvent.Pre<T, M> event, RotatingAttackWeapon weapon) {
        LivingEntity entity = event.getEntity();
        PoseStack poseStack = event.getPoseStack();
        float partialTick = event.getPartialTick();
        ItemStack useItem = entity.getUseItem();

        // handle starting rotation
        float anchorYaw = SPIN_ANCHORS.computeIfAbsent(entity, LivingEntity::getVisualRotationYInDegrees);
        float vanillaF = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);

        float spinDegrees = calculateSpinRotation(entity.getTicksUsingItem(), partialTick, weapon);

        // cancel vanilla head and body rotation so that the player model stays in place
        float correction = vanillaF - (anchorYaw + spinDegrees);
        poseStack.mulPose(Axis.YP.rotationDegrees(correction));
    }


    static ArcCylindricalBoundingBox getHitbox(Player player, float height, float radius, ItemStack stack, RotatingAttackWeapon weapon, @Nullable SoundEvent soundEvent) {
        int useTicks = player.getTicksUsingItem();

        float currentDegs = calculateSpinRotation(useTicks, 0, weapon);
        float prevDegs = useTicks > 1 ? calculateSpinRotation(useTicks - 1, 0, weapon) : 0;
        float arcSweep = currentDegs - prevDegs;


        if (soundEvent != null && !player.level().isClientSide) {
            // Calculate which "lap" we are on
            int currentLap = (int) (currentDegs / 360f);
            int prevLap = (int) (prevDegs / 360f);

            // If the lap count increased, we passed a 360-degree threshold
            // do whatever we want here
            if (currentLap > prevLap) {
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        soundEvent, SoundSource.PLAYERS, 0.75F, 0.75F + arcSweep * 0.0001F);
            }
        }


        float anchorYaw = SPIN_ANCHORS.computeIfAbsent(player, LivingEntity::getVisualRotationYInDegrees);
        float targetYRot = anchorYaw + prevDegs;

        float finalSweep = Math.max(0.01F, arcSweep);

        return (ArcCylindricalBoundingBox) new ArcCylindricalBoundingBox(Vec3.ZERO, height, radius, 0, finalSweep)
                .inWorld(player, Vec3.ZERO, 0, targetYRot);
    }

    static float calculateSpinRotation(int ticks, RotatingAttackWeapon weapon) {
        return calculateSpinRotation(ticks, Minecraft.getInstance().getPartialTick(), weapon);
    }

    static float calculateSpinRotation(int ticks, float partialTick, RotatingAttackWeapon weapon) {
        if (weapon.acceleratesInfinitely()) {
            float accelerationRate = weapon.accelerationRateInDegrees();
            float prevRot = 0.5f * accelerationRate * ((ticks - 1) * (ticks - 1));
            float currentRot = 0.5f * accelerationRate * (ticks * ticks);
            return Mth.lerp(partialTick, prevRot, currentRot);
        }
        float maxDegPerTick = (weapon.getMaxRotationsPerSecond() * 360F) / 20F;
        float windUp = weapon.getTicksTillMaxSpeed();

        float currentRot = getRotationAtTick(ticks, windUp, maxDegPerTick);
        float prevRot = getRotationAtTick(ticks - 1, windUp, maxDegPerTick);

        return Mth.lerp(partialTick, prevRot, currentRot);
    }

    private static float getRotationAtTick(int t, float windUpTicks, float maxDeg) {
        if (t <= 0) return 0;
        if (t <= windUpTicks) {
            return (t * maxDeg) * ((t / windUpTicks) / 2.0F);
        }
        float offset = (windUpTicks * maxDeg) / 2.0F;
        return offset + (t - windUpTicks) * maxDeg;
    }
}
