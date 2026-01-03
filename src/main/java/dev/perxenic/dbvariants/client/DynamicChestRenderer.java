package dev.perxenic.dbvariants.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.perxenic.dbvariants.content.blocks.DynamicChestBlockEntity;
import dev.perxenic.dbvariants.content.chestMaterialTypes.ChestMaterial;
import dev.perxenic.dbvariants.datagen.DBVChestMaterialProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DynamicChestRenderer implements BlockEntityRenderer<DynamicChestBlockEntity> {
    public final ModelPart bottom;
    public final ModelPart lid;
    public final ModelPart lock;
    public final ModelPart doubleLeftBottom;
    public final ModelPart doubleLeftLid;
    public final ModelPart doubleLeftLock;
    public final ModelPart doubleRightBottom;
    public final ModelPart doubleRightLid;
    public final ModelPart doubleRightLock;

    public DynamicChestRenderer(BlockEntityRendererProvider.Context context) {
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
        ChestMaterial dynamicMaterial = blockEntity.chestMaterial == null ? DBVChestMaterialProvider.DEFAULT : blockEntity.chestMaterial;

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
                doubleRightLock,
                false
        );
    }
}
