package dev.perxenic.dbvariants.content.materialTypes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.perxenic.dbvariants.content.blocks.chest.DynamicChestBlockEntity;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IChestMaterial;
import dev.perxenic.dbvariants.registry.DBVBlocks;
import dev.perxenic.dbvariants.util.EntityRendererHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static dev.perxenic.dbvariants.util.material.ChestMaterialHelper.*;

import static dev.perxenic.dbvariants.util.LocationHelper.dbvLoc;

public class BlockOverlay implements IChestMaterial {
    public static final MapCodec<BlockOverlay> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                ResourceLocation.CODEC.fieldOf("block_name").forGetter(s -> s.blockTexture)
            ).apply(inst, BlockOverlay::new));

    public final Material blockMaterial;
    public final Material mainOverlayMaterial;
    public final Material leftOverlayMaterial;
    public final Material rightOverlayMaterial;

    public final ResourceLocation blockTexture;

    public BlockOverlay(ResourceLocation blockName) {
        this.blockTexture = blockName;
        blockMaterial = new Material(
                ResourceLocation.withDefaultNamespace("textures/atlas/blocks.png"),
                blockName
        );
        mainOverlayMaterial = newMainMaterial(dbvLoc("overlay"));
        leftOverlayMaterial = newLeftMaterial(dbvLoc("overlay"));
        rightOverlayMaterial = newRightMaterial(dbvLoc("overlay"));
    }

    @Override
    public MapCodec<? extends IChestMaterial> codec() {
        return CODEC;
    }

    @Override
    public void renderBody(
            @NotNull DynamicChestBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int brightness,
            int packedOverlay,
            boolean isItem
    ) {
        stack.pushPose();

        Level level = blockEntity.getLevel();
        boolean levelPresent = level != null;

        BlockState blockstate = levelPresent ? blockEntity.getBlockState() : DBVBlocks.DYNAMIC_CHEST.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        ChestType chestType = blockstate.hasProperty(ChestBlock.TYPE) ? blockstate.getValue(ChestBlock.TYPE) : ChestType.SINGLE;

        Material material = getMaterial(chestType);
        VertexConsumer vertexConsumer = material.buffer(bufferSource, RenderType::entityTranslucent);

        VertexConsumer blockVertexConsumer = blockMaterial.buffer(bufferSource, RenderType::entityCutout);

        boolean isDouble = chestType != ChestType.SINGLE;
        boolean isOffset = chestType != ChestType.LEFT;

        float width = isDouble ? 15f : 14f;
        float startX = isOffset ? 1f : 0f;

        EntityRendererHelper.drawInsetCube(
                blockVertexConsumer,
                stack,
                new Vector3f(startX, 0f, 1f),
                new Vector3f(width, 10f, 14f),
                packedOverlay,
                brightness,
                new boolean[]{
                        chestType == ChestType.LEFT,
                        chestType == ChestType.RIGHT,
                        false, false, false, false
                }
        );

        if (isItem) {
            stack.translate(0.5F, 0.5F, 0.5F);
            stack.mulPose(new Matrix4f().scale(1.01f));
            stack.translate(-0.5F, -0.5F, -0.5F);
        }

        // Chest Body Overlay
        EntityRendererHelper.drawFlippedCube(
                vertexConsumer,
                stack,
                new Vector3f(startX, 0f, 1f),
                new Vector3f(width, 10f, 14f),
                new Vector2f(0f, 19f),
                64f,
                packedOverlay,
                brightness,
                new boolean[]{
                        chestType == ChestType.LEFT,
                        chestType == ChestType.RIGHT,
                        false, false, false, false
                }
        );

        stack.popPose();
    }

    @Override
    public void renderLid(
            @NotNull DynamicChestBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int brightness,
            int packedOverlay,
            boolean isItem
    ) {
        stack.pushPose();

        Level level = blockEntity.getLevel();
        boolean levelPresent = level != null;

        BlockState blockstate = levelPresent ? blockEntity.getBlockState() : DBVBlocks.DYNAMIC_CHEST.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        ChestType chestType = blockstate.hasProperty(ChestBlock.TYPE) ? blockstate.getValue(ChestBlock.TYPE) : ChestType.SINGLE;

        Material material = getMaterial(chestType);
        VertexConsumer vertexConsumer = material.buffer(bufferSource, RenderType::entityTranslucent);

        VertexConsumer blockVertexConsumer = blockMaterial.buffer(bufferSource, RenderType::entityCutout);

        boolean isDouble = chestType != ChestType.SINGLE;
        boolean isOffset = chestType != ChestType.LEFT;

        float width = isDouble ? 15f : 14f;
        float startX = isOffset ? 1f : 0f;

        // Chest Lid
        EntityRendererHelper.drawInsetCube(
                blockVertexConsumer,
                stack,
                new Vector3f(startX, 9f, 1f),
                new Vector3f(width, 5f, 14f),
                packedOverlay,
                brightness,
                new boolean[]{
                        chestType == ChestType.LEFT,
                        chestType == ChestType.RIGHT,
                        false, false, false, false
                }
        );

        if (isItem) {
            stack.translate(0.5F, 0.5F, 0.5F);
            stack.mulPose(new Matrix4f().scale(1.01f));
            stack.translate(-0.5F, -0.5F, -0.5F);
        }

        // Chest Lid Overlay
        EntityRendererHelper.drawFlippedCube(
                vertexConsumer,
                stack,
                new Vector3f(startX, 9f, 1f),
                new Vector3f(width, 5f, 14f),
                new Vector2f(0f, 0f),
                64f,
                packedOverlay,
                brightness,
                new boolean[]{
                        chestType == ChestType.LEFT,
                        chestType == ChestType.RIGHT,
                        false, false, false, false
                }
        );

        stack.popPose();
    }

    @Override
    public void renderLock(
            @NotNull DynamicChestBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int brightness,
            int packedOverlay,
            boolean isItem
    ) {
        stack.pushPose();

        Level level = blockEntity.getLevel();
        boolean levelPresent = level != null;

        BlockState blockstate = levelPresent ? blockEntity.getBlockState() : DBVBlocks.DYNAMIC_CHEST.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        ChestType chestType = blockstate.hasProperty(ChestBlock.TYPE) ? blockstate.getValue(ChestBlock.TYPE) : ChestType.SINGLE;

        Material material = getMaterial(chestType);
        VertexConsumer vertexConsumer = material.buffer(bufferSource, RenderType::entityTranslucent);

        float lockWidth = chestType == ChestType.SINGLE ? 2f: 1f;
        float lockPos = switch (chestType) {
            case SINGLE -> 7f;
            case LEFT -> 0f;
            case RIGHT -> 15f;
        };

        // Chest Lock from Overlay
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
            case SINGLE -> mainOverlayMaterial;
            case LEFT -> leftOverlayMaterial;
            case RIGHT -> rightOverlayMaterial;
        };
    }
}
