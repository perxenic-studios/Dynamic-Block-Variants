package dev.perxenic.dbvariants.content.items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class DVBItemExtensions implements IClientItemExtensions {
    private final DVBItemRenderer itemRenderer = new DVBItemRenderer();

    @Override
    public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return itemRenderer;
    }
}
