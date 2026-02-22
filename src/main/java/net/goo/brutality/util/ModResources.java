package net.goo.brutality.util;

import com.google.common.base.Suppliers;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import net.goo.brutality.Brutality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

import java.util.function.Supplier;

public interface ModResources {
    // Fonts
    ResourceLocation ALAGARD = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "alagard"); // Font used: Alagard https://www.dafont.com/alagard.font
    ResourceLocation ALAGARD_LARGE = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "alagard_large"); // Font used: Alagard https://www.dafont.com/alagard.font
    ResourceLocation DAYDREAM = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "daydream"); // Font used: Daydream https://www.dafont.com/daydream-3.font
    ResourceLocation B2BSPORTY = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "b2bsporty"); // Font used: b2bSporty https://www.dafont.com/born2bsporty-fs.font
    ResourceLocation COSMICALIEN = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "cosmicalien"); // Font used: Cosmic Alien https://www.dafont.com/cosmic-alien.font?
    ResourceLocation RARITY_FONT = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "gamer"); // Font used: Gamer https://www.dafont.com/gamer-2.font
    ResourceLocation STAT_TRAK_FONT = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "gcp_matrix"); // Font used: gcp-matrix https://fontstruct.com/fontstructions/show/2268622/gcp-matrix
    ResourceLocation TABLE_OF_WIZARDRY_FONT = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "table_of_wizardry");
    ResourceLocation ICON_FONT = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "icons");
    ResourceLocation SMALL_CAPS = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "small_caps");


    Supplier<FX> CREASE_OF_CREATION_FX = Suppliers.memoize( () -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "crease_of_creation_particle")));
    Supplier<FX> PHOTON_TRAIL_FX = Suppliers.memoize( () -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "photon_trail")));
    Supplier<FX> VAMPIRE_TRIAL_FX = Suppliers.memoize( () -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "vampire_trail")));
    Supplier<FX> CELESTIAL_STARBOARD_FX = Suppliers.memoize( () -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "celestial_starboard_trail")));
    Supplier<FX> LIGHTNING_STRIKE_BURST_FX = Suppliers.memoize( () -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "lightning_strike_burst")));
    Supplier<FX> LIGHTNING_AURA_FX = Suppliers.memoize( () -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "lightning_aura")));
    Supplier<FX> LIGHTNING_TRAIL_FX = Suppliers.memoize( () -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "lightning_trail")));
    Supplier<FX> GRAVITY_FIELD_UP_FX = Suppliers.memoize( () -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "gravity_field_up")));
    Supplier<FX> GRAVITY_FIELD_DOWN_FX = Suppliers.memoize( () -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "gravity_field_down")));
    Supplier<FX> RAINBOW_TRAIL_FX = Suppliers.memoize( () -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "rainbow_trail")));
    Supplier<FX> ABYSS_TRAIL_FX = Suppliers.memoize( () -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "abyss_trail")));
    Supplier<FX> RUINED_AURA_FX = Suppliers.memoize( () -> FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "ruined_particle")));

    int[] rainbowColor = new int[]{
            FastColor.ARGB32.color(255, 255, 0, 0),
            FastColor.ARGB32.color(255, 255, 127, 0),
            FastColor.ARGB32.color(255, 255, 255, 0),
            FastColor.ARGB32.color(255, 0, 255, 0),
            FastColor.ARGB32.color(255, 0, 0, 255),
            FastColor.ARGB32.color(255, 75, 0, 130),
            FastColor.ARGB32.color(255, 148, 0, 21)
    };
}
