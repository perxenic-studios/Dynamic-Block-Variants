package dev.perxenic.dbvariants.content.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class DynamicChestRecipeSerializer implements RecipeSerializer<DynamicChestRecipe> {
    public static final MapCodec<DynamicChestRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            CraftingBookCategory.CODEC.fieldOf("category").forGetter(DynamicChestRecipe::category),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
            ResourceLocation.CODEC.fieldOf("material").forGetter(recipe -> recipe.material)
    ).apply(inst, DynamicChestRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DynamicChestRecipe> STREAM_CODEC = StreamCodec.composite(
            CraftingBookCategory.STREAM_CODEC, DynamicChestRecipe::category,
            Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.ingredient,
            ResourceLocation.STREAM_CODEC, recipe -> recipe.material,
            DynamicChestRecipe::new
    );


    @Override
    public @NotNull MapCodec<DynamicChestRecipe> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, DynamicChestRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
