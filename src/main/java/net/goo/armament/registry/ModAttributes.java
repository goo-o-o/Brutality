package net.goo.armament.registry;

import net.goo.armament.Armament;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Armament.MOD_ID);

    public static final Attribute ARMOR_SHRED = new RangedAttribute(
            "attribute.armament.armor_shred",
            0,
            0,
            1
    ).setSyncable(true);

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}
