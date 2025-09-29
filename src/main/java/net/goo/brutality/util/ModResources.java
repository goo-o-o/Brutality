package net.goo.brutality.util;

import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import net.goo.brutality.Brutality;
import net.minecraft.resources.ResourceLocation;

public interface ModResources {
    // Fonts
    ResourceLocation ALAGARD = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "alagard"); // Font used: Alagard https://www.dafont.com/alagard.font
    ResourceLocation ALAGARD_LARGE = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "alagard_large"); // Font used: Alagard https://www.dafont.com/alagard.font
    ResourceLocation DAYDREAM = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "daydream"); // Font used: Daydream https://www.dafont.com/daydream-3.font
    ResourceLocation B2BSPORTY = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "b2bsporty"); // Font used: b2bSporty https://www.dafont.com/born2bsporty-fs.font
    ResourceLocation COSMICALIEN = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "cosmicalien"); // Font used: Cosmic Alien https://www.dafont.com/cosmic-alien.font?
    ResourceLocation RARITY_FONT = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "gamer"); // Font used: Gamer https://www.dafont.com/gamer-2.font

    FX CREASE_OF_CREATION_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "crease_of_creation_particle"));
    FX VAMPIRE_TRIAL_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "vampire_trail"));
    FX CELESTIAL_STARBOARD_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "celestial_starboard_trail"));
    FX LIGHTNING_STRIKE_BURST_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "lightning_strike_burst"));
    FX LIGHTNING_AURA_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "lightning_aura"));
    FX LIGHTNING_TRAIL_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "lightning_trail"));
    FX GRAVITY_FIELD_UP_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "gravity_field_up"));
    FX GRAVITY_FIELD_DOWN_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "gravity_field_down"));
    FX RAINBOW_TRAIL_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "rainbow_trail"));
    FX ABYSS_TRAIL_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "abyss_trail"));
    FX RUINED_AURA_FX = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "ruined_particle"));

}
