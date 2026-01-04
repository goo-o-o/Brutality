package net.goo.brutality.event.mod;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.mobs.SummonedStray;
import net.goo.brutality.registry.BrutalityModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.goo.brutality.registry.ModAttributes.ATTRIBUTES;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrutalityModEvents {

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(BrutalityModEntities.SUMMONED_STRAY.get(), SummonedStray.createAttributes().build());
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeModificationEvent event) {
        ATTRIBUTES.getEntries().forEach(attribute -> {
            if (!event.has(EntityType.PLAYER, attribute.get())) {
                event.add(EntityType.PLAYER, attribute.get());
            }
        });

    }







}
