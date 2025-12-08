package net.goo.brutality.datagen.loot;

import net.goo.brutality.block.custom.ImportantDocumentsBlock;
import net.goo.brutality.registry.BrutalityModBlocks;
import net.goo.brutality.registry.BrutalityModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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

        this.add(BrutalityModBlocks.OLD_SERVER_CASING.get(), block -> this.createSingleItemTable(BrutalityModBlocks.OLD_SERVER_CASING.get()));
        this.add(BrutalityModBlocks.OLD_SERVER_PANEL.get(), block -> this.createSingleItemTable(BrutalityModBlocks.OLD_SERVER_PANEL.get()));
        this.add(BrutalityModBlocks.PLASTERBOARD.get(), block -> this.createSingleItemTable(BrutalityModBlocks.PLASTERBOARD.get()));
        this.add(BrutalityModBlocks.GREEN_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(BrutalityModBlocks.GREEN_CUBICLE_PANEL.get()));
        this.add(BrutalityModBlocks.WHITE_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(BrutalityModBlocks.WHITE_CUBICLE_PANEL.get()));
        this.add(BrutalityModBlocks.GRAY_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(BrutalityModBlocks.GRAY_CUBICLE_PANEL.get()));
        this.add(BrutalityModBlocks.OLD_AIR_CONDITIONER.get(), block -> this.createSingleItemTable(BrutalityModBlocks.OLD_AIR_CONDITIONER.get()));
        this.add(BrutalityModBlocks.EXIT_SIGN.get(), block -> this.createSingleItemTable(BrutalityModBlocks.EXIT_SIGN.get()));
        this.add(BrutalityModBlocks.UPPER_HVAC.get(), block -> this.createSingleItemTable(BrutalityModBlocks.UPPER_HVAC.get()));
        this.add(BrutalityModBlocks.LOWER_HVAC.get(), block -> this.createSingleItemTable(BrutalityModBlocks.LOWER_HVAC.get()));
        this.add(BrutalityModBlocks.STYROFOAM_CUP.get(), block -> this.createSingleItemTable(BrutalityModItems.STYROFOAM_CUP.get()));
        this.add(BrutalityModBlocks.MUG.get(), block -> this.createSingleItemTable(BrutalityModItems.MUG.get()));
        this.add(BrutalityModBlocks.LIGHT_GRAY_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(BrutalityModBlocks.LIGHT_GRAY_CUBICLE_PANEL.get()));
        this.add(BrutalityModBlocks.BLUE_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(BrutalityModBlocks.BLUE_CUBICLE_PANEL.get()));
        this.add(BrutalityModBlocks.RED_CUBICLE_PANEL.get(), block -> this.createSingleItemTable(BrutalityModBlocks.RED_CUBICLE_PANEL.get()));
        this.add(BrutalityModBlocks.CRT_MONITOR.get(), block -> this.createSingleItemTable(BrutalityModBlocks.CRT_MONITOR.get()));
        this.add(BrutalityModBlocks.TOILET.get(), block -> this.createSingleItemTable(BrutalityModBlocks.TOILET.get()));
        this.add(BrutalityModBlocks.URINAL.get(), block -> this.createSingleItemTable(BrutalityModBlocks.URINAL.get()));
        this.add(BrutalityModBlocks.LCD_MONITOR.get(), block -> this.createSingleItemTable(BrutalityModBlocks.LCD_MONITOR.get()));
        this.add(BrutalityModBlocks.DUSTBIN.get(), block -> this.createSingleItemTable(BrutalityModBlocks.DUSTBIN.get()));
        this.add(BrutalityModBlocks.WHITE_OFFICE_CHAIR.get(), block -> this.createSingleItemTable(BrutalityModBlocks.WHITE_OFFICE_CHAIR.get()));
        this.add(BrutalityModBlocks.BLACK_OFFICE_CHAIR.get(), block -> this.createSingleItemTable(BrutalityModBlocks.BLACK_OFFICE_CHAIR.get()));
        this.add(BrutalityModBlocks.WET_FLOOR_SIGN.get(), block -> this.createSingleItemTable(BrutalityModBlocks.WET_FLOOR_SIGN.get()));
        this.add(BrutalityModBlocks.OFFICE_LIGHT.get(), block -> this.createSingleItemTable(BrutalityModBlocks.OFFICE_LIGHT.get()));
        this.add(BrutalityModBlocks.SMALL_OFFICE_LIGHT.get(), block -> this.createSingleItemTable(BrutalityModBlocks.SMALL_OFFICE_LIGHT.get()));

        this.add(BrutalityModBlocks.GRAY_OFFICE_CARPET.get(), block -> this.createSingleItemTable(BrutalityModBlocks.GRAY_OFFICE_CARPET.get()));
        this.add(BrutalityModBlocks.LIGHT_GRAY_OFFICE_CARPET.get(), block -> this.createSingleItemTable(BrutalityModBlocks.LIGHT_GRAY_OFFICE_CARPET.get()));
        this.add(BrutalityModBlocks.GRAY_OFFICE_RUG.get(), block -> this.createSingleItemTable(BrutalityModBlocks.GRAY_OFFICE_RUG.get()));
        this.add(BrutalityModBlocks.LIGHT_GRAY_OFFICE_RUG.get(), block -> this.createSingleItemTable(BrutalityModBlocks.LIGHT_GRAY_OFFICE_RUG.get()));

        this.add(BrutalityModBlocks.WHITE_FILING_CABINET.get(), block -> this.createSingleItemTable(BrutalityModItems.WHITE_FILING_CABINET_ITEM.get()));
        this.add(BrutalityModBlocks.LIGHT_GRAY_FILING_CABINET.get(), block -> this.createSingleItemTable(BrutalityModItems.LIGHT_GRAY_FILING_CABINET_ITEM.get()));
        this.add(BrutalityModBlocks.GRAY_FILING_CABINET.get(), block -> this.createSingleItemTable(BrutalityModItems.GRAY_FILING_CABINET_ITEM.get()));


        this.add(BrutalityModBlocks.IMPORTANT_DOCUMENTS_BLOCK.get(), (block) ->
                LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(this.applyExplosionDecay(Blocks.SEA_PICKLE, LootItem.lootTableItem(block).apply(List.of(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16),
                                (integer) -> SetItemCountFunction.setCount(ConstantValue.exactly((float) integer))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ImportantDocumentsBlock.PAPERS, integer))))))));

        BrutalityModBlocks.CONCRETE_SLABS.forEach(registryObject -> this.add(registryObject.get(), this::createSlabItemTable));
        BrutalityModBlocks.CONCRETE_STAIRS.forEach(registryObject -> this.add(registryObject.get(), this::createSingleItemTable));

    }

    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock, this.applyExplosionDecay(pBlock,
                LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BrutalityModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }


}
