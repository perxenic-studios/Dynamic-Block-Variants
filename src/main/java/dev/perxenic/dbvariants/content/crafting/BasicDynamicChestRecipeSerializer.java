package dev.perxenic.dbvariants.content.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class BasicDynamicChestRecipeSerializer implements RecipeSerializer<BasicDynamicChestRecipe> {
    public static final MapCodec<BasicDynamicChestRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            CraftingBookCategory.CODEC.fieldOf("category").forGetter(BasicDynamicChestRecipe::category),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient)
    ).apply(inst, BasicDynamicChestRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BasicDynamicChestRecipe> STREAM_CODEC = StreamCodec.composite(
            CraftingBookCategory.STREAM_CODEC, BasicDynamicChestRecipe::category,
            Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.ingredient,
            BasicDynamicChestRecipe::new
    );


    @Override
    public @NotNull MapCodec<BasicDynamicChestRecipe> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, BasicDynamicChestRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
