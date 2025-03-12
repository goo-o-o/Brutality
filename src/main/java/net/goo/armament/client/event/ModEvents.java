package net.goo.armament.client.event;

import net.goo.armament.Armament;
import net.goo.armament.entity.mobs.SummonedStray;
import net.goo.armament.registry.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SUMMONED_STRAY.get(), SummonedStray.createAttributes().build());
    }

}
