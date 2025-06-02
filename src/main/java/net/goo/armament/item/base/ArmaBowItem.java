package net.goo.armament.item.base;

import net.goo.armament.item.ModItemCategories;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BowItem;
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

public class ArmaBowItem extends BowItem implements ArmaGeoItem {
    public String identifier;
    public ModItemCategories category;
    public Rarity rarity;
    public int abilityCount;

    public ArmaBowItem(Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pProperties.stacksTo(1));
        this.category = category;
        this.identifier = identifier;
        this.rarity = rarity;
        this.abilityCount = abilityCount;
    }
    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack pStack) {
        return this.rarity;
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
    public String geoIdentifier() {
        return this.identifier;
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    @Override
    public ModItemCategories category() {
        return category;
    }

    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    public static float getPowerForTime(int pCharge, float fullDrawTicks) {
        float f = (float)pCharge / fullDrawTicks;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

}
