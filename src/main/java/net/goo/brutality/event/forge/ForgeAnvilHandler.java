package net.goo.brutality.event.forge;

import net.goo.brutality.item.seals.BaseSealItem;
import net.goo.brutality.util.SealUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

@Mod.EventBusSubscriber (bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeAnvilHandler {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack output = left.copy();



        if (right.getItem() instanceof BaseSealItem sealItem && !(left.getItem() instanceof ICurioItem || left.getItem() instanceof BaseSealItem)) {
            SealUtils.SEAL_TYPE sealType = sealItem.getSealType();
            if (sealType != null && SealUtils.getSealType(left) == null) {
                SealUtils.addSeal(output, sealType);
                event.setOutput(output);
                event.setCost(5);
                event.setMaterialCost(left.getCount());
            }
        }
    }
}
