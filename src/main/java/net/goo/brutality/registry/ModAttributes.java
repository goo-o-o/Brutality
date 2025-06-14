package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Brutality.MOD_ID);

    public static final Attribute ARMOR_SHRED = new RangedAttribute(
            "attribute." + Brutality.MOD_ID + ".armor_shred",
            0,
            0,
            1
    ).setSyncable(true);

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}
