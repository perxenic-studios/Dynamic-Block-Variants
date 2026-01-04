package dev.perxenic.dbvariants.datagen;

import dev.perxenic.dbvariants.content.materialTypes.VanillaBarrel;
import dev.perxenic.dbvariants.content.materialTypes.VanillaChest;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IMaterial;
import dev.perxenic.dbvariants.registry.DBVRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.concurrent.CompletableFuture;

import static dev.perxenic.dbvariants.DBVariants.MODID;
import static dev.perxenic.dbvariants.util.LocationHelper.*;

public class DBVMaterialProvider extends JsonCodecProvider<IMaterial> {
    public static final String DIRECTORY = MODID + "/material";

    public static final ResourceLocation DEFAULT_BARREL_KEY = dbvLoc("default_barrel");
    public static final VanillaBarrel DEFAULT_BARREL = new VanillaBarrel(mcLoc("barrel"));

    public static final ResourceLocation DEFAULT_CHEST_KEY = dbvLoc("default_chest");
    public static final VanillaChest DEFAULT_CHEST = new VanillaChest(mcLoc("normal"));

    public static final ResourceLocation XMAS_CHEST_KEY = dbvLoc("xmas_chest");
    public static final VanillaChest XMAS_CHEST = new VanillaChest(mcLoc("christmas"));

    public DBVMaterialProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, PackOutput.Target.RESOURCE_PACK, DIRECTORY, PackType.CLIENT_RESOURCES, DBVRegistries.MATERIAL_CODEC, lookupProvider, MODID, existingFileHelper);
    }

    @Override
    protected void gather() {
        this.unconditional(DEFAULT_BARREL_KEY, DEFAULT_BARREL);
        this.unconditional(DEFAULT_CHEST_KEY, DEFAULT_CHEST);
        this.unconditional(XMAS_CHEST_KEY, XMAS_CHEST);
    }
}
