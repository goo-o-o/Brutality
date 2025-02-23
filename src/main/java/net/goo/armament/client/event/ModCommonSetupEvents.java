package net.goo.armament.client.event;

import net.goo.armament.entity.helper.ArmaVisualType;
import net.goo.armament.entity.helper.ArmaVisualTypes;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

public class ModCommonSetupEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRegistryCreatingEvent(NewRegistryEvent event) {
        event.create(new RegistryBuilder<ArmaVisualType>().setName(ArmaVisualTypes.VISUALS_KEY.location()).disableSaving());
    }
}