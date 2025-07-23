package net.goo.brutality.magic;

import net.goo.brutality.Brutality;
import net.goo.brutality.magic.spells.daemonium.DaemonicPickaxeSpell;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SpellRegistry {
    private static final Map<ResourceLocation, IBrutalitySpell> SPELLS = new HashMap<>();

    public static void register(IBrutalitySpell spell) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, spell.getSpellName().toLowerCase(Locale.ROOT));
        SPELLS.put(id, spell);
    }

    public static IBrutalitySpell getSpell(ResourceLocation id) {
        return SPELLS.get(id);
    }

    public static Collection<IBrutalitySpell> getAllSpells() {
        return SPELLS.values();
    }

    public static void register() {
        register(new DaemonicPickaxeSpell());
    }
}