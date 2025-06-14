package net.goo.brutality.item.weapon.custom;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.item.BrutalityAutoFullbrightItemRenderer;
import net.goo.brutality.item.base.BrutalityScytheItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
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
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class DarkinScythe extends BrutalityScytheItem {
    protected String[] types = new String[]{"", "_shadow_assasin", "_rhaast"};


    public DarkinScythe(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public ItemStack getDefaultInstance() {
        return new ItemStack(this);
    }

    @Override
    public String texture(ItemStack stack) {
        return "darkin_scythe" + types[stack.getOrCreateTag().getInt("CustomModelData")];
    }

    @Override
    public String model(ItemStack stack) {
        return "darkin_scythe" + types[stack.getOrCreateTag().getInt("CustomModelData")];
    }

    @Override
    public <R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, BrutalityAutoFullbrightItemRenderer.class);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (pUsedHand == InteractionHand.OFF_HAND) return InteractionResultHolder.fail(stack);

        int customModelData = stack.getOrCreateTag().getInt("CustomModelData");
        customModelData = (customModelData + 1) % 3;


        stack.getOrCreateTag().putInt("CustomModelData", (customModelData));
        pPlayer.getCooldowns().addCooldown(stack.getItem(), 20);

        return super.use(pLevel, pPlayer, pUsedHand);
    }

}
