package net.goo.brutality.item.tool;

import net.goo.brutality.item.base.BrutalityGenericItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class TheCloudItem extends BrutalityGenericItem {
    private static final String
            DATA = "data",
            HEALTH = "health",
            HUNGER = "hunger",
            SATURATION = "saturation",
            OXYGEN = "oxygen",
            EFFECTS = "effects";

    public TheCloudItem(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

//    @Override
//    public boolean isFoil(ItemStack pStack) {
//        return pStack.getOrCreateTag().contains(DATA);
//    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (pLevel.isClientSide()) return InteractionResultHolder.fail(stack);

        float health = pPlayer.getHealth();
        FoodData foodData = pPlayer.getFoodData();
        int hunger = foodData.getFoodLevel();
        float saturation = foodData.getSaturationLevel();
        int oxygen = pPlayer.getAirSupply();

        CompoundTag dataTag = new CompoundTag();
        dataTag.putFloat(HEALTH, health);
        dataTag.putInt(HUNGER, hunger);
        dataTag.putFloat(SATURATION, saturation);
        dataTag.putInt(OXYGEN, oxygen);

        ListTag effectsList = new ListTag();

        pPlayer.getActiveEffects().forEach(effect -> {
            CompoundTag effectTag = new CompoundTag();
            effectsList.add(effect.save(effectTag));

        });
        stack.getOrCreateTag().putInt(CUSTOM_MODEL_DATA, 1);


        if (!stack.getOrCreateTag().contains(DATA)) {

            dataTag.put(EFFECTS, effectsList);
            stack.addTagElement(DATA, dataTag);

        } else {
            stack.getOrCreateTag().remove(CUSTOM_MODEL_DATA);


            CompoundTag itemData = stack.getOrCreateTag().getCompound(DATA);

            pPlayer.setHealth(itemData.getFloat(HEALTH));
            FoodData foodData1 = pPlayer.getFoodData();
            foodData1.setFoodLevel(itemData.getInt(HUNGER));
            foodData1.setSaturation(itemData.getFloat(SATURATION));
            pPlayer.setAirSupply(itemData.getInt(OXYGEN));

            ListTag itemEffects = itemData.getList(EFFECTS, CompoundTag.TAG_COMPOUND);
            pPlayer.removeAllEffects();

            itemEffects.forEach(tag -> {
                pPlayer.addEffect(Objects.requireNonNull(MobEffectInstance.load((CompoundTag) tag)));
            });

            stack.removeTagKey(DATA);
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        if (pStack.getOrCreateTag().contains(DATA)) {
            pTooltipComponents.add(Component.empty());

            CompoundTag itemData = pStack.getOrCreateTag().getCompound(DATA);

            MutableComponent health = Component.literal(": %.1f".formatted(itemData.getFloat(HEALTH)));
            MutableComponent oxygen = Component.literal(": %.1f".formatted(itemData.getFloat(OXYGEN)));
            MutableComponent saturation = Component.literal(": %.1f".formatted(itemData.getFloat(SATURATION)));
            MutableComponent hunger = Component.literal(": %.1f".formatted(itemData.getFloat(HUNGER)));

            pTooltipComponents.add(
                    Component.translatable("item.brutality.the_cloud.stored_data"));
            pTooltipComponents.add(Component.translatable("item.brutality.the_cloud.health").append(health));
            pTooltipComponents.add(
                    Component.translatable("effect.minecraft.hunger").withStyle(ChatFormatting.DARK_RED).append(hunger.withStyle(ChatFormatting.WHITE)));
            pTooltipComponents.add(
                    Component.translatable("effect.minecraft.saturation").withStyle(ChatFormatting.GOLD).append(saturation.withStyle(ChatFormatting.WHITE)));
            pTooltipComponents.add(
                    Component.translatable("item.brutality.the_cloud.oxygen").append(oxygen));
            pTooltipComponents.add(Component.empty());

            pTooltipComponents.add(Component.translatable("item.brutality.the_cloud.active_effects"));

            ListTag itemEffects = itemData.getList(EFFECTS, CompoundTag.TAG_COMPOUND);

            itemEffects.forEach(tag -> {
                MobEffectInstance effectInstance = MobEffectInstance.load(((CompoundTag) tag));

                if (effectInstance == null) return;

                MutableComponent tooltip = effectInstance.getEffect().getDisplayName().plainCopy().append(effectData(effectInstance));

                pTooltipComponents.add(tooltip);
            });

        }

    }

    private MutableComponent describeDuration(MobEffectInstance instance) {
        return Component.literal(instance.isInfiniteDuration() ? "∞" : instance.getDuration() + " seconds");
    }

    public MutableComponent effectData(MobEffectInstance instance) {

        Component amplifier = Component.literal(instance.getAmplifier() > 0 ? String.valueOf(instance.getAmplifier() + 1) : "").withStyle(ChatFormatting.GREEN);
        Component duration = (Component.literal("⌚: ").append(describeDuration(instance))).withStyle(ChatFormatting.BLUE);

        MutableComponent s;
        s = Component.literal(" ")
                .append(amplifier)
                .append(" | ").withStyle(ChatFormatting.WHITE)
                .append(duration);

        if (!instance.isVisible()) {
            s = s.append(" | Particles: false");
        }

        if (!instance.showIcon()) {
            s = s.append(" | Show Icon: false");
        }

        return s;
    }
}
