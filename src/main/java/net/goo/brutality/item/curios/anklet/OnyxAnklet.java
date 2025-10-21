package net.goo.brutality.item.curios.anklet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class OnyxAnklet extends BrutalityAnkletItem {


    public OnyxAnklet(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.ANKLET;
    }

    UUID ONYX_ANKLET_DODGE_UUID = UUID.fromString("bab07d78-5381-4575-b3c6-6c13e68e2df6");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.DODGE_CHANCE.get(),
                    new AttributeModifier(ONYX_ANKLET_DODGE_UUID, "Dodge Buff", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();

        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

    private static final List<UUID> THE_LIST = List.of(
            UUID.fromString("a2177cf2-4ef2-4588-93a2-eb04cf108cf0"), // Jordan
            UUID.fromString("8d961a89-e771-4c48-9156-900321b5629e"), // Typo
            UUID.fromString("69191b57-8eb5-4525-a8cb-138f46fd1a64"), // Carroch
            UUID.fromString("b990fd13-3cf4-418b-ad18-7da93f25da90") // Tenevares
    );

    @Override
    public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
//        if (THE_LIST.contains(dodger.getUUID())) {
//            Minecraft.crash(CrashReport.forThrowable(new Throwable(), "sybau"));
//        }

        Level level = dodger.level();
        if (dodger instanceof Player player && !player.getCooldowns().isOnCooldown(this)) {

            if (!level.isClientSide()) {
                double d0 = dodger.getX();
                double d1 = dodger.getY();
                double d2 = dodger.getZ();

                for (int i = 0; i < 16; ++i) {
                    double d3 = dodger.getX() + (dodger.getRandom().nextDouble() - 0.5D) * 16.0D;
                    double d4 = Mth.clamp(dodger.getY() + (double) (dodger.getRandom().nextInt(16) - 8), level.getMinBuildHeight(), level.getMinBuildHeight() + ((ServerLevel) level).getLogicalHeight() - 1);
                    double d5 = dodger.getZ() + (dodger.getRandom().nextDouble() - 0.5D) * 16.0D;
                    if (dodger.isPassenger()) {
                        dodger.stopRiding();
                    }

                    Vec3 vec3 = dodger.position();
                    level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(dodger));
                    if (dodger.randomTeleport(d3, d4, d5, true)) {
                        level.playSound(null, d0, d1, d2, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                        dodger.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                        player.getCooldowns().addCooldown(this, 60);
                        break;
                    }
                }

            }
        }

    }
}
