package net.goo.brutality.item.curios.charm;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.capabilities.EntityCapabilities;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.Optional;

public class DiamondRespawnCard extends BrutalityCurioItem {


    public DiamondRespawnCard(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }



    @Override
    public @NotNull ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        return ICurio.DropRule.ALWAYS_KEEP;
    }



    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) return;

        Optional<GlobalPos> deathLoc = player.getLastDeathLocation();

        deathLoc.ifPresent(globalPos -> {
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("message." + Brutality.MOD_ID + ".last_death_location")
                    .append(globalPos.pos().toShortString()).withStyle(ChatFormatting.DARK_GRAY));

        });

    }

}
