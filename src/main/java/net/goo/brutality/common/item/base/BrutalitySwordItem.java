package net.goo.brutality.common.item.base;

import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.event.mod.client.BrutalityModItemRenderManager;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.function.Consumer;

public class BrutalitySwordItem extends SwordItem implements BrutalityGeoItem {
    protected List<ItemDescriptionComponent> descriptionComponents = List.of();

    public BrutalitySwordItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, (int) pAttackDamageModifier, pAttackSpeedModifier, new Item.Properties().rarity(rarity));
        this.descriptionComponents = descriptionComponents;
    }
    public BrutalitySwordItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity) {
        super(pTier, (int) pAttackDamageModifier, pAttackSpeedModifier, new Item.Properties().rarity(rarity));
    }
    public BrutalitySwordItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, int durability) {
        super(pTier, (int) pAttackDamageModifier, pAttackSpeedModifier, new Item.Properties().rarity(rarity).durability(durability));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return BrutalityModItemRenderManager.createRenderer(BrutalitySwordItem.this);
            }
        });
    }

    @Override
    public BrutalityCategories.AttackType getAttackType() {
        return BrutalityCategories.AttackType.SLASH;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        brutalityTooltipHandler(pStack, pTooltipComponents, descriptionComponents, getRarity(pStack));
    }


    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.ItemType.SWORD;
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
