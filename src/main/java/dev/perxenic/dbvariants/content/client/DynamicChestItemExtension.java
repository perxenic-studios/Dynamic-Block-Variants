package dev.perxenic.dbvariants.content.client;

import dev.perxenic.dbvariants.DBVariants;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class DynamicChestItemExtension implements IClientItemExtensions {
    private final DynamicChestItemRenderer itemRenderer = new DynamicChestItemRenderer();

    @Override
    public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return itemRenderer;
    }
}
