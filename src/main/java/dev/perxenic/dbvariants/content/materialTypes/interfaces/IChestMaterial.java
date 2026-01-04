package dev.perxenic.dbvariants.content.materialTypes.interfaces;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import dev.perxenic.dbvariants.content.blocks.chest.DynamicChestBlockEntity;
import dev.perxenic.dbvariants.infra.MaterialSuffixDefinition;
import dev.perxenic.dbvariants.registry.DBVBlocks;
import dev.perxenic.dbvariants.registry.store.MaterialStore;
import dev.perxenic.dbvariants.util.ChestMaterialHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface IChestMaterial extends IMaterial {
    String SUFFIX = MaterialStore.registerSuffix("chest", new MaterialSuffixDefinition<>(
            ChestMaterialHelper::getDefaultMaterial
    ));

    @Override
    MapCodec<? extends IChestMaterial> codec();

    default void renderChest(
            @NotNull DynamicChestBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            boolean isItem
    ) {
        Level level = blockEntity.getLevel();
        boolean levelPresent = level != null;
        BlockState blockstate = levelPresent ? blockEntity.getBlockState() : DBVBlocks.DYNAMIC_CHEST.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);

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

        renderBody(
                blockEntity,
                partialTick,
                stack,
                bufferSource,
                brightness,
                packedOverlay,
                isItem
        );

        stack.translate(0f, 9/16f, 1/16f);
        stack.mulPose(Axis.XP.rotationDegrees(-lidAngle * 90f));
        stack.translate(0f, -9/16f, -1/16f);

        renderLid(
                blockEntity,
                partialTick,
                stack,
                bufferSource,
                brightness,
                packedOverlay,
                isItem
        );

        renderLock(
                blockEntity,
                partialTick,
                stack,
                bufferSource,
                brightness,
                packedOverlay,
                isItem
        );

        stack.popPose();
    }

    default void render(
            @NotNull DynamicChestBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            boolean isItem
    ) {
        renderChest(
                blockEntity,
                partialTick,
                stack,
                bufferSource,
                packedLight,
                packedOverlay,
                isItem
        );
    }

    void renderBody(
            @NotNull DynamicChestBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int brightness,
            int packedOverlay,
            boolean isItem
    );

    void renderLid(
            @NotNull DynamicChestBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int brightness,
            int packedOverlay,
            boolean isItem
    );

    void renderLock(
            @NotNull DynamicChestBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int brightness,
            int packedOverlay,
            boolean isItem
    );
}
