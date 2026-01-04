package dev.perxenic.dbvariants.content.blocks.chest;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.perxenic.dbvariants.content.chestMaterialTypes.interfaces.IChestMaterial;
import dev.perxenic.dbvariants.datagen.DBVChestMaterialProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class DynamicChestRenderer implements BlockEntityRenderer<DynamicChestBlockEntity> {
    public DynamicChestRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull DynamicChestBlockEntity blockEntity, float partialTick, @NotNull PoseStack stack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        IChestMaterial dynamicMaterial = blockEntity.chestMaterial == null ? DBVChestMaterialProvider.DEFAULT : blockEntity.chestMaterial;

        dynamicMaterial.renderChest(
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
