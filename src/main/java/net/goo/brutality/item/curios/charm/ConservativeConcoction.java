package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModAttributes;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class ConservativeConcoction extends BrutalityCurioItem {


    public ConservativeConcoction(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    UUID CONCOCTION_MANA_REGEN_UUID = UUID.fromString("6b9be152-d330-4b38-82ad-6cebfc3e4f32");
    private static final Object2FloatOpenHashMap<UUID> OLD_BONUS_MAP = new Object2FloatOpenHashMap<>();

    private static float getBonus(Player player) {
        return player.getCapability(BrutalityCapabilities.PLAYER_MANA_CAP).map(playerManaCap -> playerManaCap.getCurrentManaPercentage(player)).orElse(0F);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            if (!player.level().isClientSide() && player.tickCount % 10 == 0) {
                player.getCapability(BrutalityCapabilities.PLAYER_MANA_CAP).ifPresent(cap -> {
                    AttributeInstance manaRegen = player.getAttribute(BrutalityModAttributes.MANA_REGEN.get());
                    UUID uuid = player.getUUID();
                    if (manaRegen != null) {
                        float newBonus = getBonus(player);
                        float oldBonus = OLD_BONUS_MAP.getOrDefault(uuid, 0.0F);

                        if (Math.abs(oldBonus - newBonus) > 0.0001) {
                            OLD_BONUS_MAP.put(uuid, newBonus);
                            manaRegen.removeModifier(CONCOCTION_MANA_REGEN_UUID);
                            manaRegen.addTransientModifier(
                                    new AttributeModifier(
                                            CONCOCTION_MANA_REGEN_UUID,
                                            "Mana Regen Bonus",
                                            newBonus,
                                            AttributeModifier.Operation.MULTIPLY_TOTAL
                                    )
                            );
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() != null) OLD_BONUS_MAP.removeFloat(slotContext.entity().getUUID());
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        if (slotContext.entity() instanceof Player player) {
            builder.put(BrutalityModAttributes.MANA_REGEN.get(),
                    new AttributeModifier(
                            CONCOCTION_MANA_REGEN_UUID,
                            "Mana Regen Bonus",
                            getBonus(player),
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
        }
        return builder.build();
    }
}
