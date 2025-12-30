package dev.perxenic.dbvariants.datagen;

import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.registry.DBVBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DBVBlockTagProvider extends BlockTagsProvider {

    public DBVBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, DBVariants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        // Minecraft Tags
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(DBVBlocks.DYNAMIC_CHEST.get());
        tag(BlockTags.FEATURES_CANNOT_REPLACE)
                .add(DBVBlocks.DYNAMIC_CHEST.get());
        tag(BlockTags.GUARDED_BY_PIGLINS)
                .add(DBVBlocks.DYNAMIC_CHEST.get());
        tag(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)
                .add(DBVBlocks.DYNAMIC_CHEST.get());
        // Common Tags
        tag(Tags.Blocks.CHESTS_WOODEN)
                .add(DBVBlocks.DYNAMIC_CHEST.get());
    }
}
