package net.goo.brutality.util.helpers;

import net.goo.brutality.Brutality;
import net.goo.brutality.registry.BrutalityMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, value = Dist.CLIENT)
public class ClientInputHelper {
    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseButton.Pre event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(BrutalityMobEffects.STUNNED.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(BrutalityMobEffects.STUNNED.get())) {
            event.setCanceled(true);
        }
    }



}
