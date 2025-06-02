package net.goo.armament.item.weapon.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaHammerItem;
import net.goo.armament.registry.ModParticles;
import net.goo.armament.registry.ModSounds;
import net.goo.armament.util.helpers.ModTooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class JackpotHammer extends ArmaHammerItem implements GeoItem {
    private final float attackSpeedModifier;

    public JackpotHammer(Tier pTier, int pAttackDamageModifier, float attackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, attackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
        this.attackSpeedModifier = attackSpeedModifier;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        // Add the existing modifiers using the super method
        Multimap<Attribute, AttributeModifier> existingModifiers = super.getAttributeModifiers(slot, stack);
        for (Attribute attribute : existingModifiers.keySet()) {
            for (AttributeModifier modifier : existingModifiers.get(attribute)) {
                builder.put(attribute, modifier);
            }
        }

        // Apply the attack speed modifier if the item is in the main hand or offhand
        if (slot == EquipmentSlot.MAINHAND) {
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeedModifier, AttributeModifier.Operation.ADDITION));
        }

        // Build and return the immutable multimap
        return builder.build();
    }

    @Override
    public Component getName(ItemStack pStack) {
        Level pLevel = Minecraft.getInstance().level;
        return ModTooltipHelper.tooltipHelper("item.armament." + identifier, false, getFontFromCategory(category), pLevel.getGameTime(), 0.5F, 4, BASE_COLOR_MAP.get(this.getClass()));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level level = player.level();
        int damage;
        if (!level.isClientSide && player instanceof ServerPlayer) {
            if (player.getAttackStrengthScale(1.0F) == 1) {
                stack.hurtAndBreak(1, player, (consumer) -> {
                    consumer.broadcastBreakEvent(player.getUsedItemHand());
                });

                damage = level.random.nextInt(4, 14);

                if (damage <= 8) {
                    player.displayClientMessage(Component.literal(damage + " damage"), true);

                } else {
                    int msgPicker = level.random.nextInt(3);

                    player.displayClientMessage(Component.literal("\u00a7" +
                            (msgPicker == 0 ? "a\u00a7l\u00a7oCHA-CHING! " : msgPicker == 1 ? "b\u00a7l\u00a7oHUGE WIN! " : "c\u00a7l\u00a7oJACKPOT! ") + "\u00a7r" + damage + " damage"), true);

                    int randomIndex = level.random.nextInt(ModSounds.JACKPOT_SOUNDS.size());
                    SoundEvent jackpotSound = ModSounds.JACKPOT_SOUNDS.get(randomIndex).get();

                    player.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), jackpotSound, SoundSource.PLAYERS, 1F, 1F);

                    ((ServerLevel) level).sendParticles(ModParticles.POKER_CHIP_GREEN_PARTICLE.get(),
                            entity.getX(), entity.getY() + entity.getBbHeight() / 1.5, entity.getZ(),
                            25, 0, 0, 0, 0);
                    ((ServerLevel) level).sendParticles(ModParticles.POKER_CHIP_RED_PARTICLE.get(),
                            entity.getX(), entity.getY() + entity.getBbHeight() / 1.5, entity.getZ(),
                            25, 0, 0, 0, 0);
                    ((ServerLevel) level).sendParticles(ModParticles.POKER_CHIP_BLUE_PARTICLE.get(),
                            entity.getX(), entity.getY() + entity.getBbHeight() / 1.5, entity.getZ(),
                            25, 0, 0, 0, 0);
                    ((ServerLevel) level).sendParticles(ModParticles.POKER_CHIP_YELLOW_PARTICLE.get(),
                            entity.getX(), entity.getY() + entity.getBbHeight() / 1.5, entity.getZ(),
                            25, 0, 0, 0, 0);


                }

            } else damage = 0;


            entity.hurt(entity.damageSources().playerAttack(player), damage);
        }

        return true;
    }

}
