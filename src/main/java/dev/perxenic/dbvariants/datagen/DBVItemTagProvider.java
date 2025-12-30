package dev.perxenic.dbvariants.datagen;

import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.registry.DBVItems;
import dev.perxenic.dbvariants.utils.DBVItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DBVItemTagProvider extends ItemTagsProvider {

    public DBVItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, DBVariants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Common Tags
        tag(Tags.Items.CHESTS_WOODEN)
                .add(DBVItems.DYNAMIC_CHEST.get());
        // DBV Tags
        tag(DBVItemTags.CHEST_MATERIAL)
                .addTag(ItemTags.PLANKS);
    }
}
