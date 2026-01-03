package dev.perxenic.dbvariants.content.crafting.chest;

import dev.perxenic.dbvariants.content.blocks.chest.DynamicChestBlockEntity;
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
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CustomDynamicChestRecipe extends ShapedRecipe {
    public final ResourceLocation chestMaterial;

    public CustomDynamicChestRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, boolean showNotification, ResourceLocation chestMaterial) {
        super(group, category, pattern, getResultWithMaterial(chestMaterial), showNotification);
        this.chestMaterial = chestMaterial;
    }

    public static ItemStack getResultWithMaterial(ResourceLocation chestMaterial) {
        ItemStack result = DBVItems.DYNAMIC_CHEST.toStack();

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("id", DBVBlockEntities.DYNAMIC_CHEST.getKey().location().toString());

        compoundTag.putString(DynamicChestBlockEntity.MATERIAL_TAG, chestMaterial.toString());

        result.applyComponents(
                DataComponentPatch.builder().set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(compoundTag)).build()
        );

        return result;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput craftingInput, HolderLookup.@NotNull Provider provider) {
        Optional<ItemStack> itemStack = craftingInput.items().stream().filter(filter -> !filter.isEmpty()).findFirst();
        if (itemStack.isEmpty()) return ItemStack.EMPTY;

        return getResultWithMaterial(this.chestMaterial);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return DBVRecipeSerializers.CUSTOM_DYNAMIC_CHEST_RECIPE.get();
    }

    public ResourceLocation getChestMaterial()
    {
        return chestMaterial;
    }

    public ShapedRecipePattern getPattern()
    {
        return pattern;
    }
}
