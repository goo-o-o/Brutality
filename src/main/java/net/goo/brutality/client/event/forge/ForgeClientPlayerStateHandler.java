package net.goo.brutality.client.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.config.BrutalityClientConfig;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.event.mod.client.Keybindings;
import net.goo.brutality.util.item.ThrowableWeaponUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static net.goo.brutality.util.EnvironmentColorManager.*;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, value = Dist.CLIENT)
public class ForgeClientPlayerStateHandler {

    public static boolean ERROR_404_EQUIPPED = false;

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


    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        resetAllColors();
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if (level == null || player == null) return;


        activeColorSources.clear();

        if (BrutalityClientConfig.BLACK_HOLE_SKY_COLOR.get()) {
            boolean blackHoleNearby = StreamSupport.stream(level.entitiesForRendering().spliterator(), false)
                    .anyMatch(e -> e.getType() == BrutalityEntities.BLACK_HOLE_ENTITY.get() && e.distanceToSqr(player) <= 10 * 10);

            apply("black_hole", blackHoleNearby, new ProximityColorSet()
                    .setColorAutoReset(ColorType.SKY, FastColor.ARGB32.color(255, 0, 0, 0))
                    .setColorAutoReset(ColorType.FOG, FastColor.ARGB32.color(255, 0, 0, 0))
            );
        }


        if (BrutalityClientConfig.BORK_SKY_COLOR.get()) {

            boolean playerNearEntityWithBork = StreamSupport.stream(level.entitiesForRendering().spliterator(), false)
                    .anyMatch(e -> e instanceof Player && ((Player) e).isHolding(BrutalityItems.BLADE_OF_THE_RUINED_KING.get()) && e.distanceToSqr(player) <= 10 * 10);


            apply("bork", playerNearEntityWithBork,
                    new ProximityColorSet().
                            setColorAutoReset(ColorType.FOG, FastColor.ARGB32.color(255, 32, 92, 91)).
//                            setColorAutoReset(ColorType.WATER, FastColor.ARGB32.color(255,32, 92, 91)).
//                            setColorAutoReset(ColorType.GRASS, FastColor.ARGB32.color(255,32, 92, 91)).
//                            setColorAutoReset(ColorType.FOLIAGE, FastColor.ARGB32.color(255,32, 92, 91)).
        setColorAutoReset(ColorType.SKY, FastColor.ARGB32.color(255, 0, 0, 0)));
        }

        boolean rayNearby = StreamSupport.stream(level.entitiesForRendering().spliterator(), false)
                .anyMatch(e -> e.getType() == BrutalityEntities.EXPLOSION_RAY.get() && e.distanceToSqr(player) <= 50 * 50);

        apply("explosion_ray", rayNearby, new ProximityColorSet()
                .setColorAutoReset(ColorType.SKY, FastColor.ARGB32.color(255, 255, 140, 0))
                .setColorAutoReset(ColorType.FOG, FastColor.ARGB32.color(255, 0, 0, 0))
        );

        resolveAndApplyColors();

        ThrowableWeaponUtils.handleAttacksWithoutBetterCombat(player);

        if (player.tickCount % 4 == 0) {
            Optional<ICuriosItemHandler> handlerOpt = CuriosApi.getCuriosInventory(player).resolve();
            handlerOpt.ifPresent(itemHandler -> ERROR_404_EQUIPPED = itemHandler.isEquipped(BrutalityItems.ERROR_404.get()));
        }

    }
}
