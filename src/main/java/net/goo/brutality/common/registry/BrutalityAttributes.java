package net.goo.brutality.common.registry;

import net.goo.brutality.Brutality;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registry class for all custom attributes introduced by the Brutality mod.
 * Includes mechanics for Rage, Mana, Magic Schools, and advanced Combat stats.
 */
public class BrutalityAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Brutality.MOD_ID);

    /** The duration (in seconds) that the Rage state remains active. */
    public static final RegistryObject<Attribute> RAGE_TIME = ATTRIBUTES.register("rage_time",
            () -> new RangedAttribute("attribute.name.generic.rage_time", 2.0D, 0.0F, 1024F).setSyncable(true));

    /** Air resistance multiplier. 1.0 represents standard vanilla friction. */
    public static final RegistryObject<Attribute> AIR_FRICTION = ATTRIBUTES.register("air_friction",
            () -> new RangedAttribute("attribute.name.generic.air_friction", 1.0D, 0.0F, 128F).setSyncable(true));

    /** Ground resistance multiplier. 1.0 represents standard vanilla friction. */
    public static final RegistryObject<Attribute> GROUND_FRICTION = ATTRIBUTES.register("ground_friction",
            () -> new RangedAttribute("attribute.name.generic.ground_friction", 1.0D, 0.0F, 128F).setSyncable(true));

    /** Current intensity level of the player's Rage. */
    public static final RegistryObject<Attribute> RAGE_LEVEL = ATTRIBUTES.register("rage_level",
            () -> new RangedAttribute("attribute.name.generic.rage_level", 0.0D, 0.0F, 128F).setSyncable(true));

    /** Maximum capacity of the Rage meter. */
    public static final RegistryObject<Attribute> MAX_RAGE = ATTRIBUTES.register("max_rage",
            () -> new RangedAttribute("attribute.name.generic.max_rage", 100.0, 0.0F, 16384F).setSyncable(true));

    /** The ratio at which dealt/received damage is converted into Rage points. (e.g., 0.15 = 15%). */
    public static final RegistryObject<Attribute> DAMAGE_TO_RAGE_RATIO = ATTRIBUTES.register("damage_to_rage_ratio",
            () -> new RangedAttribute("attribute.name.generic.damage_to_rage_ratio", 0.15F, 0.0F, 128F).setSyncable(true));

    /** Multiplier for spell costs. 1.0 is base cost; lower values represent efficiency. */
    public static final RegistryObject<Attribute> MANA_COST = ATTRIBUTES.register("mana_cost",
            () -> new RangedAttribute("attribute.name.generic.mana_cost", 1.0, 0.0F, 128F).setSyncable(true));

    /** Amount of mana regenerated per second/tick. */
    public static final RegistryObject<Attribute> MANA_REGEN = ATTRIBUTES.register("mana_regen",
            () -> new RangedAttribute("attribute.name.generic.mana_regen", 10.0D, 0.0F, 16384F).setSyncable(true));

    /** Maximum capacity of the Mana pool. */
    public static final RegistryObject<Attribute> MAX_MANA = ATTRIBUTES.register("max_mana",
            () -> new RangedAttribute("attribute.name.generic.max_mana", 100.0, 0.0F, 16384F).setSyncable(true));

    /** Maximum capacity of the Blood pool. */
    public static final RegistryObject<Attribute> MAX_BLOOD = ATTRIBUTES.register("max_blood",
            () -> new RangedAttribute("attribute.name.generic.max_blood", 100.0, 0.0F, 16384F).setSyncable(true));

    /** Percentage reduction for spell cooldowns. 1.0 = 100% reduction (No Cooldown). */
    public static final RegistryObject<Attribute> SPELL_COOLDOWN = ATTRIBUTES.register("spell_cooldown",
            () -> new RangedAttribute("attribute.name.generic.spell_cooldown", 1, 0, 128F).setSyncable(true));

    /** Multiplier for spell casting duration. */
    public static final RegistryObject<Attribute> CAST_TIME = ATTRIBUTES.register("cast_time",
            () -> new RangedAttribute("attribute.name.generic.cast_time", 1, 0, 128F).setSyncable(true));

    // Magic School Levels
    public static final RegistryObject<Attribute> UNIVERSAL_SCHOOL_LEVEL = ATTRIBUTES.register("universal_school_level", () ->
            new RangedAttribute("attribute.name.generic.universal_school_level", 0.0, -1024F, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> DAEMONIC_SCHOOL_LEVEL = ATTRIBUTES.register("daemonic_school_level", () ->
            new RangedAttribute("attribute.name.generic.daemonic_school_level", 0.0, -1024F, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> DARKIST_SCHOOL_LEVEL = ATTRIBUTES.register("darkist_school_level", () ->
            new RangedAttribute("attribute.name.generic.darkist_school_level", 0.0, -1024F, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> EVERGREEN_SCHOOL_LEVEL = ATTRIBUTES.register("evergreen_school_level", () ->
            new RangedAttribute("attribute.name.generic.evergreen_school_level", 0.0, -1024F, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> VOLTWEAVER_SCHOOL_LEVEL = ATTRIBUTES.register("voltweaver_school_level", () ->
            new RangedAttribute("attribute.name.generic.voltweaver_school_level", 0.0, -1024F, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> COSMIC_SCHOOL_LEVEL = ATTRIBUTES.register("cosmic_school_level", () ->
            new RangedAttribute("attribute.name.generic.cosmic_school_level", 0.0, -1024F, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> CELESTIA_SCHOOL_LEVEL = ATTRIBUTES.register("celestia_school_level", () ->
            new RangedAttribute("attribute.name.generic.celestia_school_level", 0.0, -1024F, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> UMBRANCY_SCHOOL_LEVEL = ATTRIBUTES.register("umbrancy_school_level", () ->
            new RangedAttribute("attribute.name.generic.umbrancy_school_level", 0.0, -1024F, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> EXODIC_SCHOOL_LEVEL = ATTRIBUTES.register("exodic_school_level", () ->
            new RangedAttribute("attribute.name.generic.exodic_school_level", 0.0, -1024F, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> BRIMWIELDER_SCHOOL_LEVEL = ATTRIBUTES.register("brimwielder_school_level", () ->
            new RangedAttribute("attribute.name.generic.brimwielder_school_level", 0.0, -1024F, 1024F).setSyncable(true));
    public static final RegistryObject<Attribute> VOIDWALKER_SCHOOL_LEVEL = ATTRIBUTES.register("voidwalker_school_level", () ->
            new RangedAttribute("attribute.name.generic.voidwalker_school_level", 0.0, -1024F, 1024F).setSyncable(true));

    /** Global multiplier for magic/spell damage. */
    public static final RegistryObject<Attribute> SPELL_DAMAGE = ATTRIBUTES.register("spell_damage",
            () -> new RangedAttribute("attribute.name.generic.spell_damage", 1, -1024F, 1024F).setSyncable(true));

    /** A percentage chance to trigger a Critical Strike. Renders as percentage via {@link RangedPercentageAttribute}. */
    public static final RegistryObject<Attribute> CRITICAL_STRIKE_CHANCE = ATTRIBUTES.register("critical_strike_chance",
            () -> new RangedPercentageAttribute("attribute.name.generic.critical_strike_chance", 0, 0, 1024F).setSyncable(true));

    /** Multiplier applied to damage when a Critical Strike occurs. (Default: 2x). */
    public static final RegistryObject<Attribute> CRITICAL_STRIKE_DAMAGE = ATTRIBUTES.register("critical_strike_damage",
            () -> new RangedPercentageAttribute("attribute.name.generic.critical_strike_damage", 2, -1024F, 1024F).setSyncable(true));

    /** Ratio of melee damage returned as health. Renders as percentage via {@link RangedPercentageAttribute}. */
    public static final RegistryObject<Attribute> LIFESTEAL = ATTRIBUTES.register("lifesteal",
            () -> new RangedPercentageAttribute("attribute.name.generic.lifesteal", 0, 0, 1024F).setSyncable(true));

    /** Ratio of all damage types returned as health. Renders as percentage via {@link RangedPercentageAttribute}. */
    public static final RegistryObject<Attribute> OMNIVAMP = ATTRIBUTES.register("omnivamp",
            () -> new RangedPercentageAttribute("attribute.name.generic.omnivamp", 0, 0, 1024F).setSyncable(true));

    /** Percentage chance to evade an incoming attack. Range: 0.0 to 1.0. */
    public static final RegistryObject<Attribute> DODGE_CHANCE = ATTRIBUTES.register("dodge_chance",
            () -> new RangedPercentageAttribute("attribute.name.generic.dodge_chance", 0, 0, 1).setSyncable(true));

    /** Vertical velocity modifier for jumping. Default vanilla value is 1.2522. */
    public static final RegistryObject<Attribute> JUMP_HEIGHT = ATTRIBUTES.register("jump_height",
            () -> new RangedAttribute("attribute.name.generic.jump_height", 1.2522, 0, 1024F).setSyncable(true));

    /** Percentage chance to stun an entity on hit. */
    public static final RegistryObject<Attribute> STUN_CHANCE = ATTRIBUTES.register("stun_chance",
            () -> new RangedPercentageAttribute("attribute.name.generic.stun_chance", 0, 0, 1F).setSyncable(true));

    /** Fixed duration (in ticks) for the Stun effect. */
    public static final RegistryObject<Attribute> STUN_DURATION = ATTRIBUTES.register("stun_duration",
            () -> new RangedAttribute("attribute.name.generic.stun_duration", 2, 0, 1024F).setSyncable(true));

    /** Percentage reduction in the duration of incoming negative status effects. */
    public static final RegistryObject<Attribute> TENACITY = ATTRIBUTES.register("tenacity",
            () -> new RangedPercentageAttribute("attribute.name.generic.tenacity", 0.15, 0, 1F).setSyncable(true));

    /**
     * Represents the "Blunt Damage" attribute, which defines the amount of bonus damage
     * dealt by entities when using blunt weapons or melee attacks associated with blunt force.
     *
     * <p>This attribute is registered under the game's attribute system and is synchronized
     * across the server and client.*/
    public static final RegistryObject<Attribute> BLUNT_DAMAGE = ATTRIBUTES.register("blunt_damage", () -> new RangedAttribute("attribute.name.generic.blunt_damage", 0, 0, 1024F).setSyncable(true));
    /**
     * Represents a custom attribute for dealing piercing damage in the Brutality mod.
     * <p>
     * This attribute determines the magnitude of piercing damage that an entity can deal.
     * Piercing damage bypasses some resistances and is particularly effective in certain combat scenarios.
     * The attribute is defined with a range*/
    public static final RegistryObject<Attribute> PIERCING_DAMAGE = ATTRIBUTES.register("piercing_damage", () -> new RangedAttribute("attribute.name.generic.piercing_damage", 0, 0, 1024F).setSyncable(true));
    /**
     * Represents a custom attribute registered as "slash_damage".
     * This attribute is used to define the damage output for slashing attacks in the game.
     *
     * <p>The attribute has a default value of 0 and ranges from 0 to a maximum of 1024.
     * It is marked as sync*/
    public static final RegistryObject<Attribute> SLASH_DAMAGE = ATTRIBUTES.register("slash_damage", () -> new RangedAttribute("attribute.name.generic.slash_damage", 0, 0, 1024F).setSyncable(true));
    /**
     *
     */
    public static final RegistryObject<Attribute> AXE_DAMAGE = ATTRIBUTES.register("axe_damage", () -> new RangedAttribute("attribute.name.generic.axe_damage", 0, 0, 1024F).setSyncable(true));
    /**
     * Represents a custom-defined attribute for sword damage in the game.
     * This attribute is used to determine the damage dealt by swords during gameplay.
     * <p>
     * Ranges from 0 to 1024, with a base value of 0. The attribute is set to be
     **/
    public static final RegistryObject<Attribute> SWORD_DAMAGE = ATTRIBUTES.register("sword_damage", () -> new RangedAttribute("attribute.name.generic.sword_damage", 0, 0, 1024F).setSyncable(true));
    /**
     * Represents a custom attribute for hammer damage within the game.
     * <p>
     * This attribute is a {@link RangedAttribute} designed to handle the damage output of hammers,
     * with a minimum value of 0 and a maximum value of 1024. It is syncable for use in multiplayer environments.
     * <p>
     */
    public static final RegistryObject<Attribute> HAMMER_DAMAGE = ATTRIBUTES.register("hammer_damage", () -> new RangedAttribute("attribute.name.generic.hammer_damage", 0, 0, 1024F).setSyncable(true));
    /**
     * Represents a custom attribute that modifies the damage dealt by spears.
     * This attribute is registered under the "spear_damage" key and is designed to have a range
     * of values from 0 to a maximum of 1024. The value is synchronized between the server and client,
     * allowing consistent gameplay experiences in multiplayer environments.
     */
    public static final RegistryObject<Attribute> SPEAR_DAMAGE = ATTRIBUTES.register("spear_damage", () -> new RangedAttribute("attribute.name.generic.spear_damage", 0, 0, 1024F).setSyncable(true));

    /**
     * Represents a scythe damage attribute, which defines the damage dealt by weapons
     * or entities related to scythes in the game. This attribute is synchronized
     * between client and server to ensure consistent behavior across the network.
     * The attribute has a default value of 0*/
    public static final RegistryObject<Attribute> SCYTHE_DAMAGE = ATTRIBUTES.register("scythe_damage", () -> new RangedAttribute("attribute.name.generic.scythe_damage", 0, 0, 1024F).setSyncable(true));

    /** Flat armor value negation. Applied after Armor Penetration. */
    public static final RegistryObject<Attribute> LETHALITY = ATTRIBUTES.register("lethality",
            () -> new RangedAttribute("attribute.name.generic.lethality", 0, 0, 1024F).setSyncable(true));

    /** Percentage of total armor ignored when attacking. Calculated before Lethality. */
    public static final RegistryObject<Attribute> ARMOR_PENETRATION = ATTRIBUTES.register("armor_penetration",
            () -> new RangedPercentageAttribute("attribute.name.generic.armor_penetration", 0, 0, 1).setSyncable(true));

    /** Percentage reduction in detection radius for hostile mobs. Range: 0.0 to 1.0. */
    public static final RegistryObject<Attribute> STEALTH = ATTRIBUTES.register("stealth",
            () -> new RangedPercentageAttribute("attribute.name.generic.stealth", 0, 0, 1).setSyncable(true));

    /** Velocity multiplier for thrown items. */
    public static final RegistryObject<Attribute> THROW_STRENGTH = ATTRIBUTES.register("throw_strength",
            () -> new RangedAttribute("attribute.name.generic.throw_strength", 1, 0, 1024F).setSyncable(true));

    /** Global multiplier for all incoming damage. (e.g., 1.0 = 100% damage). */
    public static final RegistryObject<Attribute> DAMAGE_TAKEN = ATTRIBUTES.register("damage_taken",
            () -> new RangedPercentageAttribute("attribute.name.generic.damage_taken", 1, 0, 1024F).setSyncable(true));


    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    /**
     * A specialized {@link RangedAttribute} intended to be rendered as a percentage in tooltips.
     * <p>
     * These attributes are intercepted via Mixin in both vanilla and Curios tooltip generation
     * to treat Addition operations as if they were Multiply Base operations for display purposes.
     * </p>
     */
    public static class RangedPercentageAttribute extends RangedAttribute {
        public RangedPercentageAttribute(String descriptionId, double defaultValue, double min, double max) {
            super(descriptionId, defaultValue, min, max);
        }
    }
}