package net.goo.armament.item;

import net.goo.armament.util.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.List;

import static net.goo.armament.util.ModResources.*;

public class ArmaSwordItem extends SwordItem implements ArmaGeoItem {
    public String identifier;
    public ModItemCategories category;
    protected int[][] colors;

    public ArmaSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        this.category = category;
        this.identifier = identifier;
    }


    public String getIdentifier() {
        return this.identifier;
    }

    public ResourceLocation getFontFromCategory(ModItemCategories category) {
        this.category = category;
        switch (category) {
            case SILLY -> {
                return SILLY;
            }
            case SPACE -> {
                return SPACE;
            }
            case TECHNOLOGY -> {
                return TECHNOLOGY;
            }
            case FANTASY -> {
                return FANTASY;
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public Component getName(ItemStack pStack) {
        return ModUtils.tooltipHelper("item.armament." + identifier, false, getFontFromCategory(category), colors);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament." + identifier + ".desc.1", false, null, colors[1]));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament." + identifier + ".desc.2", false, null, colors[0]));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament." + identifier + ".desc.3", false, null, colors[1]));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public String geoIdentifier() {
        return this.identifier;
    }

    @Override
    public GeoAnimatable cacheItem() {
        return null;
    }

}
