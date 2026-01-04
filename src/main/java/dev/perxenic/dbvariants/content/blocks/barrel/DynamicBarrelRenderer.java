package dev.perxenic.dbvariants.content.blocks.barrel;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IBarrelMaterial;
import dev.perxenic.dbvariants.datagen.DBVMaterialProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class DynamicBarrelRenderer implements BlockEntityRenderer<DynamicBarrelBlockEntity> {
    public DynamicBarrelRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull DynamicBarrelBlockEntity blockEntity, float partialTick, @NotNull PoseStack stack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        IBarrelMaterial dynamicMaterial = blockEntity.barrelMaterial == null ? DBVMaterialProvider.DEFAULT_BARREL : blockEntity.barrelMaterial;

        dynamicMaterial.renderBarrel(
                blockEntity,
                partialTick,
                stack,
                bufferSource,
                packedLight,
                packedOverlay,
                false
        );
    }
}
