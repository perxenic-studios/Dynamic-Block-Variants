package dev.perxenic.dbvariants.content.items;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.perxenic.dbvariants.content.blocks.barrel.DynamicBarrel;
import dev.perxenic.dbvariants.content.blocks.barrel.DynamicBarrelBlockEntity;
import dev.perxenic.dbvariants.content.blocks.chest.DynamicChest;
import dev.perxenic.dbvariants.content.blocks.chest.DynamicChestBlockEntity;
import dev.perxenic.dbvariants.datagen.DBVMaterialProvider;
import dev.perxenic.dbvariants.registry.DBVBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;

/**
 * An item renderer that detects the item type and selects the correct renderer for the dynamic blocks
 */
public class DBVItemRenderer extends BlockEntityWithoutLevelRenderer {
    /**
     * The {@link BlockEntityRenderDispatcher} instance cached from the {@link Minecraft} instance
     * @see Minecraft#getBlockEntityRenderDispatcher()
     */
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public DBVItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.blockEntityRenderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
    }

    /**
     * Detects the item in the {@link ItemStack} and renders using correct method
     * @param stack The {@link ItemStack} to render for
     * @param transform The {@link ItemDisplayContext} to transform the render
     * @param poseStack The {@link PoseStack} that stores transforms
     * @param bufferSource The {@link MultiBufferSource} to render to
     * @param packedLight The light that the item should be rendered with
     * @param packedOverlay The packed overlay for the item
     */
    @Override
    public void renderByItem(
            ItemStack stack,
            @NotNull ItemDisplayContext transform,
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        if (!(stack.getItem() instanceof BlockItem item)) return;

        if (item.getBlock() instanceof DynamicChest) renderDynamicChest(
                stack,
                transform,
                poseStack,
                bufferSource,
                packedLight,
                packedOverlay
        );

        if (item.getBlock() instanceof DynamicBarrel) renderDynamicBarrel(
                stack,
                transform,
                poseStack,
                bufferSource,
                packedLight,
                packedOverlay
        );
    }

    /**
     * Renders a dynamic chest using the material renderer
     * Circumvents the block entity renderer to pass an isItem flag to get outlines to render correctly
     * @param stack The {@link ItemStack} to render for
     * @param transform The {@link ItemDisplayContext} to transform the render
     * @param poseStack The {@link PoseStack} that stores transforms
     * @param bufferSource The {@link MultiBufferSource} to render to
     * @param packedLight The light that the item should be rendered with
     * @param packedOverlay The packed overlay for the item
     */
    public void renderDynamicChest(
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
                ? DBVMaterialProvider.DEFAULT_CHEST_KEY
                : ResourceLocation.parse(blockEntityData.copyTag().getString(DynamicChestBlockEntity.MATERIAL_TAG));
        dynamicChest.updateChestMaterial();
        dynamicChest.chestMaterial.renderChest(
                dynamicChest,
                0,
                poseStack,
                bufferSource,
                packedLight,
                packedOverlay,
                true
        );
    }

    /**
     * Renders a dynamic barrel using the material renderer
     * Circumvents the block entity renderer to pass an isItem flag to get outlines to render correctly
     * @param stack The {@link ItemStack} to render for
     * @param transform The {@link ItemDisplayContext} to transform the render
     * @param poseStack The {@link PoseStack} that stores transforms
     * @param bufferSource The {@link MultiBufferSource} to render to
     * @param packedLight The light that the item should be rendered with
     * @param packedOverlay The packed overlay for the item
     */
    public void renderDynamicBarrel(
            ItemStack stack,
            @NotNull ItemDisplayContext transform,
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        DynamicBarrelBlockEntity dynamicChest = new DynamicBarrelBlockEntity(BlockPos.ZERO, DBVBlocks.DYNAMIC_BARREL.get().defaultBlockState());

        CustomData blockEntityData = stack.getComponents().get(DataComponents.BLOCK_ENTITY_DATA);
        dynamicChest.barrelMaterialLoc =  (blockEntityData == null || !blockEntityData.contains(DynamicChestBlockEntity.MATERIAL_TAG))
                ? DBVMaterialProvider.DEFAULT_BARREL_KEY
                : ResourceLocation.parse(blockEntityData.copyTag().getString(DynamicChestBlockEntity.MATERIAL_TAG));
        dynamicChest.updateBarrelMaterial();
        dynamicChest.barrelMaterial.renderBarrel(
                dynamicChest,
                0,
                poseStack,
                bufferSource,
                packedLight,
                packedOverlay,
                true
        );
    }
}
