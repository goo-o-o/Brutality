package net.goo.brutality.item.weapon.custom;

import net.goo.brutality.client.renderers.item.BrutalityTranslucentCullItemRenderer;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.function.Consumer;

public class DoubleDownSword extends BrutalitySwordItem{
    public DoubleDownSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public <R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, BrutalityTranslucentCullItemRenderer.class);
    }
}
