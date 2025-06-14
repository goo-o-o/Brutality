package net.goo.brutality.util;

import net.bettercombat.api.client.BetterCombatClientEvents;
import net.goo.brutality.item.weapon.custom.TerraBladeSword;
import net.goo.brutality.item.weapon.custom.AtomicJudgementHammer;
import net.goo.brutality.item.weapon.custom.ExcaliburSword;
import net.goo.brutality.item.weapon.custom.HFMurasamaSword;
import net.goo.brutality.item.weapon.custom.SupernovaSword;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.c2sTriggerAnimPacket;
import net.goo.brutality.network.s2cEnhancedExactParticlePacket;
import net.goo.brutality.particle.custom.flat.MurasamaSlash;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import software.bernie.geckolib.animatable.GeoItem;

public class BetterCombatIntegration {


    public static void register() {
        if (ModList.get().isLoaded("bettercombat")) {
            BetterCombatClientEvents.ATTACK_START.register((player, hand) -> {
                ItemStack stack = player.getMainHandItem();
                Item item = stack.getItem();
                if (item instanceof TerraBladeSword terraBladeSword) {
                    terraBladeSword.performTerraBeam(stack, player);
                } else if (item instanceof ExcaliburSword excaliburSword) {
                    excaliburSword.performExcaliburBeam(stack, player);
                } else if (item instanceof SupernovaSword) {
                    if (hand.combo().current() == 2) { // You can get hella info from the event player and hand
                        PacketHandler.sendToServer(new c2sTriggerAnimPacket(stack, GeoItem.getId(stack), "controller", "stab"));
//                        ((SupernovaSword) item).triggerAnim(player, GeoItem.getId(stack), "controller", "stab");
//                        System.out.println(GeoItem.getId(stack));
                    } else {
                        PacketHandler.sendToServer(new c2sTriggerAnimPacket(stack, GeoItem.getId(stack), "controller", "swing"));
//                        ((SupernovaSword) item).triggerAnim(player, GeoItem.getId(stack), "controller", "swing");
//                        System.out.println(GeoItem.getId(stack));
                    }
                } else if (item instanceof HFMurasamaSword) {
                    if (hand.combo().current() == 1)
                        PacketHandler.sendToAllClients(new s2cEnhancedExactParticlePacket(
                                player.getX(),
                                player.getY() + player.getBbHeight() / 2 ,
                                player.getZ(),
                                0, 0, 0,
                                new MurasamaSlash.ParticleData( true,
                                        0, player.getBbHeight() / 2, 0,
                                        player.getId(),
                                        7.0f,
                                        Mth.wrapDegrees(player.getViewXRot(1) - 20),
                                        remapYaw(Mth.wrapDegrees(player.getViewYRot(1))),
                                        -30
                                )
                        ));

                    if (hand.combo().current() == 2)
                        PacketHandler.sendToAllClients(new s2cEnhancedExactParticlePacket(
                                player.getX(),
                                player.getY() + player.getBbHeight() / 2 ,
                                player.getZ(),
                                0, 0, 0,
                                new MurasamaSlash.ParticleData( true,
                                        0, player.getBbHeight() / 1.5F, 0,
                                        player.getId(),
                                        7.0f,
                                        Mth.wrapDegrees(player.getViewXRot(1)),
                                        remapYaw(Mth.wrapDegrees(player.getViewYRot(1))),
                                        180
                                )
                        ));
                } else if (item instanceof AtomicJudgementHammer) {
                    PacketHandler.sendToServer(new c2sTriggerAnimPacket(stack, GeoItem.getId(stack), "controller", "attack"));
                }

            });
        }
    }

    public static float remapYaw(float playerYaw) {
        return ((-playerYaw + 180f) % 360f + 360f) % 360f - 180f;
    }


}


