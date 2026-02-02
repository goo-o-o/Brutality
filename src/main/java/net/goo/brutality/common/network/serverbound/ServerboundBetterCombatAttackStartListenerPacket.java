package net.goo.brutality.common.network.serverbound;

import net.goo.brutality.common.item.base.BrutalityThrowingItem;
import net.goo.brutality.common.item.weapon.hammer.AtomicJudgementHammer;
import net.goo.brutality.common.item.weapon.scythe.Schism;
import net.goo.brutality.common.item.weapon.spear.Rhongomyniad;
import net.goo.brutality.common.item.weapon.sword.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import software.bernie.geckolib.animatable.GeoItem;

import java.util.function.Supplier;

public class ServerboundBetterCombatAttackStartListenerPacket {
    private final ItemStack stack;
    private final int combo;

    public ServerboundBetterCombatAttackStartListenerPacket(ItemStack stack, int combo) {
        this.stack = stack;
        this.combo = combo;
    }

    public ServerboundBetterCombatAttackStartListenerPacket(FriendlyByteBuf buf) {
        this.stack = buf.readItem();
        this.combo = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeItem(stack);
        buf.writeInt(combo);
    }

    public static void handle(ServerboundBetterCombatAttackStartListenerPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            ServerLevel serverLevel = player.serverLevel();
            ItemStack stack = packet.stack;
            Item item = stack.getItem();
            int combo = packet.combo;
            if (item instanceof ExobladeSword exobladeSword) {
                exobladeSword.performExobladeBeam(stack, player);
            } else if (item instanceof SeventhStarSword seventhStarSword) {
                seventhStarSword.shootTriStar(player);
            } else if (item instanceof MarianasTrenchSword marianasTrenchSword) {
                marianasTrenchSword.performMarianasTrenchAttack(stack, player);
            } else if (item instanceof ChallengerDeepSword challengerDeepSword) {
                challengerDeepSword.performChallengerDeepAttack(stack, player);
            } else if (item instanceof SupernovaSword supernovaSword) {
                if (combo == 2) {
                    supernovaSword.triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "stab");
                } else {
                    supernovaSword.triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "swing");
                }
            } else if (item instanceof AtomicJudgementHammer atomicJudgementHammer) {
                atomicJudgementHammer.triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "attack");
            } else if (item instanceof BrutalityThrowingItem throwingItem) {
                throwingItem.handleCooldownAndSound(player, stack);
            } else if (item instanceof CrimsonScissorBlade crimsonScissorBlade) {
                crimsonScissorBlade.performBloodSlash(player);
            } else if (item instanceof ShadowflameScissorBlade shadowflameScissorBlade) {
                shadowflameScissorBlade.performShadowflameSlash(player);
            } else if (item instanceof Rhongomyniad rhongomyniad) {
                rhongomyniad.performRayAttack(player);
            } else if (item instanceof Schism schism) {
                schism.performVoidSlash(player, serverLevel, packet.combo);
            }
        });
        context.setPacketHandled(true);
    }
}