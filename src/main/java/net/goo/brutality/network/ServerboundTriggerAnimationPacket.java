package net.goo.brutality.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

import java.util.function.Supplier;

public class ServerboundTriggerAnimationPacket {
    private final ItemStack stack;
    private final String controllerName, animationName;
    private final long id;

    public ServerboundTriggerAnimationPacket(ItemStack stack, long id, String controllerName, String animationName) {
        this.stack = stack;
        this.id = id;
        this.controllerName = controllerName;
        this.animationName = animationName;
    }

    public ServerboundTriggerAnimationPacket(FriendlyByteBuf buf) {
        this.stack = buf.readItem();
        this.id = buf.readLong();
        this.controllerName = buf.readUtf();
        this.animationName = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(stack);
        buf.writeLong(id);
        buf.writeUtf(controllerName);
        buf.writeUtf(animationName);
    }

    public static void handle(ServerboundTriggerAnimationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player sender = ctx.get().getSender();
            assert sender != null;
            if (sender.level() instanceof ServerLevel) {

                ((SingletonGeoAnimatable) sender.getMainHandItem().getItem()).triggerAnim(sender, GeoItem.getOrAssignId(sender.getMainHandItem(), ((ServerLevel) sender.level())), packet.controllerName, packet.animationName);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
