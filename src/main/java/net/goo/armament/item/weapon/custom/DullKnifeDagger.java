package net.goo.armament.item.weapon.custom;

import net.goo.armament.Armament;
import net.goo.armament.client.renderers.item.ArmaAutoFullbrightItemRenderer;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaSwordItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID)
public class DullKnifeDagger extends ArmaSwordItem {
    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putString("mode", "neutral");
        return stack;
    }

    public DullKnifeDagger(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public <R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, ArmaAutoFullbrightItemRenderer.class);
    }

    @Override
    public String texture(ItemStack stack) {
        return "dull_knife_" + stack.getOrCreateTag().getString("mode");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        String currentMode = stack.getOrCreateTag().getString("mode");

        currentMode = switch (currentMode) {
            case "neutral" -> "happy";
            case "happy" -> "sad";
            case "sad" -> "angry";
            default -> "neutral";
        };

        stack.getOrCreateTag().putString("mode", currentMode);

        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
