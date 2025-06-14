package net.goo.brutality.event;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.mobs.SummonedStray;
import net.goo.brutality.registry.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrutalityModEvents {

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SUMMONED_STRAY.get(), SummonedStray.createAttributes().build());
    }

}
