package net.goo.armament.util;

import net.goo.armament.Armament;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public interface ModResources {
    int MAX_LIGHT = 15728880;

    UUID BASE_ENTITY_INTERACTION_RANGE_UUID = UUID.fromString("0CB612AF-CE7C-4FD2-9647-4BFD75B8D8A0");
    UUID BASE_BLOCK_INTERACTION_RANGE_UUID = UUID.fromString("E7902C57-6C37-41CB-BBC4-F23AB1F287C0");


    UUID BASE_ARMOR_UUID = UUID.fromString("FFB9296F-A5B3-4215-8196-4091D31575C6");
    UUID BASE_ARMOR_TOUGHNESS_UUID = UUID.fromString("4A87FDB4-7CCC-471D-96C4-03AC8515E62F");
    UUID BASE_KNOCKBACK_RESISTANCE_UUID = UUID.fromString("0F9ECB41-87F5-4787-8731-0A6C693B77F3");

    ResourceLocation FANTASY = new ResourceLocation(Armament.MOD_ID, "alagard"); // Font used: Alagard https://www.dafont.com/alagard.font
    ResourceLocation SILLY = new ResourceLocation(Armament.MOD_ID, "daydream"); // Font used: Daydream https://www.dafont.com/daydream-3.font
    ResourceLocation TECHNOLOGY = new ResourceLocation(Armament.MOD_ID, "b2bsporty"); // Font used: b2bSporty https://www.dafont.com/born2bsporty-fs.font
    ResourceLocation SPACE = new ResourceLocation(Armament.MOD_ID, "cosmicalien"); // Font used: Cosmic Alien https://www.dafont.com/cosmic-alien.font?

    ResourceLocation RARITY = new ResourceLocation(Armament.MOD_ID, "gamer"); // Font used: Gamer https://www.dafont.com/gamer-2.font

    // Colors
    int[][] RHITTA_COLORS = new int[][] {{255, 253, 112}, {86, 73, 191}};
    int[][] DOOMFIST_GAUNTLET_COLORS = new int[][]{{237, 205, 140}, {118, 118, 118}};
    int[][] EVENT_HORIZON_COLORS = new int[][]{{250, 140, 20}, {50, 50, 50}};
    int[][] FALLEN_SCYTHE_COLORS = new int[][] {{60, 40, 40}, {25, 250, 250},{60, 40, 40}};
    int[][] JACKPOT_COLORS = new int[][] {{0, 255, 255}, {192, 0, 15}, {225, 255, 8}, {0, 237, 36}};
    int[][] LEAF_BLOWER_COLORS = new int[][] {{212, 6, 6}, {255, 255, 255}};
    int[][] QUANTUM_DRILL_COLORS = new int[][] {{65, 0, 125}, {25, 25, 25}};
    int[][] RESONANCE_PICKAXE_COLORS = new int[][] {{65, 0, 125}, {25, 25, 25}};
    int[][] SHADOWSTEP_COLORS = new int[][] {{65, 0, 125}, {50, 50, 50}};
    int[][] SUPERNOVA_COLORS = new int[][] {{255, 255, 222}, {90, 37, 131}};
    int[][] TERRA_BLADE_COLORS = new int[][]{{174, 229, 58}, {0, 82, 60}};
    int[][] TERRATON_HAMMER_COLORS = new int[][]{{186, 198, 195}, {25, 50, 50}};
    int[][] THUNDERBOLT_COLORS = new int[][]{{255, 215, 86}, {164, 92, 0}};
    int[][] TRUTHSEEKER_COLORS = new int[][] {{128, 244, 58}, {99, 33, 0}};
}
