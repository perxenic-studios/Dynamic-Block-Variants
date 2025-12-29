package dev.perxenic.dbvariants.registry;

import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.crafting.DynamicChestRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, DBVariants.MODID);

    public static final Supplier<RecipeType<DynamicChestRecipe>> DYNAMIC_CHEST_RECIPE =
            RECIPE_TYPES.register(
                    "dynamic_chest",
                    RecipeType::simple
            );

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }
}