package net.goo.brutality.item.block;

import net.goo.brutality.event.mod.client.BrutalityModBlockRenderManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class FilingCabinetBlockItem extends BlockItem {
    public FilingCabinetBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(BrutalityModBlockRenderManager.getDefaultExtensions());
    }
}
