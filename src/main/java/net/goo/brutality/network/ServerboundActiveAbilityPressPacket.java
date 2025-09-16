package net.goo.brutality.network;

import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.ModUtils;
import net.mcreator.terramity.init.TerramityModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Supplier;

public class ServerboundActiveAbilityPressPacket {
    public ServerboundActiveAbilityPressPacket() {
    }

    public ServerboundActiveAbilityPressPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public static void handle(ServerboundActiveAbilityPressPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;
            CuriosApi.getCuriosInventory(sender).ifPresent(handler -> {

                ModUtils.handleActiveAbilityWithCd(handler, BrutalityModItems.DECK_OF_CARDS.get(), 20 * 21, () -> {
                    sender.addEffect(new MobEffectInstance(BrutalityModMobEffects.MAGIC_POWER.get(),
                            (sender.getRandom().nextIntBetweenInclusive(40, 200)), 51));
                });
                ModUtils.handleActiveAbilityWithCd(handler, BrutalityModItems.VINDICATOR_STEROIDS.get(), 45 * 20, ()
                        -> {
                    sender.level().playSound(null, BlockPos.containing(sender.position()), TerramityModSounds.BOTTLEPOP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    sender.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200));
                    sender.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200));
                });
                ModUtils.handleActiveAbilityWithCd(handler, BrutalityModItems.LIGHT_SWITCH.get(), 400, () -> {
                    MobEffectInstance instance = new MobEffectInstance(MobEffects.DARKNESS, 160);
                    sender.level().playSound(null, BlockPos.containing(sender.position()), BrutalityModSounds.LIGHT_SWITCH.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    sender.level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT,
                            sender, sender.getBoundingBox().inflate(7.5F)).forEach(e -> e.addEffect(instance));
                });


            });
        });
        context.setPacketHandled(true);
    }
}