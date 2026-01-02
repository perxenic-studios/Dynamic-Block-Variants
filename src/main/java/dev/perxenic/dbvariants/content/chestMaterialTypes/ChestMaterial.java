package dev.perxenic.dbvariants.content.chestMaterialTypes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import dev.perxenic.dbvariants.content.blocks.DynamicChestBlockEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class ChestMaterial {
    public static final ResourceLocation CHEST_SHEET = ResourceLocation.withDefaultNamespace("textures/atlas/chest.png");

    public abstract MapCodec<? extends ChestMaterial> codec();

    public abstract void render(
            @NotNull DynamicChestBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            ModelPart bottom,
            ModelPart lid,
            ModelPart lock,
            ModelPart doubleLeftBottom,
            ModelPart doubleLeftLid,
            ModelPart doubleLeftLock,
            ModelPart doubleRightBottom,
            ModelPart doubleRightLid,
            ModelPart doubleRightLock,
            boolean isItem
    );

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
