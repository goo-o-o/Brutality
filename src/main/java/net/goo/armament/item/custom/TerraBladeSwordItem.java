package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.network.PacketHandler;
import net.goo.armament.network.c2sTerraBeamPacket;
import net.goo.armament.util.ModUtils;
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

import static net.goo.armament.util.ModResources.FANTASY;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TerraBladeSwordItem extends SwordItem {
    int[] color1 = new int[]{174, 229, 58};
    int[] color2 = new int[]{0, 82, 60};
    private final ModItemCategories category;

    public TerraBladeSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, ModItemCategories category) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        this.category = category;
    }


    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        if (player.getMainHandItem().getItem() instanceof TerraBladeSwordItem) {
            PacketHandler.sendToServer(new c2sTerraBeamPacket());
        }
    }

    public ModItemCategories getCategory() {
        return category;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.terra_blade.desc.1", false, null, color2));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.terra_blade.desc.2", false, null, color1));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.terra_blade.desc.3", false, null, color2));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return ModUtils.tooltipHelper("item.armament.terra_blade", false, FANTASY, color1, color2);
    }

}
