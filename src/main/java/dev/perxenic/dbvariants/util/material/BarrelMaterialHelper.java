package dev.perxenic.dbvariants.util.material;

import dev.perxenic.dbvariants.Config;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IBarrelMaterial;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IMaterial;
import dev.perxenic.dbvariants.datagen.DBVMaterialProvider;
import dev.perxenic.dbvariants.registry.store.MaterialStore;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class BarrelMaterialHelper {
    public static final ResourceLocation BLOCK_SHEET = ResourceLocation.withDefaultNamespace("textures/atlas/blocks.png");

    public static IBarrelMaterial getDefaultMaterial(ResourceLocation wantedMaterial) {
        if (Config.vanillaDefaultBarrelTexture) {
            return DBVMaterialProvider.DEFAULT_BARREL;
        } else {
            IMaterial outMaterial = MaterialStore.getDefaultMaterial(wantedMaterial);
            if (outMaterial instanceof IBarrelMaterial barrelMaterial) return barrelMaterial;
            else return DBVMaterialProvider.DEFAULT_BARREL;
        }
    }

    public static ResourceLocation newMainLoc(ResourceLocation barrelName) {
        return barrelName.withPrefix("block/").withSuffix("_side");
    }

    public static Material newMainMaterial(ResourceLocation barrelName) {
        return new Material(BLOCK_SHEET, newMainLoc(barrelName));
    }

    public static ResourceLocation newTopLoc(ResourceLocation barrelName) {
        return barrelName.withPrefix("block/").withSuffix("_top");
    }

    public static Material newTopMaterial(ResourceLocation barrelName) {
        return new Material(BLOCK_SHEET, newTopLoc(barrelName));
    }

    public static ResourceLocation newBottomLoc(ResourceLocation barrelName) {
        return barrelName.withPrefix("block/").withSuffix("_bottom");
    }

    public static Material newBottomMaterial(ResourceLocation barrelName) {
        return new Material(BLOCK_SHEET, newBottomLoc(barrelName));
    }
}
