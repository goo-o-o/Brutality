package net.goo.brutality.datagen.loot;

import net.goo.brutality.registry.ModBlocks;
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
        this.add(ModBlocks.WATER_COOLER_BLOCK.get(), block -> this.createNameableBlockEntityTable(ModBlocks.WATER_COOLER_BLOCK.get()));
        this.add(ModBlocks.COFFEE_MACHINE_BLOCK.get(), block -> this.createNameableBlockEntityTable(ModBlocks.COFFEE_MACHINE_BLOCK.get()));
        this.add(ModBlocks.SUPER_SNIFFER_FIGURE_BLOCK.get(), block -> this.createNameableBlockEntityTable(ModBlocks.SUPER_SNIFFER_FIGURE_BLOCK.get()));

        this.add(ModBlocks.GREEN_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(ModBlocks.GREEN_CUBICLE_PANEL.get()));
        this.add(ModBlocks.WHITE_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(ModBlocks.WHITE_CUBICLE_PANEL.get()));
        this.add(ModBlocks.GRAY_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(ModBlocks.GRAY_CUBICLE_PANEL.get()));
        this.add(ModBlocks.LIGHT_GRAY_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(ModBlocks.LIGHT_GRAY_CUBICLE_PANEL.get()));
        this.add(ModBlocks.BLUE_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(ModBlocks.BLUE_CUBICLE_PANEL.get()));
        this.add(ModBlocks.RED_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(ModBlocks.RED_CUBICLE_PANEL.get()));

        this.add(ModBlocks.GRAY_OFFICE_CARPET.get(), block -> this.createSingleItemTable(ModBlocks.GRAY_OFFICE_CARPET.get()));
        this.add(ModBlocks.LIGHT_GRAY_OFFICE_CARPET.get(), block -> this.createSingleItemTable(ModBlocks.LIGHT_GRAY_OFFICE_CARPET.get()));

    }


    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock, this.applyExplosionDecay(pBlock,
                LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
