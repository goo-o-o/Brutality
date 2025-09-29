package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class BrutalityModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Brutality.MOD_ID);
    public static final RegistryObject<SoundEvent> METAL_PIPE = registerSoundEvents("metal_pipe");
    public static final RegistryObject<SoundEvent> TREASURE_CHEST_LOCK = registerSoundEvents("treasure_chest_lock");
    public static final RegistryObject<SoundEvent> LIGHT_SWITCH = registerSoundEvents("light_switch");
    public static final RegistryObject<SoundEvent> EMERGENCY_MEETING = registerSoundEvents("emergency_meeting");
    public static final RegistryObject<SoundEvent> THROW = registerSoundEvents("throw");
    public static final RegistryObject<SoundEvent> ZAP = registerSoundEvents("zap");

    public static final RegistryObject<SoundEvent> LEAF_BLOWER_ON = registerSoundEvents("leaf_blower_on");
    public static final RegistryObject<SoundEvent> LEAF_BLOWER_ACTIVE = registerSoundEvents("leaf_blower_active");
    public static final RegistryObject<SoundEvent> LEAF_BLOWER_OFF = registerSoundEvents("leaf_blower_off");

    public static final List<RegistryObject<SoundEvent>> JACKPOT_SOUNDS = List.of(
            registerSoundEvents("jackpot_sound_1"),
            registerSoundEvents("jackpot_sound_2"),
            registerSoundEvents("jackpot_sound_3"),
            registerSoundEvents("jackpot_sound_4")
    );

    public static final List<RegistryObject<SoundEvent>> CRATE_BREAK_SOUNDS = List.of(
            registerSoundEvents("crate_break_1"),
            registerSoundEvents("crate_break_2"),
            registerSoundEvents("crate_break_3"),
            registerSoundEvents("crate_break_4")
    );
    public static final List<RegistryObject<SoundEvent>> DODGE_SOUNDS = List.of(
            registerSoundEvents("dodge_1"),
            registerSoundEvents("dodge_2"),
            registerSoundEvents("dodge_3"),
            registerSoundEvents("dodge_4")
    );

    public static final List<RegistryObject<SoundEvent>> MURASAMA = List.of(
            registerSoundEvents("murasama_1"),
            registerSoundEvents("murasama_2"),
            registerSoundEvents("murasama_3")
    );

    public static final List<RegistryObject<SoundEvent>> SUPERNOVA = List.of(
            registerSoundEvents("supernova_1"),
            registerSoundEvents("supernova_2"),
            registerSoundEvents("supernova_3")
    );

    public static final List<RegistryObject<SoundEvent>> SPATULA = List.of(
            registerSoundEvents("spatula_flip_1"),
            registerSoundEvents("spatula_flip_2"),
            registerSoundEvents("spatula_flip_3"),
            registerSoundEvents("spatula_flip_4"),
            registerSoundEvents("spatula_flip_5")
    );

    public static final List<RegistryObject<SoundEvent>> PUNCHES = List.of(
            registerSoundEvents("epic_punch_1"),
            registerSoundEvents("epic_punch_2")
    );

    public static final List<RegistryObject<SoundEvent>> EXOBLADE = List.of(
            registerSoundEvents("exoblade_swing_1"),
            registerSoundEvents("exoblade_swing_2"),
            registerSoundEvents("exoblade_swing_3")
    );

    public static final List<RegistryObject<SoundEvent>> UMBRAL_DASH = List.of(
            registerSoundEvents("umbral_dash_1"),
            registerSoundEvents("umbral_dash_2")
    );

    public static final List<RegistryObject<SoundEvent>> DARKIN_BLADE = List.of(
            registerSoundEvents("darkin_blade_1"),
            registerSoundEvents("darkin_blade_2")
    );

    public static final List<RegistryObject<SoundEvent>> DARKIN_SCYTHE_TRANSFORM = List.of(
            registerSoundEvents("transform_kayn"),
            registerSoundEvents("transform_shadow_assasin"),
            registerSoundEvents("transform_rhaast")
    );

    public static final RegistryObject<SoundEvent> SHURIKEN_IMPACT = registerSoundEvents("shuriken_impact");
    public static final RegistryObject<SoundEvent> STYROFOAM_IMPACT = registerSoundEvents("styrofoam_impact");

    public static final RegistryObject<SoundEvent> BLOOD_SPLATTER = registerSoundEvents("blood_splatter");


    public static final RegistryObject<SoundEvent> DEATHBRINGER_STANCE_READY = registerSoundEvents("deathbringer_stance_ready");
    public static final RegistryObject<SoundEvent> DEATHBRINGER_STANCE_HIT = registerSoundEvents("deathbringer_stance_hit");
    public static final RegistryObject<SoundEvent> BIOMECH_REACTOR_BOOM = registerSoundEvents("biomech_reactor_boom");
    public static final RegistryObject<SoundEvent> TARGET_FOUND = registerSoundEvents("target_found");

    public static final RegistryObject<SoundEvent> GROUND_IMPACT = registerSoundEvents("ground_impact");
    public static final RegistryObject<SoundEvent> SPECTRAL_MAW = registerSoundEvents("spectral_maw");
    public static final RegistryObject<SoundEvent> LAST_PRISM_USE = registerSoundEvents("last_prism_use");

    public static final RegistryObject<SoundEvent> EVENT_HORIZON_DISLODGE = registerSoundEvents("event_horizon_dislodge");
    public static final RegistryObject<SoundEvent> EVENT_HORIZON_RETURN = registerSoundEvents("event_horizon_return");

    public static final RegistryObject<SoundEvent> TERRATOMERE_SWING = registerSoundEvents("terratomere_swing");
    public static final RegistryObject<SoundEvent> GENERIC_SLICE = registerSoundEvents("generic_slice");


    public static final RegistryObject<SoundEvent> VORTEX_EXPLOSION = registerSoundEvents("vortex_explosion");
    public static final RegistryObject<SoundEvent> SPACE_EXPLOSION = registerSoundEvents("space_explosion");
    public static final RegistryObject<SoundEvent> METEOR_CRASH = registerSoundEvents("meteor_crash");
    public static final RegistryObject<SoundEvent> LASER_BEAM = registerSoundEvents("laser_beam");


    public static final RegistryObject<SoundEvent> TOME_OPEN = registerSoundEvents("tome_open");
    public static final RegistryObject<SoundEvent> TOME_CLOSE = registerSoundEvents("tome_close");

    public static final RegistryObject<SoundEvent> PHASESABER_SWING = registerSoundEvents("phasesaber_swing");


    public static final RegistryObject<SoundEvent> KNIFE_BLOCK = registerSoundEvents("knife_block");
    public static final RegistryObject<SoundEvent> SQUELCH = registerSoundEvents("squelch");
    public static final RegistryObject<SoundEvent> FRYING_PAN_HIT = registerSoundEvents("frying_pan_hit");

    public static final RegistryObject<SoundEvent> DULL_KNIFE_SWING = registerSoundEvents("dull_knife_swing");
    public static final RegistryObject<SoundEvent> DULL_KNIFE_STAB = registerSoundEvents("dull_knife_stab");
    public static final RegistryObject<SoundEvent> DULL_KNIFE_CRIT = registerSoundEvents("dull_knife_crit");
    public static final RegistryObject<SoundEvent> DULL_KNIFE_ABILITY = registerSoundEvents("dull_knife_ability");

    public static final RegistryObject<SoundEvent> BASS_BOP = registerSoundEvents("bass_bop");

    public static final RegistryObject<SoundEvent> TERRA_BLADE_USE = registerSoundEvents("terra_blade_use");
    public static final RegistryObject<SoundEvent> ICE_WAVE = registerSoundEvents("ice_wave");

    public static final RegistryObject<SoundEvent> BASS_BOOM = registerSoundEvents("bass_boom");
    public static final RegistryObject<SoundEvent> BIG_EXPLOSION = registerSoundEvents("big_explosion");
    public static final RegistryObject<SoundEvent> BIGGER_EXPLOSION = registerSoundEvents("bigger_explosion");

    public static final RegistryObject<SoundEvent> WINGS_FLAP = registerSoundEvents("wings_flap");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}
