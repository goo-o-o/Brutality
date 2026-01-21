package net.goo.brutality.magic;

import net.goo.brutality.Brutality;
import net.goo.brutality.magic.spells.brimwielder.*;
import net.goo.brutality.magic.spells.celestia.*;
import net.goo.brutality.magic.spells.cosmic.*;
import net.goo.brutality.magic.spells.umbrancy.*;
import net.goo.brutality.magic.spells.voidwalker.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class BrutalityModSpells {
    public static final DeferredRegister<IBrutalitySpell> SPELLS =
            DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "spells"), Brutality.MOD_ID);

    // --- Void / Dark Spells ---
    public static final RegistryObject<IBrutalitySpell> VOID_WALK =
            SPELLS.register("void_walk", VoidWalkSpell::new);
    public static final RegistryObject<IBrutalitySpell> GRAVITIC_IMPLOSION =
            SPELLS.register("gravitic_implosion", GraviticImplosionSpell::new);
    public static final RegistryObject<IBrutalitySpell> SPATIAL_RUPTURE =
            SPELLS.register("spatial_rupture", SpatialRuptureSpell::new);
    public static final RegistryObject<IBrutalitySpell> INTANGIBLE =
            SPELLS.register("intangible", IntangibleSpell::new);
    public static final RegistryObject<IBrutalitySpell> DEMATERIALIZE =
            SPELLS.register("dematerialize", DematerializeSpell::new);

    // --- Holy / Light Spells ---
    public static final RegistryObject<IBrutalitySpell> HEAVENLY_FLIGHT =
            SPELLS.register("heavenly_flight", HeavenlyFlightSpell::new);
    public static final RegistryObject<IBrutalitySpell> DIVINE_RETRIBUTION =
            SPELLS.register("divine_retribution", DivineRetributionSpell::new);
    public static final RegistryObject<IBrutalitySpell> HOLY_MANTLE =
            SPELLS.register("holy_mantle", HolyMantleSpell::new);
    public static final RegistryObject<IBrutalitySpell> SACRIFICE =
            SPELLS.register("sacrifice", SacrificeSpell::new);
    public static final RegistryObject<IBrutalitySpell> LIGHT_BINDING =
            SPELLS.register("light_binding", LightBindingSpell::new);

    // --- Cosmic Spells ---
    public static final RegistryObject<IBrutalitySpell> METEOR_SHOWER =
            SPELLS.register("meteor_shower", MeteorShowerSpell::new);
    public static final RegistryObject<IBrutalitySpell> GRAVITOKINESIS =
            SPELLS.register("gravitokinesis", GravitokinesisSpell::new);
    public static final RegistryObject<IBrutalitySpell> COSMIC_CATACLYSM =
            SPELLS.register("cosmic_cataclysm", CosmicCataclysmSpell::new);
    public static final RegistryObject<IBrutalitySpell> STAR_STREAM =
            SPELLS.register("star_stream", StarStreamSpell::new);
    public static final RegistryObject<IBrutalitySpell> STAR_BURST =
            SPELLS.register("star_burst", StarBurstSpell::new);
    public static final RegistryObject<IBrutalitySpell> SINGULARITY_SHIFT =
            SPELLS.register("singularity_shift", SingularityShiftSpell::new);

    // --- Infernal / Nether Spells ---
    public static final RegistryObject<IBrutalitySpell> ANNIHILATION =
            SPELLS.register("annihilation", AnnihilationSpell::new);
    public static final RegistryObject<IBrutalitySpell> EXTINCTION =
            SPELLS.register("extinction", ExtinctionSpell::new);
    public static final RegistryObject<IBrutalitySpell> BRIMSPIKE =
            SPELLS.register("brimspike", BrimspikeSpell::new);
    public static final RegistryObject<IBrutalitySpell> STYGIAN_STEP =
            SPELLS.register("stygian_step", StygianStepSpell::new);
    public static final RegistryObject<IBrutalitySpell> CHTHONIC_CAPSULE =
            SPELLS.register("chthonic_capsule", ChthonicCapsuleSpell::new);

    public static final RegistryObject<IBrutalitySpell> NIGHTFALL =
            SPELLS.register("nightfall", NightfallSpell::new);
    public static final RegistryObject<IBrutalitySpell> MOONLIT_MENDING =
            SPELLS.register("moonlit_mending", MoonlitMendingSpell::new);
    public static final RegistryObject<IBrutalitySpell> CRESCENT_SCYTHE =
            SPELLS.register("crescent_scythe", CrescentScytheSpell::new);
    public static final RegistryObject<IBrutalitySpell> PIERCING_MOONLIGHT =
            SPELLS.register("piercing_moonlight", PiercingMoonlightSpell::new);
    public static final RegistryObject<IBrutalitySpell> CRESCENT_DART =
            SPELLS.register("crescent_dart", CrescentDartSpell::new);



    public static final ResourceKey<Registry<IBrutalitySpell>> SPELL_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "spells"));

    public static void register(IEventBus eventBus) {
        eventBus.addListener(BrutalityModSpells::createRegistry);
        SPELLS.register(eventBus);
    }

    private static void createRegistry(NewRegistryEvent event) {
        event.create(new RegistryBuilder<IBrutalitySpell>()
                .setName(SPELL_REGISTRY_KEY.location())
        );
    }
    // --- Modern Helper Methods ---

    public static IBrutalitySpell getSpell(ResourceLocation id) {
        return SPELLS.getEntries().stream()
                .filter(ro -> ro.getId() != null && ro.getId().equals(id))
                .map(RegistryObject::get)
                .findFirst()
                .orElse(null);
    }

    public static List<IBrutalitySpell> getSpellsFromSchool(IBrutalitySpell.MagicSchool school) {
        return SPELLS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(spell -> spell.getSchool() == school)
                .toList();
    }
}