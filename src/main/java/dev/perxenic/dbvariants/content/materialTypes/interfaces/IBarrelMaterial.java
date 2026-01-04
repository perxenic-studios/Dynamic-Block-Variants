package dev.perxenic.dbvariants.content.materialTypes.interfaces;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import dev.perxenic.dbvariants.content.blocks.barrel.DynamicBarrel;
import dev.perxenic.dbvariants.content.blocks.barrel.DynamicBarrelBlockEntity;
import dev.perxenic.dbvariants.infra.MaterialSuffixDefinition;
import dev.perxenic.dbvariants.registry.DBVBlocks;
import dev.perxenic.dbvariants.registry.store.MaterialStore;
import dev.perxenic.dbvariants.util.material.BarrelMaterialHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface IBarrelMaterial extends IMaterial {
    String SUFFIX = MaterialStore.registerSuffix("barrel", new MaterialSuffixDefinition<>(
            BarrelMaterialHelper::getDefaultMaterial
    ));

    @Override
    MapCodec<? extends IBarrelMaterial> codec();

    default void renderBarrel(
            @NotNull DynamicBarrelBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            boolean isItem
    ) {
        Level level = blockEntity.getLevel();
        boolean levelPresent = level != null;
        BlockState blockstate = levelPresent ? blockEntity.getBlockState() : DBVBlocks.DYNAMIC_BARREL.get().defaultBlockState().setValue(DynamicBarrel.FACING, Direction.UP);

        stack.pushPose();

        stack.translate(0.5F, 0.5F, 0.5F);
        stack.mulPose(blockstate.getValue(DynamicBarrel.FACING).getRotation());
        stack.translate(-0.5F, -0.5F, -0.5F);

        renderBody(
                blockEntity,
                partialTick,
                stack,
                bufferSource,
                packedLight,
                packedOverlay,
                isItem
        );

        renderReinforcement(
                blockEntity,
                partialTick,
                stack,
                bufferSource,
                packedLight,
                packedOverlay,
                isItem
        );

        stack.popPose();
    }

    default void render(
            @NotNull BlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            boolean isItem
    ) {
        if (!(blockEntity instanceof DynamicBarrelBlockEntity barrelBlockEntity)) return;

        renderBarrel(
                barrelBlockEntity,
                partialTick,
                stack,
                bufferSource,
                packedLight,
                packedOverlay,
                isItem
        );
    }

    void renderBody(
            @NotNull DynamicBarrelBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            boolean isItem
    );

    void renderReinforcement(
            @NotNull DynamicBarrelBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            boolean isItem
    );
}
