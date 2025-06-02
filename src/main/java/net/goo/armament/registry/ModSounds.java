package net.goo.armament.registry;

import net.goo.armament.Armament;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Armament.MOD_ID);

    public static final RegistryObject<SoundEvent> LEAF_BLOWER_ON = registerSoundEvents("leaf_blower_on");
    public static final RegistryObject<SoundEvent> LEAF_BLOWER_ACTIVE = registerSoundEvents("leaf_blower_active");
    public static final RegistryObject<SoundEvent> LEAF_BLOWER_OFF = registerSoundEvents("leaf_blower_off");

    public static final List<RegistryObject<SoundEvent>> JACKPOT_SOUNDS = List.of(
            registerSoundEvents("jackpot_sound_1"),
            registerSoundEvents("jackpot_sound_2"),
            registerSoundEvents("jackpot_sound_3"),
            registerSoundEvents("jackpot_sound_4")
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

    public static final RegistryObject<SoundEvent> TERRA_BLADE_USE = registerSoundEvents("terra_blade_use");
    public static final RegistryObject<SoundEvent> ICE_WAVE = registerSoundEvents("ice_wave");
    public static final RegistryObject<SoundEvent> BIG_EXPLOSION = registerSoundEvents("big_explosion");
    public static final RegistryObject<SoundEvent> BIGGER_EXPLOSION = registerSoundEvents("bigger_explosion");
    public static final RegistryObject<SoundEvent> WINGS_FLAP = registerSoundEvents("wings_flap");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Armament.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
