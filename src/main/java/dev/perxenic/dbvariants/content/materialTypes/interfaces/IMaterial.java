package dev.perxenic.dbvariants.content.materialTypes.interfaces;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import dev.perxenic.dbvariants.content.blocks.chest.DynamicChestBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.NotNull;

public interface IMaterial {
    MapCodec<? extends IMaterial> codec();

    void render(
            @NotNull DynamicChestBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            boolean isItem
    );
}
