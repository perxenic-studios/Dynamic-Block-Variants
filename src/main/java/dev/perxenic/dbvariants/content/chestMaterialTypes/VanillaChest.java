package dev.perxenic.dbvariants.content.chestMaterialTypes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.perxenic.dbvariants.content.blocks.chest.DynamicChestBlockEntity;
import dev.perxenic.dbvariants.registry.DBVBlocks;
import dev.perxenic.dbvariants.util.EntityRendererHelper;
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
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class VanillaChest extends ChestMaterial{
    public static final MapCodec<VanillaChest> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                ResourceLocation.CODEC.fieldOf("chest_name").forGetter(s -> s.chestName)
            ).apply(inst, VanillaChest::new));

    public final Material mainMaterial;
    public final Material leftMaterial;
    public final Material rightMaterial;

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
            boolean isItem
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
        VertexConsumer vertexConsumer = material.buffer(bufferSource, RenderType::entityTranslucent);

        boolean isDouble = chestType != ChestType.SINGLE;
        boolean isOffset = chestType != ChestType.LEFT;

        float width = isDouble ? 15f : 14f;
        float startX = isOffset ? 1f : 0f;

        // Chest Body
        EntityRendererHelper.drawFlippedCube(
                vertexConsumer,
                stack,
                new Vector3f(startX, 0f, 1f),
                new Vector3f(width, 10f, 14f),
                new Vector2f(0f, 19f),
                64f,
                packedOverlay,
                brightness,
                new boolean[] {
                        chestType == ChestType.LEFT,
                        chestType == ChestType.RIGHT,
                        false, false, false , false
                }
        );

        stack.translate(0f, 9/16f, 1/16f);
        stack.mulPose(Axis.XP.rotationDegrees(-lidAngle * 90f));
        stack.translate(0f, -9/16f, -1/16f);

        // Chest Lid
        EntityRendererHelper.drawFlippedCube(
                vertexConsumer,
                stack,
                new Vector3f(startX, 9f, 1f),
                new Vector3f(width, 5f, 14f),
                new Vector2f(0f, 0f),
                64f,
                packedOverlay,
                brightness,
                new boolean[] {
                        chestType == ChestType.LEFT,
                        chestType == ChestType.RIGHT,
                        false, false, false , false
                }
        );

        float lockWidth = chestType == ChestType.SINGLE ? 2f: 1f;
        float lockPos = switch (chestType) {
            case SINGLE -> 7f;
            case LEFT -> 0f;
            case RIGHT -> 15f;
        };

        // Chest Lock
        EntityRendererHelper.drawFlippedCube(
                vertexConsumer,
                stack,
                new Vector3f(lockPos, 7f, 15f),
                new Vector3f(lockWidth, 4f, 1f),
                new Vector2f(0f, 0f),
                64f,
                packedOverlay,
                brightness,
                new boolean[6]
        );

        stack.popPose();
    }

    protected Material getMaterial(ChestType chestType) {
        return switch (chestType) {
            case SINGLE -> mainMaterial;
            case LEFT -> leftMaterial;
            case RIGHT -> rightMaterial;
        };
    }
}
