package net.goo.armament.client.event;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.ViperRapierItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientKeybindHandler {
    private static int dashCD = 0;

    @SubscribeEvent
    public static void clientTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayer sPlayer) {
            Player player = event.player;
            ItemStack mainStack = player.getMainHandItem();
            ItemStack offStack = player.getOffhandItem();
            Item mainItem = mainStack.getItem();
            Item offItem = offStack.getItem();

            if (Keybindings.DASH_ABILITY_KEY.get().consumeClick() && dashCD <= 0) {
                if (mainItem instanceof ViperRapierItem || offItem instanceof ViperRapierItem) {
                    performDash(sPlayer, 1);

                    // Set the cooldown
                    dashCD = 40;

                    // Apply the cooldown to the corresponding item
                    Item itemToCooldown = mainItem instanceof ViperRapierItem ? mainItem : offItem;
                    player.getCooldowns().addCooldown(itemToCooldown, 40);
                }
            } else if (dashCD > 0) {
                dashCD--;
            }



        }
    }

    public static void performDash(ServerPlayer player, float scale) {
        float yaw = player.getYRot();
        double radians = Math.toRadians(yaw);
        Options options = Minecraft.getInstance().options;
        Vec3 dashDirection;
        float horizontalMovementX;
        float horizontalMovementZ;

        if (options.keyUp.isDown()) {
            horizontalMovementX = (float) -Math.sin(radians);
            horizontalMovementZ = (float) Math.cos(radians);
        } else if (options.keyRight.isDown()) {
            horizontalMovementX = (float) -Math.sin(radians + Math.PI / 2);
            horizontalMovementZ = (float) Math.cos(radians + Math.PI / 2);
        } else if (options.keyLeft.isDown()) {
            horizontalMovementX = (float) -Math.sin(radians - Math.PI / 2);
            horizontalMovementZ = (float) Math.cos(radians - Math.PI / 2);
        } else {
            horizontalMovementX = (float) -Math.sin(radians - Math.PI);
            horizontalMovementZ = (float) Math.cos(radians - Math.PI);
        }
        dashDirection = new Vec3(horizontalMovementX, 0.0, horizontalMovementZ);
        player.addDeltaMovement(dashDirection);
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
    }
}
