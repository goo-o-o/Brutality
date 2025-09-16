package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.ClientboundSyncCapabilitiesPacket;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class SpiteShard extends BrutalityCurioItem {


    public SpiteShard(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }


    UUID SPITE_SHARD_MAX_RAGE_UUID = UUID.fromString("e2bc1e9c-5cde-4de1-8e3a-60d97d6673d8");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.MAX_RAGE.get(), new AttributeModifier(SPITE_SHARD_MAX_RAGE_UUID, "Max Rage Buff", 50, AttributeModifier.Operation.ADDITION));
            return builder.build();

        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            if (player.tickCount % 20 == 0) {
                List<Mob> aggroEntities =
                        player.level().getNearbyEntities(Mob.class, TargetingConditions.forCombat().selector(e -> e instanceof Mob mob && mob.getTarget() == player),
                                player, player.getBoundingBox().inflate(5));

                if (!aggroEntities.isEmpty()) {
                    player.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP).ifPresent(cap -> {
                                cap.incrementRage(aggroEntities.size() / 5F);
                                PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(player.getId(), player));
                            }
                    );
                }
            }
        }
    }
}
