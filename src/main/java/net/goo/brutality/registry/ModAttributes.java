package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Brutality.MOD_ID);

    public static final RegistryObject<Attribute> RAGE_TIME_MULTIPLIER = ATTRIBUTES.register("rage_time_multiplier",
            () -> new RangedAttribute("attribute.name.generic.rage_time_multiplier", 1.0D, 0.0F, 128F).setSyncable(true));

    public static final RegistryObject<Attribute> RAGE_LEVEL = ATTRIBUTES.register("rage_level",
            () -> new RangedAttribute("attribute.name.generic.rage_level", 0.0D, 0.0F, 128F).setSyncable(true));

    public static final RegistryObject<Attribute> MAX_RAGE = ATTRIBUTES.register("max_rage",
            () -> new RangedAttribute("attribute.name.generic.max_rage", 100.0, 0.0F, 16384F).setSyncable(true));

    public static final RegistryObject<Attribute> RAGE_GAIN_MULTIPLIER = ATTRIBUTES.register("rage_gain_multiplier",
            () -> new RangedAttribute("attribute.name.generic.rage_gain", 1.0F, 0.0F, 128F).setSyncable(true));


    public static final RegistryObject<Attribute> MANA_COST = ATTRIBUTES.register("mana_cost",
            () -> new RangedAttribute("attribute.name.generic.mana_cost", 1.0D, 0.0F, 128F).setSyncable(true));

    public static final RegistryObject<Attribute> MANA_REGEN = ATTRIBUTES.register("mana_regen",
            () -> new RangedAttribute("attribute.name.generic.mana_regen", 10D, 0.0F, 16384F).setSyncable(true));

    public static final RegistryObject<Attribute> MAX_MANA = ATTRIBUTES.register("max_mana",
            () -> new RangedAttribute("attribute.name.generic.max_mana", 100.0, 0.0F, 16384F).setSyncable(true));

    public static final RegistryObject<Attribute> SPELL_COOLDOWN_REDUCTION = ATTRIBUTES.register("spell_cooldown_reduction",
            () -> new RangedAttribute("attribute.name.generic.spell_cooldown_reduction", 0.0, 0.0F, 1F).setSyncable(true));

    public static final RegistryObject<Attribute> DAEMONIC_SCHOOL_LEVEL = ATTRIBUTES.register("daemonic_school_level",
            () -> new RangedAttribute("attribute.name.generic.daemonic_school_level", 0.0, -1024F, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> DARKIST_SCHOOL_LEVEL = ATTRIBUTES.register("darkist_school_level",
            () -> new RangedAttribute("attribute.name.generic.darkist_school_level", 0.0, -1024F, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> EVERGREEN_SCHOOL_LEVEL = ATTRIBUTES.register("evergreen_school_level",
            () -> new RangedAttribute("attribute.name.generic.evergreen_school_level", 0.0, -1024F, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> VOLTWEAVER_SCHOOL_LEVEL = ATTRIBUTES.register("voltweaver_school_level",
            () -> new RangedAttribute("attribute.name.generic.voltweaver_school_level", 0.0, -1024F, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> COSMIC_SCHOOL_LEVEL = ATTRIBUTES.register("cosmic_school_level",
            () -> new RangedAttribute("attribute.name.generic.cosmic_school_level", 0.0, -1024F, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> CELESTIA_SCHOOL_LEVEL = ATTRIBUTES.register("celestia_school_level",
            () -> new RangedAttribute("attribute.name.generic.celestia_school_level", 0.0, -1024F, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> UMBRANCY_SCHOOL_LEVEL = ATTRIBUTES.register("umbrancy_school_level",
            () -> new RangedAttribute("attribute.name.generic.umbrancy_school_level", 0.0, -1024F, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> EXODIC_SCHOOL_LEVEL = ATTRIBUTES.register("exodic_school_level",
            () -> new RangedAttribute("attribute.name.generic.exodic_school_level", 0.0, -1024F, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> BRIMWIELDER_SCHOOL_LEVEL = ATTRIBUTES.register("brimwielder_school_level",
            () -> new RangedAttribute("attribute.name.generic.brimwielder_school_level", 0.0, -1024F, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> VOIDWALKER_SCHOOL_LEVEL = ATTRIBUTES.register("voidwalker_school_level",
            () -> new RangedAttribute("attribute.name.generic.voidwalker_school_level", 0.0, -1024F, 1024F).setSyncable(true));



    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }



}
