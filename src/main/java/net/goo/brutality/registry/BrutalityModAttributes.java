package net.goo.brutality.registry;

import com.google.common.collect.ImmutableMap;
import net.goo.brutality.Brutality;
import net.goo.brutality.magic.IBrutalitySpell;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Supplier;

public class BrutalityModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Brutality.MOD_ID);

    public static final RegistryObject<Attribute> RAGE_TIME = ATTRIBUTES.register("rage_time",
            () -> new RangedAttribute("attribute.name.generic.rage_time", 2.0D, 0.0F, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> AIR_FRICTION = ATTRIBUTES.register("air_friction",
            () -> new RangedAttribute("attribute.name.generic.air_friction", 1.0D, 0.0F, 128F).setSyncable(true));

    public static final RegistryObject<Attribute> GROUND_FRICTION = ATTRIBUTES.register("ground_friction",
            () -> new RangedAttribute("attribute.name.generic.ground_friction", 1.0D, 0.0F, 128F).setSyncable(true));

    public static final RegistryObject<Attribute> RAGE_LEVEL = ATTRIBUTES.register("rage_level",
            () -> new RangedAttribute("attribute.name.generic.rage_level", 0.0D, 0.0F, 128F).setSyncable(true));

    public static final RegistryObject<Attribute> MAX_RAGE = ATTRIBUTES.register("max_rage",
            () -> new RangedAttribute("attribute.name.generic.max_rage", 100.0, 0.0F, 16384F).setSyncable(true));

    public static final RegistryObject<Attribute> DAMAGE_TO_RAGE_RATIO = ATTRIBUTES.register("damage_to_rage_ratio",
            () -> new RangedAttribute("attribute.name.generic.damage_to_rage_ratio", 0.15F, 0.0F, 128F).setSyncable(true));


    public static final RegistryObject<Attribute> MANA_COST = ATTRIBUTES.register("mana_cost",
            () -> new RangedAttribute("attribute.name.generic.mana_cost", 1.0D, 0.0F, 128F).setSyncable(true));

    public static final RegistryObject<Attribute> MANA_REGEN = ATTRIBUTES.register("mana_regen",
            () -> new RangedAttribute("attribute.name.generic.mana_regen", 10.0D, 0.0F, 16384F).setSyncable(true));

    public static final RegistryObject<Attribute> MAX_MANA = ATTRIBUTES.register("max_mana",
            () -> new RangedAttribute("attribute.name.generic.max_mana", 100.0, 0.0F, 16384F).setSyncable(true));

    public static final RegistryObject<Attribute> SPELL_COOLDOWN_REDUCTION = ATTRIBUTES.register("spell_cooldown_reduction",
            () -> new RangedAttribute("attribute.name.generic.spell_cooldown_reduction", 1, 1, 2F).setSyncable(true));

    public static final RegistryObject<Attribute> CAST_TIME_REDUCTION = ATTRIBUTES.register("cast_time_reduction",
            () -> new RangedAttribute("attribute.name.generic.cast_time_reduction", 1, 1, 2F).setSyncable(true));

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

    public static final RegistryObject<Attribute> SPELL_DAMAGE = ATTRIBUTES.register("spell_damage",
            () -> new RangedAttribute("attribute.name.generic.spell_damage", 1, -1024F, 1024F).setSyncable(true));


    public static final RegistryObject<Attribute> CRITICAL_STRIKE_CHANCE = ATTRIBUTES.register("critical_strike_chance",
            () -> new RangedAttribute("attribute.name.generic.critical_strike_chance", 1, 1, 2).setSyncable(true));
    public static final RegistryObject<Attribute> CRITICAL_STRIKE_DAMAGE = ATTRIBUTES.register("critical_strike_damage",
            () -> new RangedAttribute("attribute.name.generic.critical_strike_damage", 1.4, -1024F, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> LIFESTEAL = ATTRIBUTES.register("lifesteal",
            () -> new RangedAttribute("attribute.name.generic.lifesteal", 1, 1.0F, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> OMNIVAMP = ATTRIBUTES.register("omnivamp",
            () -> new RangedAttribute("attribute.name.generic.omnivamp", 1, 1.0F, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> DODGE_CHANCE = ATTRIBUTES.register("dodge_chance",
            () -> new RangedAttribute("attribute.name.generic.dodge_chance", 1, 1.0F, 1.95F).setSyncable(true));
    public static final RegistryObject<Attribute> JUMP_HEIGHT = ATTRIBUTES.register("jump_height",
            () -> new RangedAttribute("attribute.name.generic.jump_height", 1.2522, 0, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> STUN_CHANCE = ATTRIBUTES.register("stun_chance",
            () -> new RangedAttribute("attribute.name.generic.stun_chance", 1, 1, 2F).setSyncable(true));
    public static final RegistryObject<Attribute> STUN_DURATION = ATTRIBUTES.register("stun_duration",
            () -> new RangedAttribute("attribute.name.generic.stun_duration", 0.1, 0.1, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> TENACITY = ATTRIBUTES.register("tenacity",
            () -> new RangedAttribute("attribute.name.generic.tenacity", 1, 1, 1.99F).setSyncable(true));

    public static final RegistryObject<Attribute> BLUNT_DAMAGE = ATTRIBUTES.register("blunt_damage",
            () -> new RangedAttribute("attribute.name.generic.blunt_damage", 0, 0, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> PIERCING_DAMAGE = ATTRIBUTES.register("piercing_damage",
            () -> new RangedAttribute("attribute.name.generic.piercing_damage", 0, 0, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> SLASH_DAMAGE = ATTRIBUTES.register("slash_damage",
            () -> new RangedAttribute("attribute.name.generic.slash_damage", 0, 0, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> AXE_DAMAGE = ATTRIBUTES.register("axe_damage",
            () -> new RangedAttribute("attribute.name.generic.axe_damage", 0, 0, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> SWORD_DAMAGE = ATTRIBUTES.register("sword_damage",
            () -> new RangedAttribute("attribute.name.generic.sword_damage", 0, 0, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> HAMMER_DAMAGE = ATTRIBUTES.register("hammer_damage",
            () -> new RangedAttribute("attribute.name.generic.hammer_damage", 0, 0, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> SPEAR_DAMAGE = ATTRIBUTES.register("spear_damage",
            () -> new RangedAttribute("attribute.name.generic.spear_damage", 0, 0, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> SCYTHE_DAMAGE = ATTRIBUTES.register("scythe_damage",
            () -> new RangedAttribute("attribute.name.generic.scythe_damage", 0, 0, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> LETHALITY = ATTRIBUTES.register("lethality",
            () -> new RangedAttribute("attribute.name.generic.lethality", 0, 0, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> ARMOR_PENETRATION = ATTRIBUTES.register("armor_penetration",
            () -> new RangedAttribute("attribute.name.generic.armor_penetration", 1, 1, 1024F).setSyncable(true));

    public static final RegistryObject<Attribute> ENTITY_VISIBILITY = ATTRIBUTES.register("entity_visibility",
            () -> new RangedAttribute("attribute.name.generic.entity_visibility", 1, 0, 1).setSyncable(true));

    public static final RegistryObject<Attribute> THROW_STRENGTH = ATTRIBUTES.register("throw_strength",
            () -> new RangedAttribute("attribute.name.generic.throw_strength", 1, 0, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> DAMAGE_TAKEN = ATTRIBUTES.register("damage_taken",
            () -> new RangedAttribute("attribute.name.generic.damage_taken", 1, 0, 1024F).setSyncable(true));


    private static final Supplier<Map<IBrutalitySpell.MagicSchool, Attribute>> SPELL_SCHOOL_ATTRIBUTE_MAP_SUPPLIER = () -> ImmutableMap.of(
            IBrutalitySpell.MagicSchool.DAEMONIC, BrutalityModAttributes.DAEMONIC_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.DARKIST, BrutalityModAttributes.DARKIST_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.EVERGREEN, BrutalityModAttributes.EVERGREEN_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.VOLTWEAVER, BrutalityModAttributes.VOLTWEAVER_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.COSMIC, BrutalityModAttributes.COSMIC_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.CELESTIA, BrutalityModAttributes.CELESTIA_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.EXODIC, BrutalityModAttributes.EXODIC_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.BRIMWIELDER, BrutalityModAttributes.BRIMWIELDER_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.VOIDWALKER, BrutalityModAttributes.VOIDWALKER_SCHOOL_LEVEL.get()
    );

    private static Map<IBrutalitySpell.MagicSchool, Attribute> SPELL_SCHOOL_ATTRIBUTE_MAP;

    public static Map<IBrutalitySpell.MagicSchool, Attribute> getSpellSchoolAttributeMap() {
        if (SPELL_SCHOOL_ATTRIBUTE_MAP == null) {
            SPELL_SCHOOL_ATTRIBUTE_MAP = SPELL_SCHOOL_ATTRIBUTE_MAP_SUPPLIER.get();
        }
        return SPELL_SCHOOL_ATTRIBUTE_MAP;
    }

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }



}
