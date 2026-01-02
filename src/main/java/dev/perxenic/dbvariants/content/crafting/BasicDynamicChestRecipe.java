package dev.perxenic.dbvariants.content.crafting;

import dev.perxenic.dbvariants.content.blocks.DynamicChestBlockEntity;
import dev.perxenic.dbvariants.registry.DBVBlockEntities;
import dev.perxenic.dbvariants.registry.DBVItems;
import dev.perxenic.dbvariants.registry.DBVRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BasicDynamicChestRecipe extends CustomRecipe {

    public final Ingredient ingredient;

    public BasicDynamicChestRecipe(CraftingBookCategory category, Ingredient ingredient) {
        super(category);
        this.ingredient = ingredient;
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
        // Should consist of exactly 8 ingredients
        if (input.ingredientCount() != 8) return false;
        // Chests should be made of only one item
        if (input.stackedContents().contents.size() != 1) return false;
        int index = -1;
        int startX = -1;
        int startY = -1;
        for (ItemStack itemStack : input.items()) {
            index++;
            // Continue searching until first non-empty item found
            if (itemStack.isEmpty()) continue;
            // Test for ingredient
            if (!ingredient.test(itemStack)) return false;
            startX = index % input.width();
            startY = index / input.width();
            break;
        }
        // If starting item is too late for the pattern to match return false
        if (index + input.width() * 2 + 2 >= input.size()) return false;
        // Check all the slots
        if (input.getItem(startX+1, startY).isEmpty()) return false;
        if (input.getItem(startX+2, startY).isEmpty()) return false;
        if (input.getItem(startX, startY+1).isEmpty()) return false;
        if (input.getItem(startX+2, startY+1).isEmpty()) return false;
        if (input.getItem(startX, startY+2).isEmpty()) return false;
        if (input.getItem(startX+1, startY+2).isEmpty()) return false;
        if (input.getItem(startX+2, startY+2).isEmpty()) return false;

        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput craftingInput, HolderLookup.@NotNull Provider provider) {
        ItemStack result = DBVItems.DYNAMIC_CHEST.toStack();

        Optional<ItemStack> itemStack = craftingInput.items().stream().filter(filter -> !filter.isEmpty()).findFirst();
        if (itemStack.isEmpty()) return ItemStack.EMPTY;

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("id", DBVBlockEntities.DYNAMIC_CHEST.getKey().location().toString());

        compoundTag.putString(DynamicChestBlockEntity.MATERIAL_TAG, itemStack.get().getItem().toString());

        result.applyComponents(
                DataComponentPatch.builder().set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(compoundTag)).build()
        );
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return DBVRecipeSerializers.DYNAMIC_CHEST_RECIPE.get();
    }
}
