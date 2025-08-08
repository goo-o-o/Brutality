package net.goo.brutality.item.curios.charm;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class Envy extends BrutalityCurioItem {
    private static final String HEALTH_DIFF = "health_diff";

    public Envy(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }


    private static void updateCurrentBonus(Player player, ItemStack stack) {
        Player nearestPlayer = player.level().getNearestPlayer(player.getX(), player.getY(), player.getZ(), 10, e ->
                e instanceof Player p && !p.isCreative() && !p.isSpectator() && p != player &&
                        !CuriosApi.getCuriosInventory(p).map(handler -> handler.isEquipped(BrutalityModItems.ENVY_CHARM.get())).orElse(false)
        );

        CompoundTag tag = stack.getOrCreateTag();
        if (nearestPlayer == null) {
            tag.putFloat(HEALTH_DIFF, 0);
            return;
        }

        float currentBonus = tag.getFloat(HEALTH_DIFF);


        float newBonus = getNewBonus(player, nearestPlayer);

        if (currentBonus == newBonus) {
            return;
        }

        tag.putFloat(HEALTH_DIFF, newBonus);
    }

    private static float getNewBonus(Player wearer, Player nearestPlayer) {
        AttributeInstance maxHealthAttr = wearer.getAttribute(Attributes.MAX_HEALTH);
        float playerHealthNoEnvy = 20;
        if (maxHealthAttr != null) {
            playerHealthNoEnvy = (float) maxHealthAttr.getBaseValue();
            for (AttributeModifier modifier : maxHealthAttr.getModifiers()) {
                if (!modifier.getId().equals(ENVY_HP_UUID)) {
                    playerHealthNoEnvy += (float) modifier.getAmount();
                }
            }
        }
        return Math.max((nearestPlayer.getMaxHealth() - playerHealthNoEnvy) / 2, 0);
    }

    private static final UUID ENVY_HP_UUID = UUID.fromString("0a98b1be-25b9-4c38-8e4e-762979e8a1a3");

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player && player.tickCount % 10 == 0) {
            AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealth != null) {
                updateCurrentBonus(player, stack);
                maxHealth.removeModifier(ENVY_HP_UUID);
                maxHealth.addTransientModifier(
                        new AttributeModifier(
                                ENVY_HP_UUID,
                                "Envy HP Bonus",
                                stack.getOrCreateTag().getFloat(HEALTH_DIFF),
                                AttributeModifier.Operation.ADDITION
                        )
                );

            }
        }
    }


    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
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

        float healthDiff = stack.getOrCreateTag().getFloat(HEALTH_DIFF);

        tooltip.add(Component.literal((healthDiff >= 0 ? "+" : "") + healthDiff + " ").append(Component.translatable("attribute.name.generic.max_health"))
                .withStyle(ChatFormatting.BLUE));

    }
}
