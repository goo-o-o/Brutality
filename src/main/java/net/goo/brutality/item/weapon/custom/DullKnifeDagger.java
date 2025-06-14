package net.goo.brutality.item.weapon.custom;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.item.BrutalityAutoFullbrightItemRenderer;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.ModMobEffects;
import net.goo.brutality.registry.ModItems;
import net.goo.brutality.registry.ModSounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.EnvironmentColorManager;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class DullKnifeDagger extends BrutalitySwordItem {
    public DullKnifeDagger(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, rarity, descriptionComponents);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putInt("CustomModelData", 0);
        return stack;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    int[][][] colors = new int[][][]{{{255, 255, 255}, {34, 34, 34}}, {{255, 226, 0}, {255, 48, 251}}, {{0, 26, 127}, {0, 242, 255}}, {{255, 201, 0}, {215, 0, 0}}};

    @Override
    public @NotNull Component getName(ItemStack pStack) {
        return BrutalityTooltipHelper.tooltipHelper("item." + Brutality.MOD_ID + "." + identifier, false, null, 0.5F, 2, colors[pStack.getOrCreateTag().getInt("CustomModelData")]);
    }

    @Override
    public <R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, BrutalityAutoFullbrightItemRenderer.class);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.empty());

        for (int i = 0; i < emotions.length; i++) {
            pTooltipComponents.add(BrutalityTooltipHelper.tooltipHelper("item." + Brutality.MOD_ID + ".dull_knife." + emotions[i], true, null, colors[i][0], colors[i][1]));

            for (int j = 1; j <= 3; j++) {
                MutableComponent component = Component.translatable("item." + Brutality.MOD_ID + "." + emotions[i] + ".desc." + j);

                if (!component.getString().isBlank()) {
                    pTooltipComponents.add(component.withStyle(ChatFormatting.BLUE));
                }
            }

            pTooltipComponents.add(Component.empty());
        }

    }

    String[] emotions = new String[]{"neutral", "happy", "sad", "angry"};

    @Override
    public String texture(ItemStack stack) {
        int currentMode = stack.getOrCreateTag().getInt("CustomModelData");

        return "dull_knife_" + emotions[currentMode];
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (pUsedHand == InteractionHand.OFF_HAND) return InteractionResultHolder.fail(stack);

        int customModelData = stack.getOrCreateTag().getInt("CustomModelData");
        customModelData = (customModelData + 1) % 4;


        stack.getOrCreateTag().putInt("CustomModelData", (customModelData));
        pPlayer.getCooldowns().addCooldown(stack.getItem(), 20);


        pLevel.playSound(pPlayer, pPlayer.getOnPos(), ModSounds.DULL_KNIFE_ABILITY.get(), SoundSource.PLAYERS);

        if (pLevel.isClientSide) {

            if (pPlayer.getOffhandItem().is(ModItems.DULL_KNIFE_DAGGER.get())) {
                pPlayer.getOffhandItem().getOrCreateTag().putInt("CustomModelData", customModelData);
            }

            int[] color1 = new int[]{colors[customModelData][0][0], colors[customModelData][0][1], colors[customModelData][0][2]};
            int[] color2 = new int[]{colors[customModelData][1][0], colors[customModelData][1][1], colors[customModelData][1][2]};

            EnvironmentColorManager.setColor(EnvironmentColorManager.ColorType.SKY, color1);
            EnvironmentColorManager.setColor(EnvironmentColorManager.ColorType.FOG, color2);
        }


        return super.use(pLevel, pPlayer, pUsedHand);
    }

    List<MobEffect> emotionEffects = List.of(
            ModMobEffects.NEUTRAL.get(),
            ModMobEffects.HAPPY.get(),
            ModMobEffects.SAD.get(),
            ModMobEffects.ANGRY.get()
    );

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player player)
            pAttacker.level().playSound(player, player.getOnPos(), ModSounds.DULL_KNIFE_STAB.get(), SoundSource.PLAYERS);

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public void onDeselected(Player player) {
        EnvironmentColorManager.resetAllColors();
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (!ModList.get().isLoaded("bettercombat") && entity instanceof Player player) {
            player.level().playSound(player, player.getOnPos(), ModSounds.DULL_KNIFE_SWING.get(), SoundSource.PLAYERS, 1F, ModUtils.nextFloatBetweenInclusive(player.getRandom(), 0.5F, 1F));
        }

        return super.onEntitySwing(stack, entity);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        return super.onEntityItemUpdate(stack, entity);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {

        if (pEntity instanceof LivingEntity entity) {
            if (entity.getMainHandItem().is(ModItems.DULL_KNIFE_DAGGER.get())) {
                int customModelData = entity.getMainHandItem().getOrCreateTag().getInt("CustomModelData");
                entity.addEffect(new MobEffectInstance(emotionEffects.get(customModelData), 1, 0, false, true));
            }
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        EnvironmentColorManager.resetAllColors();
        return super.onDroppedByPlayer(item, player);
    }
}
