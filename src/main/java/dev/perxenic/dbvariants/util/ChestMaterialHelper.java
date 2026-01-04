package dev.perxenic.dbvariants.util;

import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class ChestMaterialHelper {
    public static final ResourceLocation CHEST_SHEET = ResourceLocation.withDefaultNamespace("textures/atlas/chest.png");

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
