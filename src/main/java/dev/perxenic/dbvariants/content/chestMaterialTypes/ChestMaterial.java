package dev.perxenic.dbvariants.content.chestMaterialTypes;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public abstract class ChestMaterial {
    public static final ResourceLocation CHEST_SHEET = ResourceLocation.withDefaultNamespace("textures/atlas/chest.png");

    public abstract MapCodec<? extends ChestMaterial> codec();

    public abstract Material getMainMaterial();
    public abstract Material getLeftMaterial();
    public abstract Material getRightMaterial();

    protected static ResourceLocation newMainLoc(ResourceLocation chestName) {
        return ResourceLocation.fromNamespaceAndPath(
                chestName.getNamespace(),
                "entity/chest/" + chestName.getPath()
        );
    }

    protected static Material newMainMaterial(ResourceLocation chestName) {
        return new Material(CHEST_SHEET, newMainLoc(chestName));
    }

    protected static ResourceLocation newLeftLoc(ResourceLocation chestName) {
        return ResourceLocation.fromNamespaceAndPath(
                chestName.getNamespace(),
                "entity/chest/" + chestName.getPath() + "_left"
        );
    }

    protected static Material newLeftMaterial(ResourceLocation chestName) {
        return new Material(CHEST_SHEET, newLeftLoc(chestName));
    }

    protected static ResourceLocation newRightLoc(ResourceLocation chestName) {
        return ResourceLocation.fromNamespaceAndPath(
                chestName.getNamespace(),
                "entity/chest/" + chestName.getPath() + "_right"
        );
    }

    protected static Material newRightMaterial(ResourceLocation chestName) {
        return new Material(CHEST_SHEET, newRightLoc(chestName));
    }
}
