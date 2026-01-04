package dev.perxenic.dbvariants.content.materialTypes.interfaces;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public interface IMaterial {
    MapCodec<? extends IMaterial> codec();

    void render(
            @NotNull BlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            boolean isItem
    );
}
