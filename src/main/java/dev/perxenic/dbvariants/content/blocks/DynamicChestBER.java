package dev.perxenic.dbvariants.content.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.perxenic.dbvariants.DBVariants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DynamicChestBER implements BlockEntityRenderer<DynamicChestBlockEntity> {
    public DynamicChestBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull DynamicChestBlockEntity blockEntity, float partialTick, @NotNull PoseStack stack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        if (blockEntity.woodType == null) {
            DBVariants.LOGGER.error("Dynamic Chest at {} attempted to render but wood type was null", blockEntity.getBlockPos());
            return;
        }

        ItemStack itemStack = new ItemStack(BuiltInRegistries.ITEM.get(blockEntity.woodType));

        Level level = blockEntity.getLevel();
        BlockState state = level != null
                ? blockEntity.getBlockState()
                : Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);

        stack.pushPose();
        float yRot = state.getValue(ChestBlock.FACING).toYRot();
        stack.translate(0.5F, 0.5F, 0.5F);
        stack.mulPose(Axis.YP.rotationDegrees(-yRot));
        itemRenderer.renderStatic(
                itemStack,
                ItemDisplayContext.FIXED,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                stack,
                bufferSource,
                level,
                1
        );


        stack.translate(-0.5F, -0.5F, -0.5F);
        stack.popPose();
    }
}
