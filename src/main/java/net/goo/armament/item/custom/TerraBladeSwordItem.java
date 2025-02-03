package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.network.PacketHandler;
import net.goo.armament.network.c2sTerraBeamPacket;
import net.goo.armament.util.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TerraBladeSwordItem extends SwordItem {
    public TerraBladeSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }


    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        if (player.getMainHandItem().getItem() instanceof TerraBladeSwordItem) {
            PacketHandler.sendToServer(new c2sTerraBeamPacket());
        }

    }

    int[] color1 = new int[]{174, 229, 58};
    int[] color2 = new int[]{0, 82, 60};

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.armament.terra_blade.desc.1").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(color2))));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(Component.translatable("item.armament.terra_blade.desc.2").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(color1))));
        pTooltipComponents.add(Component.translatable("item.armament.terra_blade.desc.3").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(color2))));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return ModUtils.addGradientText((Component.translatable("item.armament.terra_blade")), color1, color2).withStyle(Style.EMPTY.withBold(true));
    }

}
