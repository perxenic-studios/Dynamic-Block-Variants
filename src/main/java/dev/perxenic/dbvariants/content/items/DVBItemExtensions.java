package dev.perxenic.dbvariants.content.items;

import dev.perxenic.dbvariants.content.items.chest.DynamicChestItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class DVBItemExtensions implements IClientItemExtensions {
    private final DynamicChestItemRenderer itemRenderer = new DynamicChestItemRenderer();

    @Override
    public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return itemRenderer;
    }
}
