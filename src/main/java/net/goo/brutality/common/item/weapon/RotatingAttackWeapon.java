package net.goo.brutality.common.item.weapon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.util.math.phys.hitboxes.ArcCylindricalBoundingBox;
import net.minecraft.client.model.EntityModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;

import java.util.Map;
import java.util.WeakHashMap;

public interface RotatingAttackWeapon {
    float getMaxRotationsPerSecond();

    int getTicksTillMaxSpeed();

    Map<LivingEntity, Float> SPIN_ANCHORS = new WeakHashMap<>();

    static <T extends LivingEntity, M extends EntityModel<T>> void handleRenderEvent(RenderLivingEvent.Pre<T, M> event, RotatingAttackWeapon weapon) {
        LivingEntity entity = event.getEntity();
        PoseStack poseStack = event.getPoseStack();
        float partialTick = event.getPartialTick();
        ItemStack useItem = entity.getUseItem();

        // 1. Handle Body Rotation (The World-Lock)
        float anchorYaw = SPIN_ANCHORS.computeIfAbsent(entity, LivingEntity::getVisualRotationYInDegrees);
        float vanillaF = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);

        int useTicks = useItem.getUseDuration() - entity.getUseItemRemainingTicks();
        float spinDegrees = calculateSpinRotation(useTicks, partialTick, weapon);

        // This cancels vanilla's upcoming rotation and forces our spin + anchor
        float correction = vanillaF - (anchorYaw + spinDegrees);
        poseStack.mulPose(Axis.YP.rotationDegrees(correction));
    }

    static ArcCylindricalBoundingBox getHitbox(Player player, float height, float radius, ItemStack stack, RotatingAttackWeapon weapon) {
        int useTicks = stack.getUseDuration() - player.getUseItemRemainingTicks();

        // Calculate the arc "slice" for this specific tick
        float prevDegs = calculateSpinRotation(useTicks - 1, 0, weapon);
        float currentDegs = calculateSpinRotation(useTicks, 0, weapon);
        float arcSweep = currentDegs - prevDegs;

        // Use the player's current rotation as the base, then offset by the spin
        float anchorYaw = SPIN_ANCHORS.computeIfAbsent(player, LivingEntity::getVisualRotationYInDegrees);
        float targetYRot = anchorYaw + prevDegs;

        if (!player.level().isClientSide())
            System.out.println("targetYRot: " + targetYRot + " | arcSweep: " + arcSweep);
        // We use the 'origin' version of inWorld to ensure it pins to the player
        return (ArcCylindricalBoundingBox) new ArcCylindricalBoundingBox(Vec3.ZERO, height, 0, radius, arcSweep)
                .inWorld(player, Vec3.ZERO, 0, targetYRot);
    }


    private static float calculateSpinRotation(int ticks, float partialTick, RotatingAttackWeapon weapon) {
        float maxDegPerTick = (weapon.getMaxRotationsPerSecond() * 360F) / 20F;
        float windUp = weapon.getTicksTillMaxSpeed();

        float currentRot = getRotationAtTick(ticks, windUp, maxDegPerTick);
        float prevRot = getRotationAtTick(ticks - 1, windUp, maxDegPerTick);

        return Mth.lerp(partialTick, prevRot, currentRot);
    }

    private static float getRotationAtTick(int t, float windUp, float maxDeg) {
        if (t <= 0) return 0;
        if (t <= windUp) {
            return (t * maxDeg) * ((t / windUp) / 2.0F);
        }
        float offset = (windUp * maxDeg) / 2.0F;
        return offset + (t - windUp) * maxDeg;
    }
}
