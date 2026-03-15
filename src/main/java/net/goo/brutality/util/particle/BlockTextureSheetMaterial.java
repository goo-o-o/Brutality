package net.goo.brutality.util.particle;

import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlockTextureSheetMaterial extends TextureMaterial {

    public BlockTextureSheetMaterial() {
        super(InventoryMenu.BLOCK_ATLAS);
    }

}
