package dev.perxenic.dbvariants.util.material;

import dev.perxenic.dbvariants.Config;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IChestMaterial;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IMaterial;
import dev.perxenic.dbvariants.datagen.DBVMaterialProvider;
import dev.perxenic.dbvariants.registry.store.MaterialStore;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class ChestMaterialHelper {
    public static final ResourceLocation CHEST_SHEET = ResourceLocation.withDefaultNamespace("textures/atlas/chest.png");

    public static IChestMaterial getDefaultMaterial(ResourceLocation wantedMaterial) {
        if (Config.vanillaDefaultChestTexture) {
            return DBVMaterialProvider.DEFAULT_CHEST;
        } else {
            IMaterial outMaterial = MaterialStore.getDefaultMaterial(wantedMaterial);
            if (outMaterial instanceof IChestMaterial chestMaterial) return chestMaterial;
            else return DBVMaterialProvider.DEFAULT_CHEST;
        }
    }

    public static ResourceLocation newMainLoc(ResourceLocation chestName) {
        return ResourceLocation.fromNamespaceAndPath(
                chestName.getNamespace(),
                "entity/chest/" + chestName.getPath()
        );
    }

    public static Material newMainMaterial(ResourceLocation chestName) {
        return new Material(CHEST_SHEET, newMainLoc(chestName));
    }

    public static ResourceLocation newLeftLoc(ResourceLocation chestName) {
        return ResourceLocation.fromNamespaceAndPath(
                chestName.getNamespace(),
                "entity/chest/" + chestName.getPath() + "_left"
        );
    }

    public static Material newLeftMaterial(ResourceLocation chestName) {
        return new Material(CHEST_SHEET, newLeftLoc(chestName));
    }

    public static ResourceLocation newRightLoc(ResourceLocation chestName) {
        return ResourceLocation.fromNamespaceAndPath(
                chestName.getNamespace(),
                "entity/chest/" + chestName.getPath() + "_right"
        );
    }

    public static Material newRightMaterial(ResourceLocation chestName) {
        return new Material(CHEST_SHEET, newRightLoc(chestName));
    }
}
