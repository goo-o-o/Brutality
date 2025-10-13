package net.goo.brutality.item.weapon.sword;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.goo.brutality.util.helpers.EnvironmentColorManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.awt.*;
import java.util.List;
import java.util.Locale;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class DullKnifeSword extends BrutalitySwordItem {


    public DullKnifeSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        ModUtils.setTextureIdx(stack, 0);
        return stack;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    public enum EmotionColor {
        NEUTRAL(new Color(255, 255, 255), new Color(34, 34, 34), BrutalityModMobEffects.NEUTRAL.get()),
        HAPPY(new Color(255, 226, 0), new Color(255, 48, 251), BrutalityModMobEffects.HAPPY.get()),
        SAD(new Color(0, 26, 127), new Color(0, 242, 255), BrutalityModMobEffects.SAD.get()),
        ANGRY(new Color(255, 201, 0), new Color(215, 0, 0), BrutalityModMobEffects.ANGRY.get());

        public final int primaryColor;
        public final int secondaryColor;
        private final MobEffect effect;

        EmotionColor(Color primaryColor, Color secondaryColor, MobEffect effect) {
            this.primaryColor = FastColor.ARGB32.color(255, primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue());
            this.secondaryColor = FastColor.ARGB32.color(255, secondaryColor.getRed(), secondaryColor.getGreen(), secondaryColor.getBlue());
            this.effect = effect;
        }

        public static EmotionColor byId(int id) {
            return values()[Mth.clamp(id, 0, values().length - 1)];
        }
    }

    @Override
    public @NotNull Component getName(ItemStack pStack) {
        String identifier = getRegistryName();
        EmotionColor emotionColor = EmotionColor.byId(ModUtils.getTextureIdx(pStack));

        return BrutalityTooltipHelper.tooltipHelper(
                "item." + Brutality.MOD_ID + "." + identifier, false, null, 0.5F, 2F, emotionColor.primaryColor, emotionColor.secondaryColor);
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.empty());

        for (EmotionColor emotion : EmotionColor.values()) {
            String emotionKey = emotion.name().toLowerCase(Locale.ROOT);

            pTooltipComponents.add(
                    BrutalityTooltipHelper.tooltipHelper("item." + Brutality.MOD_ID + ".dull_knife." + emotionKey, true, null, 0F, 1F, emotion.primaryColor, emotion.secondaryColor));

            for (int j = 1; j <= 3; j++) {
                MutableComponent component = Component.translatable("item." + Brutality.MOD_ID + ".dull_knife." + emotionKey + ".desc." + j);

                if (!component.getString().isBlank()) {
                    pTooltipComponents.add(component.withStyle(ChatFormatting.BLUE));
                }
            }

            pTooltipComponents.add(Component.empty());
        }

    }


    @Override
    public String texture(@javax.annotation.Nullable ItemStack stack) {
        int currentMode = ModUtils.getTextureIdx(stack);

        return "dull_knife_" + EmotionColor.values()[currentMode].toString().toLowerCase(Locale.ROOT);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (pUsedHand == InteractionHand.OFF_HAND) return InteractionResultHolder.fail(stack);

        int emotionIndex = ModUtils.getTextureIdx(stack);
        emotionIndex = (emotionIndex + 1) % 4;


        ModUtils.setTextureIdx(stack, emotionIndex);
        pPlayer.getCooldowns().addCooldown(stack.getItem(), 20);


        pLevel.playSound(pPlayer, pPlayer.getOnPos(), BrutalityModSounds.DULL_KNIFE_ABILITY.get(), SoundSource.PLAYERS);

        if (pLevel.isClientSide) {

            if (stack.is(BrutalityModItems.DULL_KNIFE_DAGGER.get())) {
                ModUtils.setTextureIdx(stack, emotionIndex);
            }

            EnvironmentColorManager.setColor(EnvironmentColorManager.ColorType.SKY, EmotionColor.byId(emotionIndex).primaryColor);
            EnvironmentColorManager.setColor(EnvironmentColorManager.ColorType.FOG, EmotionColor.byId(emotionIndex).secondaryColor);
        }


        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player player)
            pAttacker.level().playSound(player, player.getOnPos(), BrutalityModSounds.DULL_KNIFE_STAB.get(), SoundSource.PLAYERS);

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }


    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (!ModList.get().isLoaded("bettercombat") && entity instanceof Player player) {
            player.level().playSound(player, player.getOnPos(), BrutalityModSounds.DULL_KNIFE_SWING.get(), SoundSource.PLAYERS,
                    1F, Mth.nextFloat(player.getRandom(), 0.5F, 1F));
        }

        return super.onEntitySwing(stack, entity);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        return super.onEntityItemUpdate(stack, entity);
    }


}
