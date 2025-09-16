package net.goo.brutality.item.curios.anklet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class AnkleMonitor extends BrutalityAnkletItem {


    public AnkleMonitor(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID ANKLE_MONITOR_UUID = UUID.fromString("afbfbc88-1201-4de5-a36a-143b2d3b3e33");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.DODGE_CHANCE.get(),
                new AttributeModifier(ANKLE_MONITOR_UUID, "Dodge Buff", 0.35, AttributeModifier.Operation.MULTIPLY_BASE));
        return builder.build();
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof ServerPlayer player) || player.tickCount % 10 != 0) {
            return;
        }
        boolean applyEffects;
        BlockPos pos = BlockPos.containing(player.getPosition(1));

        if (player.getRespawnDimension().equals(player.level().dimension())) {
            BlockPos spawnPos = player.getRespawnPosition();
            if (spawnPos == null) {
                spawnPos = player.level().getSharedSpawnPos();
            }
            applyEffects = pos.distSqr(spawnPos) > 22500;
        } else {
            applyEffects = true;
        }

        if (applyEffects) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 11, 1, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 11, 0, false, false));
        }
    }

}
