package dev.perxenic.dbvariants.content.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.blocks.DynamicChestBlockEntity;
import dev.perxenic.dbvariants.datagen.DBVChestMaterialProvider;
import dev.perxenic.dbvariants.registry.DBVBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DynamicChestItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public DynamicChestItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.blockEntityRenderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
    }

    @Override
    public void renderByItem(
            ItemStack stack,
            @NotNull ItemDisplayContext transform,
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        DynamicChestBlockEntity dynamicChest = new DynamicChestBlockEntity(BlockPos.ZERO, DBVBlocks.DYNAMIC_CHEST.get().defaultBlockState());

        CustomData blockEntityData = stack.getComponents().get(DataComponents.BLOCK_ENTITY_DATA);
        dynamicChest.chestMaterialLoc =  (blockEntityData == null || !blockEntityData.contains(DynamicChestBlockEntity.MATERIAL_TAG))
                ? DBVChestMaterialProvider.DEFAULT_KEY
                : ResourceLocation.parse(blockEntityData.copyTag().getString(DynamicChestBlockEntity.MATERIAL_TAG));
        dynamicChest.updateChestMaterial();
        DynamicChestRenderer renderer = (DynamicChestRenderer) blockEntityRenderDispatcher.getRenderer(dynamicChest);
        if (renderer == null) return;
        dynamicChest.chestMaterial.render(
                dynamicChest,
                0,
                poseStack,
                bufferSource,
                packedLight,
                packedOverlay,
                renderer.bottom,
                renderer.lid,
                renderer.lock,
                renderer.doubleLeftBottom,
                renderer.doubleLeftLid,
                renderer.doubleLeftLock,
                renderer.doubleRightBottom,
                renderer.doubleRightLid,
                renderer.doubleRightLock,
                true
        );
    }
}
