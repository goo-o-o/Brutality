package net.goo.brutality.item.base;

import net.goo.brutality.item.BrutalityItemCategories;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.List;
import java.util.function.Consumer;

public class BrutalityArmorItem extends ArmorItem implements BrutalityGeoItem {
    private final List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents;
    public String identifier;
    public BrutalityItemCategories category;
    public Rarity rarity;

    public BrutalityArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties, String identifier,
                              Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pMaterial, pType, pProperties);
        this.identifier = identifier;
        this.rarity = rarity;
        this.descriptionComponents = descriptionComponents;
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack pStack) {
        return this.rarity;
    }

    @Override
    public @NotNull Component getName(ItemStack pStack) {
        return brutalityNameHandler(pStack, identifier);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        brutalityHoverTextHandler(pStack, pTooltipComponents, descriptionComponents, rarity, identifier);
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }


    @Override
    public String geoIdentifier() {
        return this.identifier;
    }

    @Override
    public BrutalityItemCategories category() {
        return BrutalityItemCategories.ARMOR;
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public <R extends GeoArmorRenderer<?>> void initGeoArmor(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null) {
                    try {
                        this.renderer = rendererClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to instantiate GeoArmorRenderer: " + e);
                    }
                }
                // This prepares our GeoArmorRenderer for the current render frame.
                // These parameters may be null however, so we don't do anything further with them
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
            }
        });
    }
}
