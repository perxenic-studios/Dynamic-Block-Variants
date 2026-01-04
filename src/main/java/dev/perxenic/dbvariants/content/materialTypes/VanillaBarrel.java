package dev.perxenic.dbvariants.content.materialTypes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.blocks.barrel.DynamicBarrel;
import dev.perxenic.dbvariants.content.blocks.barrel.DynamicBarrelBlockEntity;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IBarrelMaterial;
import dev.perxenic.dbvariants.util.EntityRendererHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.RenderTypeHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static dev.perxenic.dbvariants.util.material.BarrelMaterialHelper.*;

public class VanillaBarrel implements IBarrelMaterial {
    public static final MapCodec<VanillaBarrel> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    ResourceLocation.CODEC.fieldOf("barrel_name").forGetter(s -> s.barrelName)
            ).apply(inst, VanillaBarrel::new));

    public final ResourceLocation barrelName;

    public VanillaBarrel(ResourceLocation barrelName) {
        this.barrelName = barrelName;
    }

    @Override
    public MapCodec<? extends IBarrelMaterial> codec() {
        return CODEC;
    }

    @Override
    public void renderBarrel(@NotNull DynamicBarrelBlockEntity blockEntity, float partialTick, @NotNull PoseStack stack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean isItem) {
        // Vanilla barrel doesn't have reinforcements, only render barrel
        renderBody(blockEntity, partialTick, stack, bufferSource, packedLight, packedOverlay, isItem);
    }

    @Override
    public void renderBody(@NotNull DynamicBarrelBlockEntity blockEntity, float partialTick, @NotNull PoseStack stack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean isItem) {
        stack.pushPose();

        Material mainMaterial = newMainMaterial(barrelName);
        VertexConsumer mainVertexConsumer = mainMaterial.buffer(bufferSource, RenderType::entitySolid);

        EntityRendererHelper.drawInsetCube(
                mainVertexConsumer,
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

        Material bottomMaterial = newBottomMaterial(barrelName);
        VertexConsumer bottomVertexConsumer = bottomMaterial.buffer(bufferSource, RenderType::entitySolid);

        EntityRendererHelper.drawNegYQuad(
                bottomVertexConsumer,
                stack,
                new Vector3f(0f),
                new Vector2f(16f),
                new Vector2f(0f),
                16f,
                packedLight,
                packedOverlay,
                0xFFFFFFFF
        );

        Material topMaterial = newTopMaterial(barrelName);
        VertexConsumer topVertexConsumer = topMaterial.buffer(bufferSource, RenderType::entitySolid);

        EntityRendererHelper.drawYQuad(
                topVertexConsumer,
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
        // Do nothing, vanilla barrel doesn't have separate reinforcement textures
    }
}
