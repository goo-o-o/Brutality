package net.goo.brutality.client.event.forge;

import net.goo.brutality.event.mod.client.Keybindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class InputEvents {
    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        // 1. EQUALIZE SPEEDS: Set all non-jumping impulses to the forward impulse (max speed)
        // The forwardImpulse will be 1.0F if moving forward, -1.0F if moving backward, or 0.0F.
        // We want the magnitude (absolute value) of the impulse to be used for all directions,
        // effectively treating strafing and backing up at the same speed as walking forward.
        float maxImpulse = Math.max(Math.abs(event.getInput().forwardImpulse), Math.abs(event.getInput().leftImpulse));

        // Apply the MAX magnitude (1.0F when keys are pressed) while retaining the original direction sign.

        // Check if moving forward/backward (zza). If so, keep the original sign (+1 or -1) but set magnitude to max.
        if (event.getInput().forwardImpulse != 0.0F) {
            event.getInput().forwardImpulse = Math.signum(event.getInput().forwardImpulse) * maxImpulse;
        }

        // Check if moving left/right (xxa). If so, keep the original sign (+1 or -1) but set magnitude to max.
        if (event.getInput().leftImpulse != 0.0F) {
            event.getInput().leftImpulse = Math.signum(event.getInput().leftImpulse) * maxImpulse;
        }

        // Since the raw impulses are only -1, 0, or 1, and the maxImpulse will be 1.0F if any key is down,
        // this effectively forces:
        // Forward (1.0F) -> 1.0F
        // Backward (-1.0F) -> -1.0F (Now treated as max speed by the movement code)
        // Strafe Left (-1.0F) -> -1.0F (Now treated as max speed by the movement code)
        // Strafe Right (1.0F) -> 1.0F (Now treated as max speed by the movement code)

        // 2. DIAGONAL FIX (Normalization):
        float forward = event.getInput().forwardImpulse;
        float strafe = event.getInput().leftImpulse;
        float magnitude = (float) Math.sqrt(forward * forward + strafe * strafe);

        if (magnitude > 1.0f) {
            // Normalize the impulses: new_impulse = old_impulse / magnitude
            event.getInput().forwardImpulse /= magnitude;
            event.getInput().leftImpulse /= magnitude;
        }
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if (level == null || player == null) return;

        Keybindings.handleKeyPress(event, player);
    }
}
