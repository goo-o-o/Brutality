package net.goo.brutality.common.item.curios;

import net.goo.brutality.util.build_archetypes.GastronomyDebuffContainer;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.goo.brutality.util.tooltip.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BrutalityGastronomyCurioItem extends BrutalityCurioItem {
    public List<GastronomyDebuffContainer> trueMeleeDebuffs = new ArrayList<>();
    public List<GastronomyDebuffContainer> nonTrueMeleeDebuffs = new ArrayList<>();

    public BrutalityGastronomyCurioItem(Rarity rarity) {
        super(rarity);
    }

    public BrutalityGastronomyCurioItem(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        TooltipHelper.handleGastronomyItemDescriptions(pStack, this, pTooltipComponents);
    }

    public BrutalityGastronomyCurioItem withDebuffs(GastronomyDebuffContainer... debuffContainers) {
        for (GastronomyDebuffContainer container : debuffContainers) {
            if (container.requiresMelee()) {
                trueMeleeDebuffs.add(container);
            } else {
                nonTrueMeleeDebuffs.add(container);
            }
        }
        return this;
    }
}
