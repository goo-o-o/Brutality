package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.network.PacketHandler;
import net.goo.armament.network.c2sTerraBeamPacket;
import net.minecraft.network.chat.Component;
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

        // TERRA BEAM PROJECTILE START //
        if (player.getMainHandItem().getItem() instanceof TerraBladeSwordItem) {
            PacketHandler.sendToServer(new c2sTerraBeamPacket());
        }
        // TERRA BEAM PROJECTILE END //

    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.armament.terra_blade.desc.1"));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(Component.translatable("item.armament.terra_blade.desc.2"));
        pTooltipComponents.add(Component.translatable("item.armament.terra_blade.desc.3"));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

}
