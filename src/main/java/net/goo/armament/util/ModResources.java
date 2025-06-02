package net.goo.armament.util;

import net.goo.armament.Armament;
import net.goo.armament.item.noir.CanopyOfShadows;
import net.goo.armament.item.noir.NoirArmorItem;
import net.goo.armament.item.noir.ShadowstepDagger;
import net.goo.armament.item.terra.TerraArmorItem;
import net.goo.armament.item.terra.TerraBladeSword;
import net.goo.armament.item.terra.TerratonHammer;
import net.goo.armament.item.base.ArmaTridentItem;
import net.goo.armament.item.weapon.custom.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface ModResources {
    // Attributes
    UUID BASE_ENTITY_INTERACTION_RANGE_UUID = UUID.fromString("0CB612AF-CE7C-4FD2-9647-4BFD75B8D8A0");
    UUID BASE_BLOCK_INTERACTION_RANGE_UUID = UUID.fromString("E7902C57-6C37-41CB-BBC4-F23AB1F287C0");
    UUID BASE_ARMOR_UUID = UUID.fromString("FFB9296F-A5B3-4215-8196-4091D31575C6");
    UUID BASE_ARMOR_TOUGHNESS_UUID = UUID.fromString("4A87FDB4-7CCC-471D-96C4-03AC8515E62F");
    UUID BASE_KNOCKBACK_RESISTANCE_UUID = UUID.fromString("0F9ECB41-87F5-4787-8731-0A6C693B77F3");

    // Fonts
    ResourceLocation ALAGARD = new ResourceLocation(Armament.MOD_ID, "alagard"); // Font used: Alagard https://www.dafont.com/alagard.font
    ResourceLocation ALAGARD_LARGE = new ResourceLocation(Armament.MOD_ID, "alagard_large"); // Font used: Alagard https://www.dafont.com/alagard.font
    ResourceLocation DAYDREAM = new ResourceLocation(Armament.MOD_ID, "daydream"); // Font used: Daydream https://www.dafont.com/daydream-3.font
    ResourceLocation B2BSPORTY = new ResourceLocation(Armament.MOD_ID, "b2bsporty"); // Font used: b2bSporty https://www.dafont.com/born2bsporty-fs.font
    ResourceLocation COSMICALIEN = new ResourceLocation(Armament.MOD_ID, "cosmicalien"); // Font used: Cosmic Alien https://www.dafont.com/cosmic-alien.font?
    ResourceLocation RARITY = new ResourceLocation(Armament.MOD_ID, "gamer"); // Font used: Gamer https://www.dafont.com/gamer-2.font

    // Colors
    int[][] ATOMIC_JUDGEMENT_COLORS = new int[][] {{10, 155, 20}, {240, 230, 15}};
    int[][] RHITTA_COLORS = new int[][] {{255, 253, 112}, {86, 73, 191}};
    int[][] DOOMFIST_GAUNTLET_COLORS = new int[][]{{237, 205, 140}, {118, 118, 118}};
    int[][] EVENT_HORIZON_COLORS = new int[][]{{250, 140, 20}, {50, 50, 50}};
    int[][] EXCALIBUR_COLORS = new int[][]{{255, 215, 86}, {255, 240, 122}};
    int[][] FALLEN_SCYTHE_COLORS = new int[][] {{60, 40, 40}, {25, 250, 250},{60, 40, 40}};
    int[][] FROSTMOURNE_COLORS = new int[][] {{97, 211, 220}, {193, 249, 252},{112, 121, 148}, {59, 65, 82}};
    int[][] GUNGNIR_COLORS = new int[][] {{171, 102, 34}, {240, 200, 129}, {234, 75, 24}};
    int[][] JACKPOT_COLORS = new int[][] {{0, 255, 255}, {192, 0, 15}, {225, 255, 8}, {0, 237, 36}};
    int[][] LEAF_BLOWER_COLORS = new int[][] {{212, 6, 6}, {255, 255, 255}};
    int[][] MURASAMA_COLORS = new int[][] {{255, 0, 0}, {150, 0, 0}};
    int[][] MURAMASA_COLORS = new int[][] {{0, 0, 255}, {0, 0, 150}};
    int[][] MAGIC_ROD_COLORS = new int[][] {{190, 115, 60}, {170, 0, 50}, {190, 115, 60}};
    int[][] PROVIDENCE_COLORS = new int[][] {{255, 254, 232}, {157, 110, 64}};
    int[][] QUANTUM_DRILL_COLORS = new int[][] {{65, 0, 125}, {25, 25, 25}};
    int[][] RESONANCE_PICKAXE_COLORS = new int[][] {{65, 0, 125}, {25, 25, 25}};
    int[][] SUPERNOVA_COLORS = new int[][] {{255, 255, 222}, {90, 37, 131}};
    int[][] TERRA_BLADE_COLORS = new int[][]{{174, 229, 58}, {0, 82, 60}};
    int[][] TERRATON_HAMMER_COLORS = new int[][]{{186, 198, 195}, {25, 50, 50}};
    int[][] THUNDERBOLT_COLORS = new int[][]{{255, 215, 86}, {164, 92, 0}};
    int[][] TRUTHSEEKER_COLORS = new int[][] {{128, 244, 58}, {99, 33, 0}};


    int[][] NOIR_COLORS = new int[][] {{50, 50, 50}, {20, 20, 20}};
    int[][] TERRA_COLORS = new int[][] {{70, 200, 75}, {170, 120, 70}};

    Map<Class<?>, int[][]> BASE_COLOR_MAP = Map.ofEntries(
//                    Map.entry(DivineRhittaAxeItem.class, RHITTA_COLORS),
            Map.entry(ArmaTridentItem.class, new int[][]{{0,0,0}, {0,0,0}}),
            Map.entry(AtomicJudgementHammer.class, ATOMIC_JUDGEMENT_COLORS),
            Map.entry(DoomfistGauntletItem.class, DOOMFIST_GAUNTLET_COLORS),
            Map.entry(EventHorizonLance.class, EVENT_HORIZON_COLORS),
            Map.entry(ExcaliburSword.class, EXCALIBUR_COLORS),
            Map.entry(FallenScythe.class, FALLEN_SCYTHE_COLORS),
            Map.entry(FrostmourneSword.class, FROSTMOURNE_COLORS),
            Map.entry(GungnirTrident.class, GUNGNIR_COLORS),
            Map.entry(JackpotHammer.class, JACKPOT_COLORS),
            Map.entry(LeafBlowerItem.class, LEAF_BLOWER_COLORS),
            Map.entry(FirstExplosionStaff.class, MAGIC_ROD_COLORS),

            Map.entry(MurasamaSword.class, MURASAMA_COLORS),
            Map.entry(MuramasaSword.class, MURAMASA_COLORS),

            Map.entry(ProvidenceBow.class, PROVIDENCE_COLORS),
            Map.entry(SupernovaSword.class, SUPERNOVA_COLORS),
            Map.entry(TruthseekerSword.class, TRUTHSEEKER_COLORS),
            Map.entry(ThunderboltTrident.class, THUNDERBOLT_COLORS),


            Map.entry(TerraBladeSword.class, TERRA_COLORS),
            Map.entry(TerratonHammer.class, TERRA_COLORS),
            Map.entry(TerraArmorItem.class, TERRA_COLORS),

            Map.entry(ShadowstepDagger.class, NOIR_COLORS),
            Map.entry(NoirArmorItem.class, NOIR_COLORS),
            Map.entry(CanopyOfShadows.class, NOIR_COLORS),
            Map.entry(DullKnifeDagger.class, NOIR_COLORS)

    );

    Map<Class<?>, int[][]> DARKENED_COLOR_CACHE = new ConcurrentHashMap<>();
    Map<Class<?>, int[][]> LORE_COLOR_CACHE = new ConcurrentHashMap<>();

    // Mod Compat
    boolean BETTER_COMBAT_LOADED = ModList.get().isLoaded("bettercombat");



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

    // Sweep particles


}
