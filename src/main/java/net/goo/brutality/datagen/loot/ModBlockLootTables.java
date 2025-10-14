package net.goo.brutality.datagen.loot;

import net.goo.brutality.registry.BrutalityModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.add(BrutalityModBlocks.WATER_COOLER_BLOCK.get(), block -> this.createNameableBlockEntityTable(BrutalityModBlocks.WATER_COOLER_BLOCK.get()));
        this.add(BrutalityModBlocks.COFFEE_MACHINE_BLOCK.get(), block -> this.createNameableBlockEntityTable(BrutalityModBlocks.COFFEE_MACHINE_BLOCK.get()));
        this.add(BrutalityModBlocks.SUPER_SNIFFER_FIGURE_BLOCK.get(), block -> this.createNameableBlockEntityTable(BrutalityModBlocks.SUPER_SNIFFER_FIGURE_BLOCK.get()));

        this.add(BrutalityModBlocks.GREEN_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(BrutalityModBlocks.GREEN_CUBICLE_PANEL.get()));
        this.add(BrutalityModBlocks.WHITE_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(BrutalityModBlocks.WHITE_CUBICLE_PANEL.get()));
        this.add(BrutalityModBlocks.GRAY_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(BrutalityModBlocks.GRAY_CUBICLE_PANEL.get()));
        this.add(BrutalityModBlocks.LIGHT_GRAY_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(BrutalityModBlocks.LIGHT_GRAY_CUBICLE_PANEL.get()));
        this.add(BrutalityModBlocks.BLUE_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(BrutalityModBlocks.BLUE_CUBICLE_PANEL.get()));
        this.add(BrutalityModBlocks.RED_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(BrutalityModBlocks.RED_CUBICLE_PANEL.get()));

        this.add(BrutalityModBlocks.GRAY_OFFICE_CARPET.get(), block -> this.createSingleItemTable(BrutalityModBlocks.GRAY_OFFICE_CARPET.get()));
        this.add(BrutalityModBlocks.LIGHT_GRAY_OFFICE_CARPET.get(), block -> this.createSingleItemTable(BrutalityModBlocks.LIGHT_GRAY_OFFICE_CARPET.get()));

    }


    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock, this.applyExplosionDecay(pBlock,
                LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BrutalityModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
