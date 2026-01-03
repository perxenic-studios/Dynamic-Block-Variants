package dev.perxenic.dbvariants.content.crafting.chest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

/**
 * The class that stores the codecs for a {@link CustomDynamicChestRecipe}
 */
public class CustomDynamicChestRecipeSerializer implements RecipeSerializer<CustomDynamicChestRecipe> {
    /**
     * The {@link MapCodec} for the {@link CustomDynamicChestRecipe}, mainly used for parsing JSON
     */
    public static final MapCodec<CustomDynamicChestRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.fieldOf("group").forGetter(CustomDynamicChestRecipe::getGroup),
            CraftingBookCategory.CODEC.fieldOf("category").forGetter(CustomDynamicChestRecipe::category),
            ShapedRecipePattern.MAP_CODEC.fieldOf("pattern").forGetter(CustomDynamicChestRecipe::getPattern),
            Codec.BOOL.fieldOf("showNotification").forGetter(CustomDynamicChestRecipe::showNotification),
            ResourceLocation.CODEC.fieldOf("material").forGetter(CustomDynamicChestRecipe::getChestMaterial)
    ).apply(inst, CustomDynamicChestRecipe::new));

    /**
     * The {@link StreamCodec} for the {@link CustomDynamicChestRecipe}, used for parsing streams
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, CustomDynamicChestRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, CustomDynamicChestRecipe::getGroup,
            CraftingBookCategory.STREAM_CODEC, CustomDynamicChestRecipe::category,
            ShapedRecipePattern.STREAM_CODEC, CustomDynamicChestRecipe::getPattern,
            ByteBufCodecs.BOOL, CustomDynamicChestRecipe::showNotification,
            ResourceLocation.STREAM_CODEC, CustomDynamicChestRecipe::getChestMaterial,
            CustomDynamicChestRecipe::new
    );

    /**
     * Returns {@link CustomDynamicChestRecipeSerializer#CODEC}
     * @return The {@link MapCodec} for the {@link CustomDynamicChestRecipe}, mainly used for parsing JSON
     */
    @Override
    public @NotNull MapCodec<CustomDynamicChestRecipe> codec() {
        return CODEC;
    }

    /**
     * Returns {@link CustomDynamicChestRecipeSerializer#STREAM_CODEC}
     * @return The {@link StreamCodec} for the {@link CustomDynamicChestRecipe}, used for parsing streams
     */
    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, CustomDynamicChestRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
