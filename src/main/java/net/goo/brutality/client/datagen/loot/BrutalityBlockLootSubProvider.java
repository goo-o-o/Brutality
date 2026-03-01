package net.goo.brutality.client.datagen.loot;

import net.goo.brutality.common.block.custom.ImportantDocumentsBlock;
import net.goo.brutality.common.registry.BrutalityBlockFamilies;
import net.goo.brutality.common.registry.BrutalityBlocks;
import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class BrutalityBlockLootSubProvider extends BlockLootSubProvider {
    public BrutalityBlockLootSubProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(BrutalityBlockFamilies.SOLIDIFIED_MANA.getBaseBlock());
        BrutalityBlockFamilies.SOLIDIFIED_MANA.getVariants().forEach((variant, block) -> {
            if (variant == BlockFamily.Variant.SLAB) {
                this.add(block, this::createSlabItemTable);
            } else if (variant == BlockFamily.Variant.DOOR) {
                this.add(block, this::createDoorTable);
            } else {
                this.dropSelf(block);
            }
        });


        this.add(BrutalityBlocks.WATER_COOLER_BLOCK.get(), block -> this.createNameableBlockEntityTable(BrutalityBlocks.WATER_COOLER_BLOCK.get()));
        this.add(BrutalityBlocks.COFFEE_MACHINE_BLOCK.get(), block -> this.createNameableBlockEntityTable(BrutalityBlocks.COFFEE_MACHINE_BLOCK.get()));
        this.add(BrutalityBlocks.SUPER_SNIFFER_FIGURE_BLOCK.get(), block -> this.createNameableBlockEntityTable(BrutalityBlocks.SUPER_SNIFFER_FIGURE_BLOCK.get()));

        this.add(BrutalityBlocks.MANA_CANDLE.get(), this::createCandleDrops);
        this.add(BrutalityBlocks.MANA_CANDLE_CAKE.get(), BrutalityBlockLootSubProvider::createCandleCakeDrops);
        this.add(BrutalityBlocks.BOOKSHELF_OF_WIZARDRY.get(), (block) -> this.createSingleItemTableWithSilkTouch(block, Items.BOOK, ConstantValue.exactly(3.0F)));

        this.dropSelf(BrutalityBlocks.MANA_CRYSTAL_BLOCK.get());
        this.dropSelf(BrutalityBlocks.MANA_CAULDRON.get());

        this.add(BrutalityBlocks.MANA_CRYSTAL_CLUSTER.get(), (block) -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(BrutalityItems.SOLIDIFIED_MANA.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(this.applyExplosionDecay(block, LootItem.lootTableItem(BrutalityItems.SOLIDIFIED_MANA.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
        this.dropWhenSilkTouch(BrutalityBlocks.SMALL_MANA_CRYSTAL_BUD.get());
        this.dropWhenSilkTouch(BrutalityBlocks.MEDIUM_MANA_CRYSTAL_BUD.get());
        this.dropWhenSilkTouch(BrutalityBlocks.LARGE_MANA_CRYSTAL_BUD.get());


        this.dropSelf(BrutalityBlocks.TABLE_OF_WIZARDRY.get());
        this.dropSelf(BrutalityBlocks.PEDESTAL_OF_WIZARDRY.get());
        this.dropSelf(BrutalityBlocks.OLD_SERVER_CASING.get());
        this.dropSelf(BrutalityBlocks.OLD_SERVER_PANEL.get());
        this.dropSelf(BrutalityBlocks.PLASTERBOARD.get());
        this.dropSelf(BrutalityBlocks.GREEN_CUBICLE_PANEL.get());
        this.dropSelf(BrutalityBlocks.WHITE_CUBICLE_PANEL.get());
        this.dropSelf(BrutalityBlocks.GRAY_CUBICLE_PANEL.get());
        this.dropSelf(BrutalityBlocks.OLD_AIR_CONDITIONER.get());
        this.dropSelf(BrutalityBlocks.EXIT_SIGN.get());
        this.dropSelf(BrutalityBlocks.UPPER_HVAC.get());
        this.dropSelf(BrutalityBlocks.LOWER_HVAC.get());
        this.add(BrutalityBlocks.STYROFOAM_CUP.get(), block -> this.createSingleItemTable(BrutalityItems.STYROFOAM_CUP.get()));
        this.add(BrutalityBlocks.MUG.get(), block -> this.createSingleItemTable(BrutalityItems.MUG.get()));
        this.dropSelf(BrutalityBlocks.LIGHT_GRAY_CUBICLE_PANEL.get());
        this.dropSelf(BrutalityBlocks.BLUE_CUBICLE_PANEL.get());
        this.dropSelf(BrutalityBlocks.RED_CUBICLE_PANEL.get());
        this.dropSelf(BrutalityBlocks.CRT_MONITOR.get());
        this.dropSelf(BrutalityBlocks.TOILET.get());
        this.dropSelf(BrutalityBlocks.URINAL.get());
        this.dropSelf(BrutalityBlocks.LCD_MONITOR.get());
        this.dropSelf(BrutalityBlocks.DUSTBIN.get());
        this.dropSelf(BrutalityBlocks.WHITE_OFFICE_CHAIR.get());
        this.dropSelf(BrutalityBlocks.BLACK_OFFICE_CHAIR.get());
        this.dropSelf(BrutalityBlocks.WET_FLOOR_SIGN.get());
        this.dropSelf(BrutalityBlocks.OFFICE_LIGHT.get());
        this.dropSelf(BrutalityBlocks.SMALL_OFFICE_LIGHT.get());

        this.dropSelf(BrutalityBlocks.GRAY_OFFICE_CARPET.get());
        this.dropSelf(BrutalityBlocks.LIGHT_GRAY_OFFICE_CARPET.get());
        this.dropSelf(BrutalityBlocks.GRAY_OFFICE_RUG.get());
        this.dropSelf(BrutalityBlocks.LIGHT_GRAY_OFFICE_RUG.get());

        this.add(BrutalityBlocks.WHITE_FILING_CABINET.get(), block -> this.createSingleItemTable(BrutalityItems.WHITE_FILING_CABINET_ITEM.get()));
        this.add(BrutalityBlocks.LIGHT_GRAY_FILING_CABINET.get(), block -> this.createSingleItemTable(BrutalityItems.LIGHT_GRAY_FILING_CABINET_ITEM.get()));
        this.add(BrutalityBlocks.GRAY_FILING_CABINET.get(), block -> this.createSingleItemTable(BrutalityItems.GRAY_FILING_CABINET_ITEM.get()));


        this.add(BrutalityBlocks.IMPORTANT_DOCUMENTS_BLOCK.get(), (block) ->
                LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(this.applyExplosionDecay(Blocks.SEA_PICKLE, LootItem.lootTableItem(block).apply(List.of(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16),
                                (integer) -> SetItemCountFunction.setCount(ConstantValue.exactly((float) integer))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ImportantDocumentsBlock.PAPERS, integer))))))));

        BrutalityBlocks.CONCRETE_SLABS.forEach(registryObject -> this.add(registryObject.get(), this::createSlabItemTable));
        BrutalityBlocks.CONCRETE_STAIRS.forEach(registryObject -> this.add(registryObject.get(), this::createSingleItemTable));

    }

    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock, this.applyExplosionDecay(pBlock,
                LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BrutalityBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }


}
