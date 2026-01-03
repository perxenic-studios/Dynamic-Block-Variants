package dev.perxenic.dbvariants.content.items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class DBVItemExtensions implements IClientItemExtensions {
    private final DBVItemRenderer itemRenderer = new DBVItemRenderer();

    @Override
    public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return itemRenderer;
    }
}
