package dev.perxenic.dbvariants.content.blocks.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.perxenic.dbvariants.content.blocks.DynamicChestBlockEntity;
import dev.perxenic.dbvariants.content.chestMaterialTypes.ChestMaterial;
import dev.perxenic.dbvariants.datagen.DBVChestMaterialProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class DynamicChestBER implements BlockEntityRenderer<DynamicChestBlockEntity> {
    private final ModelPart bottom;
    private final ModelPart lid;
    private final ModelPart lock;
    private final ModelPart doubleLeftBottom;
    private final ModelPart doubleLeftLid;
    private final ModelPart doubleLeftLock;
    private final ModelPart doubleRightBottom;
    private final ModelPart doubleRightLid;
    private final ModelPart doubleRightLock;

    public DynamicChestBER(BlockEntityRendererProvider.Context context) {
        ModelPart chestModelPart = context.bakeLayer(ModelLayers.CHEST);
        this.bottom = chestModelPart.getChild("bottom");
        this.lid = chestModelPart.getChild("lid");
        this.lock = chestModelPart.getChild("lock");
        ModelPart doubleLeftModelPart = context.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT);
        this.doubleLeftBottom = doubleLeftModelPart.getChild("bottom");
        this.doubleLeftLid = doubleLeftModelPart.getChild("lid");
        this.doubleLeftLock = doubleLeftModelPart.getChild("lock");
        ModelPart doubleRightModelPart = context.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT);
        this.doubleRightBottom = doubleRightModelPart.getChild("bottom");
        this.doubleRightLid = doubleRightModelPart.getChild("lid");
        this.doubleRightLock = doubleRightModelPart.getChild("lock");
    }@Override
    public void render(@NotNull DynamicChestBlockEntity blockEntity, float partialTick, @NotNull PoseStack stack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ChestMaterial dynamicMaterial = blockEntity.dynamicMaterial == null ? DBVChestMaterialProvider.DEFAULT : blockEntity.dynamicMaterial.value();

        dynamicMaterial.render(
                blockEntity,
                partialTick,
                stack,
                bufferSource,
                packedLight,
                packedOverlay,
                bottom,
                lid,
                lock,
                doubleLeftBottom,
                doubleLeftLid,
                doubleLeftLock,
                doubleRightBottom,
                doubleRightLid,
                doubleRightLock
        );
    }
}
