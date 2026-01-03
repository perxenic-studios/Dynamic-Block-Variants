package dev.perxenic.dbvariants.content.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * The class that stores the codecs for a {@link BasicDynamicChestRecipe}
 */
public class BasicDynamicChestRecipeSerializer implements RecipeSerializer<BasicDynamicChestRecipe> {
    /**
     * The {@link MapCodec} for the {@link BasicDynamicChestRecipe}, mainly used for parsing JSON
     */
    public static final MapCodec<BasicDynamicChestRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.fieldOf("group").forGetter(BasicDynamicChestRecipe::getGroup),
            CraftingBookCategory.CODEC.fieldOf("category").forGetter(BasicDynamicChestRecipe::category),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(BasicDynamicChestRecipe::getIngredient)
    ).apply(inst, BasicDynamicChestRecipe::new));

    /**
     * The {@link StreamCodec} for the {@link BasicDynamicChestRecipe}, used for parsing streams
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, BasicDynamicChestRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, BasicDynamicChestRecipe::getGroup,
            CraftingBookCategory.STREAM_CODEC, BasicDynamicChestRecipe::category,
            Ingredient.CONTENTS_STREAM_CODEC, BasicDynamicChestRecipe::getIngredient,
            BasicDynamicChestRecipe::new
    );

    /**
     * Returns {@link BasicDynamicChestRecipeSerializer#CODEC}
     * @return The {@link MapCodec} for the {@link BasicDynamicChestRecipe}, mainly used for parsing JSON
     */
    @Override
    public @NotNull MapCodec<BasicDynamicChestRecipe> codec() {
        return CODEC;
    }

    /**
     * Returns {@link BasicDynamicChestRecipeSerializer#STREAM_CODEC}
     * @return The {@link StreamCodec} for the {@link BasicDynamicChestRecipe}, used for parsing streams
     */
    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, BasicDynamicChestRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
