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

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Armament.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
