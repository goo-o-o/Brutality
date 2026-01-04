package net.goo.brutality.item.curios.hands;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseHandsCurio;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class EyeOfTheDragon extends BaseHandsCurio {


    public EyeOfTheDragon(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    UUID EOTD_ARMOR_PEN_UUID = UUID.fromString("4323ba54-a23f-4a20-9541-363b1704b868");
    UUID EOTD_LETHALITY_UUID = UUID.fromString("6f6b1f81-1d98-4522-9ce4-708f96315dbe");
    UUID EOTD_CRITICAL_DAMAGE_UUID = UUID.fromString("01e372a1-364e-4888-8340-9add0685e7bd");
    UUID EOTD_CRITICAL_CHANCE_UUID = UUID.fromString("9aae58f2-6af4-4557-a97d-fa213a3cdfad");
    UUID EOTD_SWORD_DAMAGE_UUID = UUID.fromString("f8dd1666-df59-4b56-ad2d-930066212267");
    UUID EOTD_AXE_DAMAGE_UUID = UUID.fromString("b3e3cf12-685d-4a04-9a37-c490b67d4777");
    UUID EOTD_SPEAR_DAMAGE_UUID = UUID.fromString("dbe0ac0e-fd10-419b-b37d-3a2e4379ab29");
    UUID EOTD_SCYTHE_DAMAGE_UUID = UUID.fromString("abd85ec4-51f0-43dc-8ec8-be965b4ac981");
    UUID EOTD_HAMMER_DAMAGE_UUID = UUID.fromString("c0abb073-4042-4801-8c4f-27c48723020c");
    UUID EOTD_PIERCING_DAMAGE_UUID = UUID.fromString("989d1d67-e5c0-46bb-b682-1b1e802da028");
    UUID EOTD_SLASH_DAMAGE_UUID = UUID.fromString("105d0682-8ec0-4302-bc7a-f524bfaaaab0");
    UUID EOTD_BLUNT_DAMAGE_UUID = UUID.fromString("02a1d561-25e6-4cf6-bc2a-db99785b08e8");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.ARMOR_PENETRATION.get(),
                    new AttributeModifier(EOTD_ARMOR_PEN_UUID, "Armor Pen Buff", 0.01F, AttributeModifier.Operation.MULTIPLY_BASE));

            builder.put(ModAttributes.LETHALITY.get(),
                    new AttributeModifier(EOTD_LETHALITY_UUID, "Lethality Buff", 1, AttributeModifier.Operation.ADDITION));

            builder.put(ModAttributes.CRITICAL_STRIKE_CHANCE.get(),
                    new AttributeModifier(EOTD_CRITICAL_CHANCE_UUID, "Crit Chance Buff", 0.025F, AttributeModifier.Operation.MULTIPLY_BASE));

            builder.put(ModAttributes.CRITICAL_STRIKE_DAMAGE.get(),
                    new AttributeModifier(EOTD_CRITICAL_DAMAGE_UUID, "Crit Damage Buff", 0.025F, AttributeModifier.Operation.MULTIPLY_BASE));

            builder.put(ModAttributes.SWORD_DAMAGE.get(),
                    new AttributeModifier(EOTD_SWORD_DAMAGE_UUID, "Sword Damage Buff", 1F, AttributeModifier.Operation.ADDITION));

            builder.put(ModAttributes.SPEAR_DAMAGE.get(),
                    new AttributeModifier(EOTD_SPEAR_DAMAGE_UUID, "Spear Damage Buff", 1F, AttributeModifier.Operation.ADDITION));

            builder.put(ModAttributes.AXE_DAMAGE.get(),
                    new AttributeModifier(EOTD_AXE_DAMAGE_UUID, "Axe Damage Buff", 1F, AttributeModifier.Operation.ADDITION));

            builder.put(ModAttributes.SCYTHE_DAMAGE.get(),
                    new AttributeModifier(EOTD_SCYTHE_DAMAGE_UUID, "Scythe Damage Buff", 1F, AttributeModifier.Operation.ADDITION));

            builder.put(ModAttributes.HAMMER_DAMAGE.get(),
                    new AttributeModifier(EOTD_HAMMER_DAMAGE_UUID, "Hammer Damage Buff", 1F, AttributeModifier.Operation.ADDITION));

            builder.put(ModAttributes.PIERCING_DAMAGE.get(),
                    new AttributeModifier(EOTD_PIERCING_DAMAGE_UUID, "Piercing Damage Buff", 1F, AttributeModifier.Operation.ADDITION));

            builder.put(ModAttributes.SLASH_DAMAGE.get(),
                    new AttributeModifier(EOTD_SLASH_DAMAGE_UUID, "Slash Damage Buff", 1F, AttributeModifier.Operation.ADDITION));

            builder.put(ModAttributes.BLUNT_DAMAGE.get(),
                    new AttributeModifier(EOTD_BLUNT_DAMAGE_UUID, "Blunt Damage Buff", 1F, AttributeModifier.Operation.ADDITION));



            return builder.build();
    }

}
