package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.magic.SpellCastingHandler;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class ScribesIndex extends BrutalityCurioItem {


    public ScribesIndex(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

    private static final Object2BooleanOpenHashMap<UUID> MANA_STATE_MAP = new Object2BooleanOpenHashMap<>();

    UUID SCRIBES_INDEX_SPELL_DAMAGE_UUID = UUID.fromString("8cec320c-659a-4221-b8e1-2e73e734f831");

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            if (!player.level().isClientSide() && player.tickCount % 10 == 0) {
                AttributeInstance spellDamage = player.getAttribute(ModAttributes.SPELL_DAMAGE.get());
                boolean isMaxMana = SpellCastingHandler.getManaHandler(player).isMaxMana(player);
                float newBonus = isMaxMana ? 0.25F : -0.1F;
                UUID uuid = player.getUUID();
                boolean wasMaxMana = MANA_STATE_MAP.getOrDefault(uuid, false);
                if (spellDamage != null && wasMaxMana != isMaxMana) {
                    MANA_STATE_MAP.put(uuid, isMaxMana);
                    spellDamage.removeModifier(SCRIBES_INDEX_SPELL_DAMAGE_UUID);
                    spellDamage.addTransientModifier(
                            new AttributeModifier(
                                    SCRIBES_INDEX_SPELL_DAMAGE_UUID,
                                    "Spell Damage Bonus",
                                    newBonus,
                                    AttributeModifier.Operation.MULTIPLY_TOTAL
                            )
                    );
                }
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() != null) MANA_STATE_MAP.removeBoolean(slotContext.entity().getUUID());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            boolean isMaxMana = SpellCastingHandler.getManaHandler(player).isMaxMana(player);
            float newBonus = isMaxMana ? 0.25F : -0.1F;

            builder.put(ModAttributes.SPELL_DAMAGE.get(), new AttributeModifier(SCRIBES_INDEX_SPELL_DAMAGE_UUID, "Spell Damage Buff", newBonus, AttributeModifier.Operation.MULTIPLY_TOTAL));

            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
