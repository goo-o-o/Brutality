package net.goo.brutality.event;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.mobs.SummonedStray;
import net.goo.brutality.registry.BrutalityModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.goo.brutality.registry.ModAttributes.*;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrutalityModEvents {

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(BrutalityModEntities.SUMMONED_STRAY.get(), SummonedStray.createAttributes().build());
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeModificationEvent event) {
        if (!event.has(EntityType.PLAYER, RAGE_TIME_MULTIPLIER.get()))
            event.add(EntityType.PLAYER, RAGE_TIME_MULTIPLIER.get());

        if (!event.has(EntityType.PLAYER, RAGE_GAIN_MULTIPLIER.get()))
            event.add(EntityType.PLAYER, RAGE_GAIN_MULTIPLIER.get());

        if (!event.has(EntityType.PLAYER, MAX_RAGE.get()))
            event.add(EntityType.PLAYER, MAX_RAGE.get());

        if (!event.has(EntityType.PLAYER, RAGE_LEVEL.get()))
            event.add(EntityType.PLAYER, RAGE_LEVEL.get());

    }


}
