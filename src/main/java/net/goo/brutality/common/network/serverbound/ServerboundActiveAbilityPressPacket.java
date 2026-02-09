package net.goo.brutality.common.network.serverbound;

import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.util.CooldownUtils;
import net.goo.brutality.util.build_archetypes.RageHelper;
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

import java.util.function.Supplier;

public class ServerboundActiveAbilityPressPacket {
    public ServerboundActiveAbilityPressPacket() {
    }

    public ServerboundActiveAbilityPressPacket(FriendlyByteBuf buf) {
    }

    public void write(FriendlyByteBuf buf) {
    }

    public static void handle(ServerboundActiveAbilityPressPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;

            CooldownUtils.validateCurioCooldown(sender, BrutalityItems.DECK_OF_CARDS.get(), 20 * 21, () -> sender.addEffect(new MobEffectInstance(BrutalityEffects.ARCANE_SURGE.get(),
                    (sender.getRandom().nextIntBetweenInclusive(40, 200)), 51)));

            CooldownUtils.validateCurioCooldown(sender, BrutalityItems.MIRACLE_CURE.get(), 20 * 60, () -> sender.getActiveEffects().stream().map(MobEffectInstance::getEffect).filter(effect -> !effect.isBeneficial()).forEach(sender::removeEffect));

            CooldownUtils.validateCurioCooldown(sender, BrutalityItems.EMERGENCY_MEETING.get(), 20 * 60, () -> {
                sender.level().playSound(null, BlockPos.containing(sender.position()), BrutalitySounds.EMERGENCY_MEETING.get(), SoundSource.PLAYERS, 0.75F, 1.0F);
                sender.level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT,
                        sender, sender.getBoundingBox().inflate(7.5F)).forEach(e ->
                        e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200)));
            });

            CooldownUtils.validateCurioCooldown(sender, BrutalityItems.VINDICATOR_STEROIDS.get(), 45 * 20, ()
                    -> {
                sender.level().playSound(null, BlockPos.containing(sender.position()), TerramityModSounds.BOTTLEPOP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                sender.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200));
                sender.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200));
            });

            CooldownUtils.validateCurioCooldown(sender, BrutalityItems.LIGHT_SWITCH.get(), 400, () -> {
                sender.level().playSound(null, BlockPos.containing(sender.position()), BrutalitySounds.LIGHT_SWITCH.get(), SoundSource.PLAYERS, 0.75F, 1.0F);
                sender.level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT,
                        sender, sender.getBoundingBox().inflate(7.5F)).forEach(e ->
                        e.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 160)));
            });

            CooldownUtils.validateCurioCooldown(sender, BrutalityItems.STRESS_PILLS.get(), 60 * 20, () -> {
                RageHelper.modifyRageValue(sender, (float) sender.getAttributeValue(BrutalityAttributes.MAX_MANA.get()));
                sender.level().playSound(null, BlockPos.containing(sender.position()), TerramityModSounds.BOTTLEPOP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            });

            CooldownUtils.validateCurioCooldown(sender, BrutalityItems.SEROTONIN_PILLS.get(), 60 * 20, () -> {
                sender.addEffect(new MobEffectInstance(BrutalityEffects.TRANQUILITY.get(), 200, 9));
                RageHelper.modifyRageValue(sender, 0);
            });


        });
        context.setPacketHandled(true);
    }
}