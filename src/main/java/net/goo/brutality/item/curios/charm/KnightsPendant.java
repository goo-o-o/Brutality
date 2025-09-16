package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class KnightsPendant extends BrutalityCurioItem {
    public KnightsPendant(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.NECKLACE;
    }

    UUID KNIGHTS_PENDANT_SWORD_DAMAGE_UUID = UUID.fromString("49baa2f0-d7f6-4e47-8a28-571b5074989d");
    UUID KNIGHTS_PENDANT_LETHALITY_UUID = UUID.fromString("a23c6244-a53c-4c40-8f74-7db93bb8b4f4");
    UUID KNIGHTS_PENDANT_ARMOR_PEN_UUID = UUID.fromString("3ddff39e-688d-420e-b768-25ea7d33bad8");

    private static final Object2BooleanOpenHashMap<UUID> OLD_BONUS_MAP = new Object2BooleanOpenHashMap<>();

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {
                AttributeInstance attackDamage = livingEntity.getAttribute(Attributes.ATTACK_DAMAGE);
                boolean active = livingEntity.getHealth() / livingEntity.getMaxHealth() < 0.75F;
                UUID uuid = livingEntity.getUUID();
                boolean wasActive = OLD_BONUS_MAP.getOrDefault(uuid, false);
                if (attackDamage != null && wasActive != active) {
                    OLD_BONUS_MAP.put(uuid, active);
                    attackDamage.removeModifier(KNIGHTS_PENDANT_ARMOR_PEN_UUID);
                    if (active)
                        attackDamage.addTransientModifier(
                                new AttributeModifier(
                                        KNIGHTS_PENDANT_ARMOR_PEN_UUID,
                                        "Armor Pen Buff",
                                        0.05,
                                        AttributeModifier.Operation.MULTIPLY_BASE
                                )
                        );
                }
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() != null) OLD_BONUS_MAP.removeBoolean(slotContext.entity().getUUID());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.SWORD_DAMAGE.get(), new AttributeModifier(KNIGHTS_PENDANT_SWORD_DAMAGE_UUID, "Sword Damage Buff", 4, AttributeModifier.Operation.ADDITION));
        builder.put(ModAttributes.LETHALITY.get(), new AttributeModifier(KNIGHTS_PENDANT_LETHALITY_UUID, "Lethality Buff", 4, AttributeModifier.Operation.ADDITION));
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (livingEntity.getHealth() / livingEntity.getMaxHealth() < 0.75F)
                builder.put(ModAttributes.ARMOR_PENETRATION.get(), new AttributeModifier(KNIGHTS_PENDANT_ARMOR_PEN_UUID, "Armor Pen Buff", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        return builder.build();
    }
}
