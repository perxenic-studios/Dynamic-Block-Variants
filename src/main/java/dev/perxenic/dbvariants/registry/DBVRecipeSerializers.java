package dev.perxenic.dbvariants.registry;

import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.crafting.BasicDynamicChestRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DBVRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, DBVariants.MODID);

    public static final Supplier<BasicDynamicChestRecipeSerializer> DYNAMIC_CHEST_RECIPE =
            RECIPE_SERIALIZERS.register(
                    "dynamic_chest",
                    BasicDynamicChestRecipeSerializer::new
            );

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
