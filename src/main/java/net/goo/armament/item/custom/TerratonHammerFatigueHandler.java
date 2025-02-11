package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.registry.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TerratonHammerFatigueHandler {

    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingTickEvent event) {
        // Check if the entity is a player
        if (event.getEntity() instanceof Player player) {
            // Check if the player is holding the Terraton Hammer in the main hand
            if (player.getMainHandItem().getItem() == ModItems.TERRATON_HAMMER.get()) {
                    // Apply the mining fatigue effect
                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 4, 8, false, false));
            }
        }
    }
}