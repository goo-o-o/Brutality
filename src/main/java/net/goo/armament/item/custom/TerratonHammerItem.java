package net.goo.armament.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.armament.Armament;
import net.goo.armament.item.base.ArmaHammerItem;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.registry.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.UUID;

import static net.goo.armament.util.ModResources.TERRATON_HAMMER_COLORS;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TerratonHammerItem extends ArmaHammerItem implements GeoItem {
    private static final UUID MOVEMENT_SPEED_MODIFIER_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;

    public TerratonHammerItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
        this.attributeModifiers = ImmutableMultimap.<Attribute, AttributeModifier>builder()
        .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 29, AttributeModifier.Operation.ADDITION))
        .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.5, AttributeModifier.Operation.ADDITION))
        .put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_MODIFIER_UUID, "Tool modifier", -0.25, AttributeModifier.Operation.MULTIPLY_BASE))
        .build();
        this.colors = TERRATON_HAMMER_COLORS;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(equipmentSlot, stack);
    }

    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingTickEvent event) {
        // Check if the entity is a player
        if (event.getEntity() instanceof Player player) {
            // Check if the player is holding the Terraton Hammer in the main hand
            if (player.getMainHandItem().getItem() == ModItems.TERRATON_HAMMER.get()) {
                // Apply the mining fatigue effect
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 4, 8, false, false));
            }
        }
    }

}
