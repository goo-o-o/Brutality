package net.goo.brutality.item.block;

import net.goo.brutality.item.base.BrutalityBlockItem;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class CoffeeMachineBlockItem extends BrutalityBlockItem {

    public CoffeeMachineBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

//    @Override
//    public String texture(ItemStack stack) {
//        if (this.getName(getDefaultInstance()).contains(Component.translatable("block.brutality.eeffoc_machine"))) {
//            return "eeffoc_machine";
//        }
//
//        return super.texture(stack);
//    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//        if (this.getName(getDefaultInstance()).contains(Component.translatable("block.brutality.eeffoc_machine"))) {
//            controllers.add(new AnimationController<>(this, (state) ->
//                    state.setAndContinue(RawAnimation.begin().thenLoop("eeffoc")))
//            );
//        }
    }
}
