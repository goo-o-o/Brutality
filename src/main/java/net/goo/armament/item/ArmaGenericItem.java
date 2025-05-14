package net.goo.armament.item;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

import java.util.List;

public class ArmaGenericItem extends Item implements ArmaGeoItem {
    public String identifier;
    public ModItemCategories category;
    public Rarity rarity;
    public int abilityCount;

    public ArmaGenericItem(Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pProperties);
        this.category = category;
        this.identifier = identifier;
        this.rarity = rarity;
        this.abilityCount = abilityCount;

    }

    @Override
    public @NotNull Component getName(ItemStack pStack) {
        assert Minecraft.getInstance().level != null;
        return armaNameHandler(Minecraft.getInstance().level, this.getClass(), category, identifier);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        armaHoverTextHandler(pStack, pTooltipComponents, abilityCount, this.getClass(), rarity, identifier);
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public String geoIdentifier() {
        return this.identifier;
    }

    @Override
    public ModItemCategories category() {
        return category;
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
}
