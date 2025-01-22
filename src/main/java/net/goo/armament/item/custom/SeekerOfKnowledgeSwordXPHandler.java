package net.goo.armament.item.custom;

import net.goo.armament.item.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SeekerOfKnowledgeSwordXPHandler {

    private static final float EXPERIENCE_BOOST = 2.0F;

    public static void register() {
        // Register on the Forge event bus
        MinecraftForge.EVENT_BUS.register(SeekerOfKnowledgeSwordXPHandler.class);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onXPChange(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();

        // Check if the player is holding the Seeker of Knowledge sword
        if (player.getMainHandItem().getItem() == ModItems.SEEKER_OF_KNOWLEDGE_SWORD.get()) {
            int originalXP = event.getAmount();
            int boostedXP = (int) (originalXP * EXPERIENCE_BOOST);
            event.setAmount(boostedXP);
        }
    }
}
