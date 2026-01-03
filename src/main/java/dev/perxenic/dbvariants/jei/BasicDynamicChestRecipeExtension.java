package dev.perxenic.dbvariants.jei;

import dev.perxenic.dbvariants.content.crafting.chest.BasicDynamicChestRecipe;
import dev.perxenic.dbvariants.registry.DBVItems;
import dev.perxenic.dbvariants.util.CollectionHelper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public class BasicDynamicChestRecipeExtension implements ICraftingCategoryExtension<BasicDynamicChestRecipe> {
    @Override
    public void setRecipe(RecipeHolder<BasicDynamicChestRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        Ingredient input = recipeHolder.value().ingredient;

        craftingGridHelper.createAndSetIngredients(builder, List.of(
                input, input, input,
                input, Ingredient.EMPTY, input,
                input, input, input
        ), 3, 3);

        ItemStack defaultInput = CollectionHelper.getFirstArray(input.getItems());

        craftingGridHelper.createAndSetOutputs(builder, List.of(
                defaultInput != null ? BasicDynamicChestRecipe.getStackFromInput(defaultInput) : DBVItems.DYNAMIC_CHEST.toStack()
                ));
    }

    @Override
    public int getWidth(RecipeHolder<BasicDynamicChestRecipe> recipeHolder) {
        return 3;
    }

    @Override
    public int getHeight(RecipeHolder<BasicDynamicChestRecipe> recipeHolder) {
        return 3;
    }
}
