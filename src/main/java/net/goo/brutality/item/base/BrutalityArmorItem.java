package net.goo.brutality.item.base;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.armor.BrutalityArmorRenderer;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
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
import java.util.Locale;
import java.util.function.Consumer;

public class BrutalityArmorItem extends ArmorItem implements BrutalityGeoItem {
    private final List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents;
    public String identifier;
    public Rarity rarity;

    public BrutalityArmorItem(ArmorMaterial pMaterial, Type pType,
                              Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pMaterial, pType, new Item.Properties());
        this.rarity = rarity;
        this.descriptionComponents = descriptionComponents;
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/armor/" + getMaterial().toString().toLowerCase(Locale.ROOT) + "_armor.png").toString();
    }


    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack pStack) {
        return this.rarity;
    }

    @Override
    public @NotNull Component getName(ItemStack pStack) {
        return brutalityNameHandler(pStack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        brutalityTooltipHandler(pStack, pTooltipComponents, descriptionComponents, rarity);
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public BrutalityCategories.AttackType getAttackType() {
        return BrutalityCategories.AttackType.NONE;
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.ItemType.ARMOR;
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

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(
                    LivingEntity livingEntity,
                    ItemStack itemStack,
                    EquipmentSlot equipmentSlot,
                    HumanoidModel<?> original) {

                if (this.renderer == null) {
                    try {
                        this.renderer = new BrutalityArmorRenderer<>();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to instantiate GeoArmorRenderer: " + e);
                    }
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

}
