package dev.perxenic.dbvariants.content.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.perxenic.dbvariants.content.chestMaterialTypes.ChestMaterial;
import dev.perxenic.dbvariants.datagen.DBVChestMaterialProvider;
import dev.perxenic.dbvariants.registry.DBVBlocks;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.NotNull;

public class DynamicChestBER implements BlockEntityRenderer<DynamicChestBlockEntity> {
    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart lock;
    private final ModelPart doubleLeftLid;
    private final ModelPart doubleLeftBottom;
    private final ModelPart doubleLeftLock;
    private final ModelPart doubleRightLid;
    private final ModelPart doubleRightBottom;
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
    }

    @Override
    public void render(@NotNull DynamicChestBlockEntity blockEntity, float partialTick, @NotNull PoseStack stack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ChestMaterial dynamicMaterial = blockEntity.dynamicMaterial == null ? DBVChestMaterialProvider.DEFAULT : blockEntity.dynamicMaterial.value();

        Level level = blockEntity.getLevel();
        boolean levelPresent = level != null;

        BlockState blockstate = levelPresent ? blockEntity.getBlockState() : DBVBlocks.DYNAMIC_CHEST.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        ChestType chestType = blockstate.hasProperty(ChestBlock.TYPE) ? blockstate.getValue(ChestBlock.TYPE) : ChestType.SINGLE;

        if (!(blockstate.getBlock() instanceof AbstractChestBlock<?> abstractchestblock)) return;

        stack.pushPose();

        float yRot = blockstate.getValue(ChestBlock.FACING).toYRot();
        stack.translate(0.5F, 0.5F, 0.5F);
        stack.mulPose(Axis.YP.rotationDegrees(-yRot));
        stack.translate(-0.5F, -0.5F, -0.5F);

        DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> neighborcombineresult;
        if (levelPresent) {
            neighborcombineresult = abstractchestblock.combine(blockstate, level, blockEntity.getBlockPos(), true);
        } else {
            neighborcombineresult = DoubleBlockCombiner.Combiner::acceptNone;
        }

        float lidAngle = 1.0F - neighborcombineresult.apply(ChestBlock.opennessCombiner(blockEntity)).get(partialTick);
        lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
        int i = neighborcombineresult.apply(new BrightnessCombiner<>()).applyAsInt(packedLight);
        Material material = getMaterial(dynamicMaterial, chestType);
        VertexConsumer vertexconsumer = material.buffer(bufferSource, RenderType::entityCutout);

        switch (chestType) {
            case SINGLE -> render(stack, vertexconsumer, this.lid, this.lock, this.bottom, lidAngle, i, packedOverlay);
            case LEFT -> render(stack, vertexconsumer, this.doubleLeftLid, this.doubleLeftLock, this.doubleLeftBottom, lidAngle, i, packedOverlay);
            case RIGHT -> render(stack, vertexconsumer, this.doubleRightLid, this.doubleRightLock, this.doubleRightBottom, lidAngle, i, packedOverlay);
        }

        stack.popPose();
    }

    private void render(
            PoseStack poseStack,
            VertexConsumer consumer,
            ModelPart lidPart,
            ModelPart lockPart,
            ModelPart bottomPart,
            float lidAngle,
            int packedLight,
            int packedOverlay
    ) {
        lidPart.xRot = -(lidAngle * (float) (Math.PI / 2));
        lockPart.xRot = lidPart.xRot;
        lidPart.render(poseStack, consumer, packedLight, packedOverlay);
        lockPart.render(poseStack, consumer, packedLight, packedOverlay);
        bottomPart.render(poseStack, consumer, packedLight, packedOverlay);
    }

    protected Material getMaterial(ChestMaterial material, ChestType chestType) {
        return switch (chestType) {
            case SINGLE -> material.getMainMaterial();
            case LEFT -> material.getLeftMaterial();
            case RIGHT -> material.getRightMaterial();
        };
    }
}
