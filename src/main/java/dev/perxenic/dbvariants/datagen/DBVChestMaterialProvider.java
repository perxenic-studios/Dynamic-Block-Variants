package dev.perxenic.dbvariants.datagen;

import dev.perxenic.dbvariants.content.chestMaterialTypes.BlockOverlayChest;
import dev.perxenic.dbvariants.content.chestMaterialTypes.ChestMaterial;
import dev.perxenic.dbvariants.content.chestMaterialTypes.VanillaChest;
import dev.perxenic.dbvariants.registry.DBVRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.concurrent.CompletableFuture;

import static dev.perxenic.dbvariants.DBVariants.*;

public class DBVChestMaterialProvider extends JsonCodecProvider<ChestMaterial> {
    public static final String DIRECTORY = MODID + "/chest_material";

    public static final ResourceLocation DEFAULT_KEY = dbvLoc("default");
    public static final ChestMaterial DEFAULT = new VanillaChest(mcLoc("normal"));

    public DBVChestMaterialProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, PackOutput.Target.RESOURCE_PACK, DIRECTORY, PackType.CLIENT_RESOURCES, DBVRegistries.CHEST_MATERIAL_CODEC, lookupProvider, MODID, existingFileHelper);
    }

    @Override
    protected void gather() {
        this.unconditional(DEFAULT_KEY, DEFAULT);
    }
}
