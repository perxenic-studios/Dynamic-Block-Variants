package dev.perxenic.dbvariants.content.items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

/**
 * Item extensions class to set custom renderer for items
 * @see DBVItemRenderer
 */
public class DBVItemExtensions implements IClientItemExtensions {
    /**
     * The {@link DBVItemRenderer} instance
     */
    private final DBVItemRenderer itemRenderer = new DBVItemRenderer();

    /**
     * Returns the custom renderer, used by Minecraft to render items
     * @return The {@link DBVItemRenderer} instance as a {@link BlockEntityWithoutLevelRenderer}
     */
    @Override
    public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return itemRenderer;
    }
}
