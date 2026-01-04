package net.goo.brutality.mixin.mixins;

import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Villager.class)
public class VillagerMixin {
//    @ModifyExpressionValue(method = "updateSpecialPrices", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;getPlayerReputation(Lnet/minecraft/world/entity/player/Player;)I"))
//    private int increaseReputation(int original) {
//        Player player = Minecraft.getInstance().player;
//        if (player != null) {
//            Optional<ICuriosItemHandler> handler = CuriosApi.getCuriosInventory(player).resolve();
//            if (handler.isPresent()) {
//                Optional<SlotResult> slotResult = handler.get().findFirstCurio(BrutalityModItems.ANKLE_MONITOR.get());
//                if (slotResult.isPresent()) {
//                    return (int) (original * 0.75F);
//                }
//            }
//        }
//        return original;
//    }
}
