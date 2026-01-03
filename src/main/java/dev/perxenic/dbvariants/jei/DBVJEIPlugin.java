package dev.perxenic.dbvariants.jei;

import dev.perxenic.dbvariants.content.crafting.chest.BasicDynamicChestRecipe;
import dev.perxenic.dbvariants.registry.DBVItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IExtraIngredientRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

import static dev.perxenic.dbvariants.util.LocationHelper.dbvLoc;

@JeiPlugin
public class DBVJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return dbvLoc("main");
    }

    @Override
    public void registerExtraIngredients(IExtraIngredientRegistration registration) {
        IModPlugin.super.registerExtraIngredients(registration);

        registration.addExtraItemStacks(Set.of(DBVItems.DYNAMIC_CHEST.toStack()));
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        IModPlugin.super.registerVanillaCategoryExtensions(registration);

        registration.getCraftingCategory().addExtension(BasicDynamicChestRecipe.class, new BasicDynamicChestRecipeExtension());
    }
}
