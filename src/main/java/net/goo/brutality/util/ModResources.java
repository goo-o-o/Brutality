package net.goo.brutality.util;

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


    // Values
    float FROSTMOURNE_WAVE_RADIUS = 15, FROSTMOURNE_WAVE_DURATION = 4, FROSTMOURNE_WAVE_SPEED = 2;
    float HEAL_WAVE_RADIUS = 3, HEAL_WAVE_DURATION = 2, HEAL_WAVE_SPEED = 3;

    enum BEAM_TYPES {
        TERRA,
        EXCALIBUR
    }

    enum EXPLOSION_TYPES {
        DEFAULT,
        NUCLEAR,
        FIRE,
        HOLY
    }

    String CUSTOM_MODEL_DATA = "CustomModelData";

}
