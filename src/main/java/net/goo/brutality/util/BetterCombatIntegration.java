package net.goo.brutality.util;

import net.bettercombat.api.client.BetterCombatClientEvents;
import net.goo.brutality.item.base.BrutalityThrowingItem;
import net.goo.brutality.item.weapon.hammer.AtomicJudgementHammer;
import net.goo.brutality.item.weapon.sword.*;
import net.goo.brutality.network.ClientboundExactParticlePacket;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.ServerboundTriggerAnimationPacket;
import net.goo.brutality.particle.providers.FlatParticleData;
import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.animatable.GeoItem;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class BetterCombatIntegration {
    public static void register() {
        BetterCombatClientEvents.ATTACK_START.register((player, hand) -> {
            if (!(player.level() instanceof ClientLevel level)) return;
            if (hand == null) return;
            ItemStack stack = hand.isOffHand() ? player.getOffhandItem() : player.getMainHandItem();
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
                    PacketHandler.sendToServer(new ServerboundTriggerAnimationPacket(stack, GeoItem.getId(stack), "controller", "stab"));
                } else {
                    PacketHandler.sendToServer(new ServerboundTriggerAnimationPacket(stack, GeoItem.getId(stack), "controller", "swing"));
                }
            } else if (item instanceof MurasamaSword) {
                if (hand.combo().current() == 1) {

                    FlatParticleData<?> data = new FlatParticleData<>(BrutalityModParticles.MURASAMA_SLASH_PARTICLE.get(),
                            7F,
                            Mth.wrapDegrees(player.getViewXRot(1) - 20),
                            remapYaw(Mth.wrapDegrees(player.getViewYRot(1))),
                            -30,
                            0, player.getBbHeight() / 2, 0, player.getId()
                    );

                    PacketHandler.sendToServer(new ClientboundExactParticlePacket(
                            player.getX(), player.getY(0.5F), player.getZ(), data
                    ));
                }

                if (hand.combo().current() == 2) {
                    FlatParticleData<?> data = new FlatParticleData<>(BrutalityModParticles.MURASAMA_SLASH_PARTICLE.get(),
                            7F,
                            Mth.wrapDegrees(player.getViewXRot(1)),
                            remapYaw(Mth.wrapDegrees(player.getViewYRot(1))),
                            180,
                            0, player.getBbHeight() / 2, 0, player.getId()
                    );

                    PacketHandler.sendToServer(new ClientboundExactParticlePacket(
                            player.getX(), player.getY(0.5F), player.getZ(), data
                    ));
                }
            } else if (item instanceof AtomicJudgementHammer) {
                PacketHandler.sendToServer(new ServerboundTriggerAnimationPacket(stack, GeoItem.getId(stack), "controller", "attack"));
            } else if (item instanceof BrutalityThrowingItem throwingItem) {
                throwingItem.throwProjectile(player);

            }

        });
    }

    public static float remapYaw(float playerYaw) {
        return ((-playerYaw + 180f) % 360f + 360f) % 360f - 180f;
    }


}


