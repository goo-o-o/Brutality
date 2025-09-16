package net.goo.brutality.magic;

import net.goo.brutality.Brutality;
import net.goo.brutality.magic.spells.brimwielder.*;
import net.goo.brutality.magic.spells.celestia.*;
import net.goo.brutality.magic.spells.cosmic.*;
import net.goo.brutality.magic.spells.umbrancy.*;
import net.goo.brutality.magic.spells.voidwalker.*;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SpellRegistry {
    public static final Map<ResourceLocation, IBrutalitySpell> SPELLS = new HashMap<>();

    private static void register(IBrutalitySpell spell) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, spell.getSpellName().toLowerCase(Locale.ROOT));
        SPELLS.put(id, spell);
    }

    public static IBrutalitySpell getSpell(ResourceLocation id) {
        return SPELLS.get(id);
    }

    public static void register() {
        register(new VoidWalkSpell());
        register(new GraviticImplosionSpell());
        register(new SpatialRuptureSpell());
        register(new IntangibleSpell());
        register(new DematerializeSpell());

        register(new HeavenlyFlightSpell());
        register(new DivineRetributionSpell());
        register(new HolyMantleSpell());
        register(new SacrificeSpell());
        register(new LightBindingSpell());

        register(new MeteorShowerSpell());
        register(new GravitokinesisSpell());
        register(new CosmicCataclysmSpell());
        register(new StarStreamSpell());
        register(new StarBurstSpell());
        register(new SingularityShiftSpell());

        register(new AnnihilationSpell());
        register(new ExtinctionSpell());
        register(new BrimspikeSpell());
        register(new StygianStepSpell());
        register(new ChthonicCapsuleSpell());

        register(new NightfallSpell());
        register(new MoonlitMendingSpell());
        register(new CrescentScytheSpell());
        register(new PiercingMoonlightSpell());
        register(new CrescentDartSpell());

    }
}