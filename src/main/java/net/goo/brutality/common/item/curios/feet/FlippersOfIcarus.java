package net.goo.brutality.common.item.curios.feet;

import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class FlippersOfIcarus extends BrutalityCurioItem {
    public FlippersOfIcarus(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if (wearer.tickCount % 20 == 0) {
            if (wearer.getY() > 200 && wearer.level().canSeeSkyFromBelowWater(wearer.blockPosition())) {
                wearer.setSecondsOnFire(2);
            }
            if (wearer.isVisuallySwimming()) {
                wearer.level().addParticle(TerramityModParticleTypes.HOLY_GLINT.get(), wearer.getX(0.5), wearer.getY(0.5), wearer.getZ(0.5F),
                        wearer.random.nextFloat() * 0.5F - 0.25F,
                        wearer.random.nextFloat() * 0.5F - 0.25F,
                        wearer.random.nextFloat() * 0.5F - 0.25F
                );
            }
        }
    }

}
