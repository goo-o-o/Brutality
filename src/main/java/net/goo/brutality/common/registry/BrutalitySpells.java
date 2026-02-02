package net.goo.brutality.common.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.magic.spells.brimwielder.*;
import net.goo.brutality.common.magic.spells.celestia.*;
import net.goo.brutality.common.magic.spells.cosmic.*;
import net.goo.brutality.common.magic.spells.umbrancy.*;
import net.goo.brutality.common.magic.spells.voidwalker.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

import java.util.List;

public class BrutalitySpells {
    public static final DeferredRegister<BrutalitySpell> SPELLS =
            DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "spells"), Brutality.MOD_ID);

    // --- Void / Dark Spells ---
    public static final RegistryObject<BrutalitySpell> VOID_WALK =
            SPELLS.register("void_walk", VoidWalkSpell::new);
    public static final RegistryObject<BrutalitySpell> GRAVITIC_IMPLOSION =
            SPELLS.register("gravitic_implosion", GraviticImplosionSpell::new);
    public static final RegistryObject<BrutalitySpell> SPATIAL_RUPTURE =
            SPELLS.register("spatial_rupture", SpatialRuptureSpell::new);
    public static final RegistryObject<BrutalitySpell> INTANGIBLE =
            SPELLS.register("intangible", IntangibleSpell::new);
    public static final RegistryObject<BrutalitySpell> DEMATERIALIZE =
            SPELLS.register("dematerialize", DematerializeSpell::new);

    // --- Holy / Light Spells ---
    public static final RegistryObject<BrutalitySpell> HEAVENLY_FLIGHT =
            SPELLS.register("heavenly_flight", HeavenlyFlightSpell::new);
    public static final RegistryObject<BrutalitySpell> DIVINE_RETRIBUTION =
            SPELLS.register("divine_retribution", DivineRetributionSpell::new);
    public static final RegistryObject<BrutalitySpell> HOLY_MANTLE =
            SPELLS.register("holy_mantle", HolyMantleSpell::new);
    public static final RegistryObject<BrutalitySpell> SACRIFICE =
            SPELLS.register("sacrifice", SacrificeSpell::new);
    public static final RegistryObject<BrutalitySpell> LIGHT_BINDING =
            SPELLS.register("light_binding", LightBindingSpell::new);

    // --- Cosmic Spells ---
    public static final RegistryObject<BrutalitySpell> METEOR_SHOWER =
            SPELLS.register("meteor_shower", MeteorShowerSpell::new);
    public static final RegistryObject<BrutalitySpell> GRAVITOKINESIS =
            SPELLS.register("gravitokinesis", GravitokinesisSpell::new);
    public static final RegistryObject<BrutalitySpell> COSMIC_CATACLYSM =
            SPELLS.register("cosmic_cataclysm", CosmicCataclysmSpell::new);
    public static final RegistryObject<BrutalitySpell> STAR_STREAM =
            SPELLS.register("star_stream", StarStreamSpell::new);
    public static final RegistryObject<BrutalitySpell> STAR_BURST =
            SPELLS.register("star_burst", StarBurstSpell::new);
    public static final RegistryObject<BrutalitySpell> SINGULARITY_SHIFT =
            SPELLS.register("singularity_shift", SingularityShiftSpell::new);

    // --- Infernal / Nether Spells ---
    public static final RegistryObject<BrutalitySpell> ANNIHILATION =
            SPELLS.register("annihilation", AnnihilationSpell::new);
    public static final RegistryObject<BrutalitySpell> EXTINCTION =
            SPELLS.register("extinction", ExtinctionSpell::new);
    public static final RegistryObject<BrutalitySpell> BRIMSPIKE =
            SPELLS.register("brimspike", BrimspikeSpell::new);
    public static final RegistryObject<BrutalitySpell> STYGIAN_STEP =
            SPELLS.register("stygian_step", StygianStepSpell::new);
    public static final RegistryObject<BrutalitySpell> CHTHONIC_CAPSULE =
            SPELLS.register("chthonic_capsule", ChthonicCapsuleSpell::new);

    public static final RegistryObject<BrutalitySpell> NIGHTFALL =
            SPELLS.register("nightfall", NightfallSpell::new);
    public static final RegistryObject<BrutalitySpell> MOONLIT_MENDING =
            SPELLS.register("moonlit_mending", MoonlitMendingSpell::new);
    public static final RegistryObject<BrutalitySpell> CRESCENT_SCYTHE =
            SPELLS.register("crescent_scythe", CrescentScytheSpell::new);
    public static final RegistryObject<BrutalitySpell> PIERCING_MOONLIGHT =
            SPELLS.register("piercing_moonlight", PiercingMoonlightSpell::new);
    public static final RegistryObject<BrutalitySpell> CRESCENT_DART =
            SPELLS.register("crescent_dart", CrescentDartSpell::new);



    public static final ResourceKey<Registry<BrutalitySpell>> SPELL_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "spells"));

    public static void register(IEventBus eventBus) {
        eventBus.addListener(BrutalitySpells::createRegistry);
        SPELLS.register(eventBus);
    }

    private static void createRegistry(NewRegistryEvent event) {
        event.create(new RegistryBuilder<BrutalitySpell>()
                .setName(SPELL_REGISTRY_KEY.location())
        );
    }
    // --- Modern Helper Methods ---

    public static BrutalitySpell getSpell(ResourceLocation id) {
        return SPELLS.getEntries().stream()
                .filter(ro -> ro.getId() != null && ro.getId().equals(id))
                .map(RegistryObject::get)
                .findFirst()
                .orElse(null);
    }

    public static List<BrutalitySpell> getSpellsFromSchool(BrutalitySpell.MagicSchool school) {
        return SPELLS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(spell -> spell.getSchool() == school)
                .toList();
    }

    public static ResourceLocation getIdFromSpell(BrutalitySpell currentSpell) {
        var registry = net.minecraftforge.registries.RegistryManager.ACTIVE.getRegistry(SPELL_REGISTRY_KEY.location());

        if (registry != null) {
            return registry.getKey(currentSpell);
        }

        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "empty");
    }
}