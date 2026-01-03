package dev.perxenic.dbvariants.datagen;

import dev.perxenic.dbvariants.content.crafting.chest.BasicDynamicChestRecipeBuilder;
import dev.perxenic.dbvariants.registry.DBVItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static dev.perxenic.dbvariants.util.LocationHelper.dbvLoc;

public class DBVRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public DBVRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        new BasicDynamicChestRecipeBuilder(
                RecipeCategory.BUILDING_BLOCKS,
                Ingredient.of(ItemTags.PLANKS)
        )
                .group("dynamic_blocks")
                .save(recipeOutput, dbvLoc("default_dynamic_chest"));

        new ShapelessRecipeBuilder(
                RecipeCategory.BUILDING_BLOCKS,
                new ItemStack(Items.CHEST)
                )
                .requires(DBVItems.DYNAMIC_CHEST)
                .group("undynamic")
                .unlockedBy("has_dynamic_chest", has(DBVItems.DYNAMIC_CHEST))
                .save(recipeOutput, dbvLoc("chest_from_dynamic"));
    }
}
