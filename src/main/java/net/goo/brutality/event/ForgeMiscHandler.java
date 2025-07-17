package net.goo.brutality.event;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.weapon.generic.LastPrismItem;
import net.goo.brutality.item.weapon.unused.ViperRapierItem;
import net.goo.brutality.registry.BrutalityModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeClientMiscHandler {
    private static int dashCD = 0;

    @SubscribeEvent
    public static void onItemBreak(PlayerDestroyItemEvent event) {
        if (event.getOriginal().getOrCreateTag().getBoolean("fromDoubleDown")) {
            Player player = event.getEntity();
            if (!player.level().isClientSide()) {
                player.level().getServer().tell(new TickTask(1, () -> {
                    player.getInventory().setItem(player.getInventory().selected, BrutalityModItems.DOUBLE_DOWN_SWORD.get().getDefaultInstance());
                }));
            }
        }
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        if (event.getEntity().getItem().getOrCreateTag().getBoolean("fromDoubleDown")) {
            event.getEntity().setItem(BrutalityModItems.DOUBLE_DOWN_SWORD.get().getDefaultInstance());
        }
    }


    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.getMainHandItem().getItem() instanceof LastPrismItem) {
            if (event.getHand() == InteractionHand.OFF_HAND)
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.PlayerTickEvent event) {
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
