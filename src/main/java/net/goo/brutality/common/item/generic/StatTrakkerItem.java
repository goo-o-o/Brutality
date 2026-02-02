package net.goo.brutality.common.item.generic;

import com.mojang.blaze3d.platform.InputConstants;
import net.goo.brutality.Brutality;
import net.goo.brutality.util.item.ItemCategoryUtils;
import net.goo.brutality.util.item.StatTrakUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Supplier;

public class StatTrakkerItem extends Item {
    public StatTrakkerItem(Properties pProperties, Supplier<Double> chanceSupplier) {
        super(pProperties);
        this.chanceSupplier = chanceSupplier;
    }

    public static void incrementOnShear(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getItemStack();
        if (event.getTarget() instanceof IForgeShearable shearable && shearable.isShearable(stack, event.getLevel(), event.getPos())) {
            if (ItemCategoryUtils.isShear(stack)) {
                StatTrakUtils.incrementStatTrakIfPossible(stack);
            }
        }
    }

    public static void incrementOnKill(LivingEntity killer) {
        if (ItemCategoryUtils.isWeapon(killer.getMainHandItem()))
            StatTrakUtils.incrementStatTrakIfPossible(killer.getMainHandItem());
    }

    public static void incrementOnBlockBreak(BlockEvent.BreakEvent event) {
        ItemStack stack = event.getPlayer().getMainHandItem();
        if (ItemCategoryUtils.isTool(stack)) {
            StatTrakUtils.incrementStatTrakIfPossible(stack);
        }
    }

    public static void incrementOnShieldBlock(ShieldBlockEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player player) {
            ItemStack shield = player.getUseItem();
            StatTrakUtils.incrementStatTrakIfPossible(shield);
        } else {
            ItemStack mainShield = livingEntity.getMainHandItem();
            if (ItemCategoryUtils.isShield(mainShield)) {
                StatTrakUtils.incrementStatTrakIfPossible(mainShield);
                return;
            }
            ItemStack offShield = livingEntity.getOffhandItem();
            if (ItemCategoryUtils.isShield(offShield)) {
                StatTrakUtils.incrementStatTrakIfPossible(offShield);
            }
        }
    }

    private final Supplier<Double> chanceSupplier;
    DecimalFormat format = new DecimalFormat("0.#");

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + pStack.getItem() + ".description.1"));
        pTooltipComponents.add(Component.empty());
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + pStack.getItem() + ".description.2"));

        pTooltipComponents.add(Component.literal(format.format(chanceSupplier.get()) + "% ")
                .append(Component.translatable("item." + Brutality.MOD_ID + "." + pStack.getItem() + ".description.3")));
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + pStack.getItem() + ".description.4"));


        Minecraft mc = Minecraft.getInstance();
        if (InputConstants.isKeyDown(mc.getWindow().getWindow(), mc.options.keyShift.getKey().getValue())) {
            for (int i = 1; i <= 14; i++) {
                pTooltipComponents.add(Component.translatable("stat_trak." + Brutality.MOD_ID + "." + i));
            }
        } else {
            pTooltipComponents.add(
                    Component.translatable("message." + Brutality.MOD_ID + ".press")
                            .append(Minecraft.getInstance().options.keyShift.getKey().getDisplayName())
                            .append(Component.translatable("message." + Brutality.MOD_ID + ".show_more")).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

}
