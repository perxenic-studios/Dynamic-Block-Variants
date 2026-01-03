package dev.perxenic.dbvariants.content.crafting.chest;

import dev.perxenic.dbvariants.registry.DBVItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A {@link RecipeBuilder} for a {@link BasicDynamicChestRecipe}
 */
public class BasicDynamicChestRecipeBuilder implements RecipeBuilder {
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    protected String group;

    protected final RecipeCategory category;
    protected final Ingredient ingredient;

    public BasicDynamicChestRecipeBuilder(RecipeCategory category, Ingredient ingredient) {
        this.category = category;
        this.ingredient = ingredient;
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(@NotNull String name, @NotNull Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return DBVItems.DYNAMIC_CHEST.get();
    }

    @Override
    public void save(@NotNull RecipeOutput output, @NotNull ResourceLocation location) {
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location))
                .rewards(AdvancementRewards.Builder.recipe(location))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);

        BasicDynamicChestRecipe recipe = new BasicDynamicChestRecipe(Objects.requireNonNullElse(this.group, ""), RecipeBuilder.determineBookCategory(this.category), this.ingredient);

        output.accept(location, recipe, advancement.build(location.withPrefix("recipes/")));
    }
}
