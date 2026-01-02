package dev.perxenic.dbvariants.content.chestMaterialTypes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.perxenic.dbvariants.content.blocks.DynamicChestBlockEntity;
import dev.perxenic.dbvariants.datagen.DBVChestMaterialProvider;
import dev.perxenic.dbvariants.registry.DBVBlocks;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.NotNull;

public class VanillaChest extends ChestMaterial{
    public static final MapCodec<VanillaChest> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                ResourceLocation.CODEC.fieldOf("chest_name").forGetter(s -> s.chestName)
            ).apply(inst, VanillaChest::new));

    public final Material mainMaterial;
    public final Material leftMaterial;
    public final Material rightMaterial;

    // Change to a resource location or something to allow rendering chests from different namespaces
    public final ResourceLocation chestName;

    public VanillaChest(ResourceLocation chestName) {
        this.chestName = chestName;
        mainMaterial = newMainMaterial(chestName);
        leftMaterial = newLeftMaterial(chestName);
        rightMaterial = newRightMaterial(chestName);
    }

    @Override
    public MapCodec<? extends ChestMaterial> codec() {
        return CODEC;
    }

    @Override
    public void render(
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
            ModelPart doubleRightLock
    ) {
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
        int brightness = neighborcombineresult.apply(new BrightnessCombiner<>()).applyAsInt(packedLight);
        Material material = getMaterial(chestType);
        VertexConsumer vertexconsumer = material.buffer(bufferSource, RenderType::entityCutout);

        switch (chestType) {
            case SINGLE -> render(stack, vertexconsumer, lid, lock, bottom, lidAngle, brightness, packedOverlay);
            case LEFT -> render(stack, vertexconsumer, doubleLeftLid, doubleLeftLock, doubleLeftBottom, lidAngle, brightness, packedOverlay);
            case RIGHT -> render(stack, vertexconsumer, doubleRightLid, doubleRightLock, doubleRightBottom, lidAngle, brightness, packedOverlay);
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

    protected Material getMaterial(ChestType chestType) {
        return switch (chestType) {
            case SINGLE -> mainMaterial;
            case LEFT -> leftMaterial;
            case RIGHT -> rightMaterial;
        };
    }
}
