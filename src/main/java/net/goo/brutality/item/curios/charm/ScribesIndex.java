package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.magic.SpellCastingHandler;
import net.goo.brutality.registry.BrutalityModAttributes;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class ScribesIndex extends BrutalityCurioItem {


    public ScribesIndex(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    private static final Object2BooleanOpenHashMap<UUID> MANA_STATE_MAP = new Object2BooleanOpenHashMap<>();

    UUID SCRIBES_INDEX_SPELL_DAMAGE_UUID = null;

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;
        if (player.level().isClientSide() || player.tickCount % 10 != 0) return;

        var spellDamage = player.getAttribute(BrutalityModAttributes.SPELL_DAMAGE.get());
        if (spellDamage == null) return;

        boolean isMaxMana = SpellCastingHandler.getManaHandler(player)
                .map(cap -> cap.isMaxMana(player))
                .orElse(false);

        UUID uuid = player.getUUID();
        boolean wasMaxMana = MANA_STATE_MAP.getOrDefault(uuid, false);
        if (wasMaxMana == isMaxMana) return; // No change

        MANA_STATE_MAP.put(uuid, isMaxMana);
        spellDamage.removeModifier(SCRIBES_INDEX_SPELL_DAMAGE_UUID);

        float bonus = isMaxMana ? 0.25F : -0.1F;
        spellDamage.addTransientModifier(new AttributeModifier(
                SCRIBES_INDEX_SPELL_DAMAGE_UUID,
                "Scribes Index Bonus",
                bonus,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        ));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() != null) MANA_STATE_MAP.removeBoolean(slotContext.entity().getUUID());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            this.SCRIBES_INDEX_SPELL_DAMAGE_UUID = uuid;
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            boolean isMaxMana = SpellCastingHandler.getManaHandler(player).map(cap -> cap.isMaxMana(player)).orElse(false);
            float newBonus = isMaxMana ? 0.25F : -0.1F;

            builder.put(BrutalityModAttributes.SPELL_DAMAGE.get(), new AttributeModifier(SCRIBES_INDEX_SPELL_DAMAGE_UUID, "Spell Damage Buff", newBonus, AttributeModifier.Operation.MULTIPLY_TOTAL));

            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
