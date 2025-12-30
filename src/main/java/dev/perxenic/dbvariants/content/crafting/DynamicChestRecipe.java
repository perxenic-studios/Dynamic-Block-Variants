package dev.perxenic.dbvariants.content.crafting;

import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.blocks.DynamicChestBlockEntity;
import dev.perxenic.dbvariants.datagen.DBVChestMaterialProvider;
import dev.perxenic.dbvariants.datagen.DBVDataMapProvider;
import dev.perxenic.dbvariants.registry.DBVBlockEntities;
import dev.perxenic.dbvariants.registry.DBVItems;
import dev.perxenic.dbvariants.registry.DBVRecipeSerializers;
import dev.perxenic.dbvariants.utils.DBVDataMaps;
import dev.perxenic.dbvariants.utils.DBVItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DynamicChestRecipe extends CustomRecipe {
    public DynamicChestRecipe(CraftingBookCategory category) {
        super(category);
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
            // If not a chest material, recipe invalid
            if (!itemStack.is(DBVItemTags.CHEST_MATERIAL)) return false;
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
        ItemStack itemStack = DBVItems.DYNAMIC_CHEST.toStack();

        Optional<ItemStack> ingredient = craftingInput.items().stream().filter(filter -> !filter.isEmpty()).findFirst();
        if (ingredient.isEmpty()) return ItemStack.EMPTY;

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("id", DBVBlockEntities.DYNAMIC_CHEST.getKey().location().toString());

        ResourceLocation chestMaterial = ingredient.get().getItemHolder().getData(DBVDataMaps.CHEST_MATERIAL);
        if (chestMaterial == null) {
            DBVariants.LOGGER.warn("Item {} has no chest material!", ingredient.get().getItem());
            chestMaterial = DBVChestMaterialProvider.DEFAULT.location();
        }

        compoundTag.putString(DynamicChestBlockEntity.MATERIAL_TAG, chestMaterial.toString());

        itemStack.applyComponents(
                DataComponentPatch.builder().set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(compoundTag)).build()
        );
        return itemStack;
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
