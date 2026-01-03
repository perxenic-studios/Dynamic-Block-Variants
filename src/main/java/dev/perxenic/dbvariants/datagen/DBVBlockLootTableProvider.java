package dev.perxenic.dbvariants.datagen;

import dev.perxenic.dbvariants.registry.DBVBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class DBVBlockLootTableProvider extends BlockLootSubProvider {
    public DBVBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        add(
                DBVBlocks.DYNAMIC_CHEST.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().add(
                                LootItem.lootTableItem(DBVBlocks.DYNAMIC_CHEST)
                                        .apply(CopyComponentsFunction
                                                .copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                                .include(DataComponents.BLOCK_ENTITY_DATA)
                                                .include(DataComponents.CUSTOM_NAME)
                                        )
                        )
                )
        );
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return DBVBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
