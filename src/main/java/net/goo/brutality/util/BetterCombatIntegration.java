package net.goo.brutality.util;

import net.bettercombat.api.client.BetterCombatClientEvents;
import net.goo.brutality.item.weapon.hammer.AtomicJudgementHammer;
import net.goo.brutality.item.weapon.sword.*;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.c2sTriggerAnimPacket;
import net.goo.brutality.network.ClientboundEnhancedParticlePacket;
import net.goo.brutality.particle.custom.flat.MurasamaSlash;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.animatable.GeoItem;

@Mod.EventBusSubscriber (value = Dist.CLIENT)
public class BetterCombatIntegration {
    public static void register() {
            BetterCombatClientEvents.ATTACK_START.register((player, hand) -> {
                if (!player.level().isClientSide()) return;
                ItemStack stack = player.getMainHandItem();
                Item item = stack.getItem();
                if (item instanceof TerraBladeSword terraBladeSword) {
                    terraBladeSword.performTerraBeam(stack, player);
                } else if (item instanceof TerratomereSword terratomereSword) {
                    terratomereSword.performTerraBeam(stack, player);
//                } else if (item instanceof ExcaliburSword excaliburSword) {
//                    excaliburSword.performExcaliburBeam(stack, player);
                } else if (item instanceof ExobladeSword exobladeSword) {
                    exobladeSword.performExobladeBeam(stack, player);
                } else if (item instanceof SeventhStarSword seventhStarSword) {
                    seventhStarSword.shootTriStar(player);
                } else if (item instanceof MarianasTrenchSword marianasTrenchSword) {
                    marianasTrenchSword.performMarianasTrenchAttack(stack, player);
                } else if (item instanceof ChallengerDeepSword challengerDeepSword) {
                    challengerDeepSword.performChallengerDeepAttack(stack, player);
                } else if (item instanceof SupernovaSword) {
                    if (hand.combo().current() == 2) {
                        PacketHandler.sendToServer(new c2sTriggerAnimPacket(stack, GeoItem.getId(stack), "controller", "stab"));
//                        ((SupernovaSword) item).triggerAnim(player, GeoItem.getId(stack), "controller", "stab");
//                        System.out.println(GeoItem.getId(stack));
                    } else {
                        PacketHandler.sendToServer(new c2sTriggerAnimPacket(stack, GeoItem.getId(stack), "controller", "swing"));
//                        ((SupernovaSword) item).triggerAnim(player, GeoItem.getId(stack), "controller", "swing");
//                        System.out.println(GeoItem.getId(stack));
                    }
                } else if (item instanceof MurasamaSword) {
                    if (hand.combo().current() == 1)
                        PacketHandler.sendToServer(new ClientboundEnhancedParticlePacket(
                                player.getX(),
                                player.getY() + player.getBbHeight() / 2,
                                player.getZ(),
                                new MurasamaSlash.ParticleData(true,
                                        0, player.getBbHeight() / 2, 0,
                                        player.getId(),
                                        7.0f,
                                        Mth.wrapDegrees(player.getViewXRot(1) - 20),
                                        remapYaw(Mth.wrapDegrees(player.getViewYRot(1))),
                                        -30
                                )
                        ));

                    if (hand.combo().current() == 2)
                        PacketHandler.sendToServer(new ClientboundEnhancedParticlePacket(
                                player.getX(),
                                player.getY() + player.getBbHeight() / 2,
                                player.getZ(),
                                new MurasamaSlash.ParticleData(true,
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

    public static float remapYaw(float playerYaw) {
        return ((-playerYaw + 180f) % 360f + 360f) % 360f - 180f;
    }


}


