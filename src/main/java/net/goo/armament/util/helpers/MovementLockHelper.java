package net.goo.armament.util.helpers;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;
import java.util.UUID;

public class MovementLockHelper {
    private static final UUID MOVEMENT_LOCK_UUID = UUID.fromString("a5b2c3d4-1234-5678-9101-112131415161");

    public static void applyMovementLock(Player player) {
        // Set movement speed to zero
        Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED))
                .addTransientModifier(new AttributeModifier(
                        MOVEMENT_LOCK_UUID,
                        "Movement Lock",
                        -1.0,
                        AttributeModifier.Operation.MULTIPLY_TOTAL
                ));

    }

    public static void applyFullLock(Player player) {
        // Movement speed
        applyMovementLock(player);

        player.setJumping(true);
        player.setShiftKeyDown(false);

    }

    public static void removeMovementLock(Player player) {
        Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED))
                .removeModifier(MOVEMENT_LOCK_UUID);
    }


    public static void removeFullLock(Player player) {
        removeMovementLock(player);
        player.onUpdateAbilities();
    }
}
