package net.goo.brutality.item.curios.necklace;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.registry.BrutalityModAttributes;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class KnightsPendant extends BrutalityCurioItem {
    public KnightsPendant(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID ARMOR_PEN_UUID = null;

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        // Only run on the server (world.isClientSide() check) and only every 10 ticks
        if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {

            // 2. Target the correct attribute instance!
            AttributeInstance armorPenAttribute = livingEntity.getAttribute(BrutalityModAttributes.ARMOR_PENETRATION.get());
            if (armorPenAttribute == null) return;

            // 3. Define the active condition
            boolean active = livingEntity.getHealth() / livingEntity.getMaxHealth() < 0.75F;

            // 4. Check if the modifier is currently applied
            boolean modifierIsPresent = armorPenAttribute.getModifier(ARMOR_PEN_UUID) != null;

            if (active && !modifierIsPresent) {
                // Condition met, and modifier is missing -> ADD IT
                AttributeModifier dynamicModifier = new AttributeModifier(
                        ARMOR_PEN_UUID,
                        "KnightsPendant_ArmorPenBuff", // Use a unique modifier name
                        0.05,
                        AttributeModifier.Operation.MULTIPLY_BASE
                );
                // Use addTransientModifier to apply the bonus
                armorPenAttribute.addTransientModifier(dynamicModifier);

            } else if (!active && modifierIsPresent) {
                // Condition NOT met, but modifier is present -> REMOVE IT
                armorPenAttribute.removeModifier(ARMOR_PEN_UUID);
            }
        }
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(BrutalityModAttributes.SWORD_DAMAGE.get(), new AttributeModifier(uuid, "Sword Damage Buff", 4, AttributeModifier.Operation.ADDITION));
        builder.put(BrutalityModAttributes.LETHALITY.get(), new AttributeModifier(uuid, "Lethality Buff", 4, AttributeModifier.Operation.ADDITION));
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (livingEntity.getHealth() / livingEntity.getMaxHealth() < 0.75F) {
                this.ARMOR_PEN_UUID = uuid;
                builder.put(BrutalityModAttributes.ARMOR_PENETRATION.get(), new AttributeModifier(uuid, "Armor Pen Buff", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }
        return builder.build();
    }
}
