package net.goo.brutality.item.curios;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class Envy extends BrutalityCurioItem {
    private float healthDiff = 0;

    public Envy(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }


    UUID ENVY_HP_UUID = UUID.fromString("0a98b1be-25b9-4c38-8e4e-762979e8a1a3");

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {

//            LivingEntity entity = player.level().getNearestEntity(LivingEntity.class, TargetingConditions.DEFAULT, player, player.getX(), player.getY(), player.getZ(), player.getBoundingBox().inflate(10));
            LivingEntity entity = player.level().getNearestPlayer(player, 10);

            AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealth != null) {
                maxHealth.removeModifier(ENVY_HP_UUID);
                if (entity != null) {

                    healthDiff = Math.max(entity.getMaxHealth() - player.getMaxHealth(), 0);
                    maxHealth.addTransientModifier(
                            new AttributeModifier(
                                    ENVY_HP_UUID,
                                    "Temporary HP Bonus",
                                    healthDiff / 2,
                                    AttributeModifier.Operation.ADDITION
                            )
                    );
                }
            }
        }
    }

    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance maxHealth = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (maxHealth != null) {
                maxHealth.removeModifier(ENVY_HP_UUID);
            }
        }
    }


    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
//        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("curios.modifiers.charm").withStyle(ChatFormatting.GOLD));


        tooltip.add(Component.literal((healthDiff / 2 >= 0 ? "+" : "") + healthDiff / 2 + " ").append(Component.translatable("attribute.name.generic.max_health"))
                .withStyle(ChatFormatting.BLUE));

    }
}
