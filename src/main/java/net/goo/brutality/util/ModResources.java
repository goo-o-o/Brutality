package net.goo.brutality.util;

import net.goo.brutality.Brutality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public interface ModResources {
    // Fonts
    ResourceLocation ALAGARD = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "alagard"); // Font used: Alagard https://www.dafont.com/alagard.font
    ResourceLocation ALAGARD_LARGE = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "alagard_large"); // Font used: Alagard https://www.dafont.com/alagard.font
    ResourceLocation DAYDREAM = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "daydream"); // Font used: Daydream https://www.dafont.com/daydream-3.font
    ResourceLocation B2BSPORTY = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "b2bsporty"); // Font used: b2bSporty https://www.dafont.com/born2bsporty-fs.font
    ResourceLocation COSMICALIEN = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "cosmicalien"); // Font used: Cosmic Alien https://www.dafont.com/cosmic-alien.font?
    ResourceLocation RARITY_FONT = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "gamer"); // Font used: Gamer https://www.dafont.com/gamer-2.font
    ResourceLocation STAT_TRAK_FONT = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "gcp_matrix"); // Font used: gcp-matrix https://fontstruct.com/fontstructions/show/2268622/gcp-matrix
    ResourceLocation TABLE_OF_WIZARDRY_FONT = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "small");
    ResourceLocation ICON_FONT = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "icons");


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