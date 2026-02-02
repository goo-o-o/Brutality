package net.goo.brutality.util.build_archetypes;

import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.world.entity.player.Player;

public class BloodHelper {
    public static void modifyBloodValue(Player player, float amount) {
        player.getCapability(BrutalityCapabilities.BLOOD).ifPresent(cap -> {
            cap.modifyBloodValue(amount);
            BrutalityCapabilities.sync(player, BrutalityCapabilities.BLOOD);
        });
    }

    public static void setBloodValue(Player player, float amount) {
        player.getCapability(BrutalityCapabilities.BLOOD).ifPresent(cap -> {
            cap.setBlood(amount);
            BrutalityCapabilities.sync(player, BrutalityCapabilities.BLOOD);
        });
    }

    public static float getCurrentBloodPercentage(Player player) {
        return player.getCapability(BrutalityCapabilities.BLOOD).map(cap -> cap.getBlood() / player.getAttributeValue(BrutalityAttributes.MAX_BLOOD.get())).orElse(0D).floatValue();
    }

}