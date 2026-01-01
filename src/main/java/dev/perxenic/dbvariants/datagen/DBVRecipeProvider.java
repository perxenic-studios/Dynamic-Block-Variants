package dev.perxenic.dbvariants.datagen;

import dev.perxenic.dbvariants.content.crafting.DynamicChestRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static dev.perxenic.dbvariants.DBVariants.dbvLoc;

public class DBVRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public DBVRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        new DynamicChestRecipeBuilder(
                CraftingBookCategory.BUILDING,
                Ingredient.of(ItemTags.PLANKS),
                DBVChestMaterialProvider.DEFAULT_KEY.location()
        ).save(recipeOutput, dbvLoc("default_dynamic_chest"));
    }
}
