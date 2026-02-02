package net.goo.brutality.common.item.curios.charm;

import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class OmnidirectionalMovementGear extends BrutalityCurioItem {

    public OmnidirectionalMovementGear(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    /**
     * Determines if the player is providing sufficient movement input to maintain an active sprint.
     * <p>
     * This handles the "continuous check" that happens every tick while a player is already sprinting.
     * If the Omnidirectional Gear is equipped, the player can maintain a sprint while moving in any
     * direction (including backwards or sideways). Otherwise, the player must maintain forward
     * momentum to keep sprinting.
     * </p>
     *
     * @param player The local client-side player.
     * @param input  The current movement input (keyboard or controller state).
     * @return {@code true} if the current movement inputs satisfy the requirements to keep sprinting.
     */
    public static boolean handleOmnidirectionalImpulseToMaintainSprint(LocalPlayer player, Input input) {
        return CuriosApi.getCuriosInventory(player).resolve().map(handler -> {
            if (handler.isEquipped(BrutalityItems.OMNIDIRECTIONAL_MOVEMENT_GEAR.get())) {
                // Allow maintaining sprint as long as there is ANY directional input.
                return input.forwardImpulse != 0.0F || input.leftImpulse != 0.0F;
            } else {
                // VANILLA LOGIC: Player must be holding 'W' (or equivalent forward stick).
                // 1.0E-5F is used as a small epsilon to prevent floating-point errors.
                return input.forwardImpulse > 1.0E-5F;
            }
        }).orElseGet(() -> input.forwardImpulse > 1.0E-5F); // Fallback to vanilla if Curios is missing
    }

    /**
     * Determines if the player has enough directional input to initiate or maintain a sprint.
     * <p>
     * Under vanilla logic, Minecraft only allows sprinting if {@code forwardImpulse > 0}.
     * This method overrides that check if the player has Omnidirectional Gear equipped,
     * allowing for side-to-side (strafing) sprints.
     * </p>
     *
     * @param player The local client-side player.
     * @return An {@link Optional} containing the sprint eligibility:
     * <ul>
     * <li>{@code Optional.of(true/false)}: Override vanilla logic with this result.</li>
     * <li>{@code Optional.empty()}: No gear equipped; proceed with vanilla logic.</li>
     * </ul>
     */
    public static Optional<Boolean> getOmnidirectionalImpulse(LocalPlayer player) {
        // Vanilla behavior: Swimming always requires forward momentum to sprint.
        if (player.isUnderWater()) {
            return Optional.of(player.input.hasForwardImpulse());
        }

        // We use AtomicReference because variables accessed inside a lambda (ifPresent)
        // must be effectively final. This acts as a mutable container for our result.
        AtomicReference<Boolean> result = new AtomicReference<>(null);

        // Access the Curios capability. This is a LazyOptional, so we use ifPresent
        // to ensure we only run the logic if the inventory is actually available.
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityItems.OMNIDIRECTIONAL_MOVEMENT_GEAR.get())) {
                // Check for ANY movement input.
                // forwardImpulse: Positive (W), Negative (S)
                // leftImpulse: Positive (A), Negative (D)
                boolean hasAnyInput = Math.abs(player.input.forwardImpulse) > 1.0E-5F ||
                        Math.abs(player.input.leftImpulse) > 1.0E-5F;
                result.set(hasAnyInput);
            }
        });

        return Optional.ofNullable(result.get());
    }


    /**
     * Calculates a 3D world-space velocity boost for jumping, based on the entity's
     * internal movement inputs (strafe and forward/back).
     * * <p>By default, Minecraft's jump boost only applies to the "Forward" vector.
     * This method intercepts that logic to allow for "Omni-directional" boosts,
     * enabling strafe-jumping and backward-jumping with consistent momentum.</p>
     * * <p><b>Mathematical Logic:</b>
     * <ol>
     * <li>The method checks for the {@code OMNIDIRECTIONAL_MOVEMENT_GEAR} in a Curios slot.</li>
     * <li>It captures the local input variables {@code xxa} (strafe) and {@code zza} (forward).</li>
     * <li>It transforms these local inputs into world-space X and Z coordinates using the
     * entity's current Y-Rotation (Yaw).</li>
     * <li>The resulting vector is normalized to prevent "diagonal speedup" and scaled
     * by a fixed constant (0.2D).</li>
     * </ol>
     * </p>
     * *
     *
     * @param entity The {@link LivingEntity} performing the jump.
     * @return A {@link Vec3} containing the X and Z velocity additions to be applied;
     * returns {@code null} if no custom gear is equipped or no movement input
     * is detected, signaling that vanilla logic should proceed.
     * * @see net.minecraft.world.entity.LivingEntity#jumpFromGround()
     */
    @Nullable
    public static Vec3 getOmnidirectionalJumpBoost(LivingEntity entity) {
        return CuriosApi.getCuriosInventory(entity).resolve().map(handler -> {
            // Gear Check
            if (handler.isEquipped(BrutalityItems.OMNIDIRECTIONAL_MOVEMENT_GEAR.get())) {

                // Capture movement inputs
                float strafe = entity.xxa;  // Left (+), Right (-)
                float forward = entity.zza; // Forward (+), Back (-)
                double magnitudeSq = strafe * strafe + forward * forward;

                // Epsilon check to prevent Division by Zero or boosts from a standstill
                if (magnitudeSq > 1.0E-6D) {
                    // Convert degrees to radians for trigonometric functions
                    float yaw = entity.getYRot() * ((float) Math.PI / 180F);
                    float sinYaw = Mth.sin(yaw);
                    float cosYaw = Mth.cos(yaw);

                    // Transform local impulse into world-space delta
                    double boostX = (strafe * cosYaw - forward * sinYaw);
                    double boostZ = (forward * cosYaw + strafe * sinYaw);

                    // Normalize the vector so diagonal jumping isn't faster than cardinal jumping
                    double magnitude = Math.sqrt(magnitudeSq);

                    // Return the final world-space boost vector scaled to 0.2 (vanilla sprint-jump parity)
                    return new Vec3(boostX / magnitude, 0, boostZ / magnitude).scale(0.2D);
                }
            }

            // Return null to allow the Mixin to fall through to vanilla or other gear logic
            return null;
        }).orElse(null);
    }

}
