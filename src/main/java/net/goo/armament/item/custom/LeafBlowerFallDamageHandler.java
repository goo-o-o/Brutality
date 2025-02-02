package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LeafBlowerFallDamageHandler {
    private static final String ACTIVE_KEY = "LeafBlowerActive";

    @SubscribeEvent
    public static void cancelFallDamage(LivingFallEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack mainStack = player.getMainHandItem();
            ItemStack offStack = player.getOffhandItem();
            Item mainItem = mainStack.getItem();
            Item offItem = offStack.getItem();

            if (mainItem instanceof LeafBlowerItem && offItem instanceof LeafBlowerItem) {
                if (mainStack.getOrCreateTag().getBoolean(ACTIVE_KEY) && offStack.getOrCreateTag().getBoolean(ACTIVE_KEY)) {
                    event.setCanceled(true);

                }
            }
        }
    }
}
