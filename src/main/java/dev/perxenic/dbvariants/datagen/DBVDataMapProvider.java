package dev.perxenic.dbvariants.datagen;

import dev.perxenic.dbvariants.utils.DBVDataMaps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class DBVDataMapProvider extends DataMapProvider {

    protected DBVDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(DBVDataMaps.CHEST_MATERIAL)
                .add(ItemTags.PLANKS, DBVChestMaterialProvider.DEFAULT.location(), false);
    }
}
