package dev.perxenic.dbvariants.content.materialTypes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.blocks.barrel.DynamicBarrel;
import dev.perxenic.dbvariants.content.blocks.barrel.DynamicBarrelBlockEntity;
import dev.perxenic.dbvariants.content.blocks.chest.DynamicChestBlockEntity;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IBarrelMaterial;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IChestMaterial;
import dev.perxenic.dbvariants.registry.DBVBlocks;
import dev.perxenic.dbvariants.util.EntityRendererHelper;
import dev.perxenic.dbvariants.util.material.BarrelMaterialHelper;
import dev.perxenic.dbvariants.util.material.ChestMaterialHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static dev.perxenic.dbvariants.util.LocationHelper.BLOCK_SHEET;
import static dev.perxenic.dbvariants.util.LocationHelper.dbvLoc;

public class BlockOverlay implements IChestMaterial, IBarrelMaterial {
    public static final MapCodec<BlockOverlay> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    ResourceLocation.CODEC.fieldOf("block_name").forGetter(s -> s.blockTexture),
                    Codec.INT.fieldOf("color").forGetter(s -> s.color)
            ).apply(inst, BlockOverlay::new));

    public final Material blockMaterial;

    public final Material reinforcementMaterial;
    public final Material mainBarrelOverlayMaterial;
    public final Material bottomBarrelOverlayMaterial;
    public final Material topBarrelOverlayMaterial;
    public final Material topOpenBarrelOverlayMaterial;

    public final Material mainChestOverlayMaterial;
    public final Material leftChestOverlayMaterial;
    public final Material rightChestOverlayMaterial;

    public final ResourceLocation blockTexture;
    public final int color;

    public BlockOverlay(ResourceLocation blockName, int color) {
        this.blockTexture = blockName;
        this.color = color;
        this.blockMaterial = new Material(BLOCK_SHEET, blockName);
        this.reinforcementMaterial = new Material(BLOCK_SHEET, dbvLoc("block/barrel/grayscale_reinforcement"));

        ResourceLocation barrelOverlayLocation = dbvLoc("barrel/overlay");
        this.mainBarrelOverlayMaterial = BarrelMaterialHelper.newMainMaterial(barrelOverlayLocation);
        this.bottomBarrelOverlayMaterial = BarrelMaterialHelper.newBottomMaterial(barrelOverlayLocation);
        this.topBarrelOverlayMaterial = BarrelMaterialHelper.newTopMaterial(barrelOverlayLocation);
        this.topOpenBarrelOverlayMaterial = BarrelMaterialHelper.newTopOpenMaterial(barrelOverlayLocation);

        ResourceLocation chestOverlayLocation = dbvLoc("overlay");
        this.mainChestOverlayMaterial = ChestMaterialHelper.newMainMaterial(chestOverlayLocation);
        this.leftChestOverlayMaterial = ChestMaterialHelper.newLeftMaterial(chestOverlayLocation);
        this.rightChestOverlayMaterial = ChestMaterialHelper.newRightMaterial(chestOverlayLocation);
    }

    @Override
    public MapCodec<? extends BlockOverlay> codec() {
        return CODEC;
    }

    @Override
    public void render(@NotNull BlockEntity blockEntity, float partialTick, @NotNull PoseStack stack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean isItem) {
        if (blockEntity instanceof DynamicBarrelBlockEntity barrelBlockEntity) {
            renderBarrel(barrelBlockEntity, partialTick, stack, bufferSource, packedLight, packedOverlay, isItem);
        } else if (blockEntity instanceof DynamicChestBlockEntity chestBlockEntity) {
            renderChest(chestBlockEntity, partialTick, stack, bufferSource, packedLight, packedOverlay, isItem);
        }
    }

    @Override
    public void renderBody(@NotNull DynamicBarrelBlockEntity blockEntity, float partialTick, @NotNull PoseStack stack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean isItem) {
        stack.pushPose();

        VertexConsumer mainVertexConsumer = blockMaterial.buffer(bufferSource, RenderType::entitySolid);

        EntityRendererHelper.drawInsetCube(
                mainVertexConsumer,
                stack,
                new Vector3f(0f),
                new Vector3f(16f),
                packedOverlay,
                packedLight,
                new boolean[6]
        );

        if (isItem) {
            stack.translate(0.5F, 0.5F, 0.5F);
            stack.mulPose(new Matrix4f().scale(1.01f));
            stack.translate(-0.5F, -0.5F, -0.5F);
        }

        VertexConsumer mainOverlayVertexConsumer = mainBarrelOverlayMaterial.buffer(bufferSource, RenderType::entityTranslucent);

        EntityRendererHelper.drawInsetCube(
                mainOverlayVertexConsumer,
                stack,
                new Vector3f(0f),
                new Vector3f(16f),
                packedOverlay,
                packedLight,
                // Disable top and bottom for main texture
                new boolean[]{
                        false, false, true, true, false, false
                }
        );

        VertexConsumer bottomOverlayVertexConsumer = bottomBarrelOverlayMaterial.buffer(bufferSource, RenderType::entityTranslucent);

        EntityRendererHelper.drawNegYQuad(
                bottomOverlayVertexConsumer,
                stack,
                new Vector3f(0f),
                new Vector2f(16f),
                new Vector2f(0f),
                16f,
                packedLight,
                packedOverlay,
                0xFFFFFFFF
        );

        VertexConsumer topOverlayVertexConsumer = blockEntity.getBlockState().getValue(DynamicBarrel.OPEN)
                ? topOpenBarrelOverlayMaterial.buffer(bufferSource, RenderType::entityTranslucent)
                : topBarrelOverlayMaterial.buffer(bufferSource, RenderType::entityTranslucent);


        EntityRendererHelper.drawYQuad(
                topOverlayVertexConsumer,
                stack,
                new Vector3f(0f, 16f, 0f),
                new Vector2f(16f),
                new Vector2f(0f),
                16f,
                packedLight,
                packedOverlay,
                0xFFFFFFFF
        );

        stack.popPose();
    }

    @Override
    public void renderReinforcement(@NotNull DynamicBarrelBlockEntity blockEntity, float partialTick, @NotNull PoseStack stack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean isItem) {
        stack.pushPose();

        if (isItem) {
            stack.translate(0.5F, 0.5F, 0.5F);
            stack.mulPose(new Matrix4f().scale(1.02f));
            stack.translate(-0.5F, -0.5F, -0.5F);
        } else {
            stack.translate(0.5F, 0.5F, 0.5F);
            stack.mulPose(new Matrix4f().scale(1.02f));
            stack.translate(-0.5F, -0.5F, -0.5F);
        }

        VertexConsumer mainVertexConsumer = reinforcementMaterial.buffer(bufferSource, RenderType::entityCutout);

        EntityRendererHelper.drawInsetCube(
                mainVertexConsumer,
                stack,
                new Vector3f(0f),
                new Vector3f(16f),
                packedOverlay,
                packedLight,
                // Reinforcement not drawn on top or bottom
                new boolean[] {
                        false, false, true, true, false, false
                },
                color
        );

        stack.popPose();
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

        VertexConsumer blockVertexConsumer = blockMaterial.buffer(bufferSource, RenderType::entitySolid);

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

        VertexConsumer blockVertexConsumer = blockMaterial.buffer(bufferSource, RenderType::entitySolid);

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
        VertexConsumer vertexConsumer = material.buffer(bufferSource, RenderType::entitySolid);

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
            case SINGLE -> mainChestOverlayMaterial;
            case LEFT -> leftChestOverlayMaterial;
            case RIGHT -> rightChestOverlayMaterial;
        };
    }
}
