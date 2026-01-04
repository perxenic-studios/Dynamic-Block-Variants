package dev.perxenic.dbvariants.registry;

import dev.perxenic.dbvariants.DBVariants;
import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DBVItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DBVariants.MODID);

    public static final DeferredItem<BlockItem> DYNAMIC_BARREL = ITEMS.registerSimpleBlockItem(DBVBlocks.DYNAMIC_BARREL);
    public static final DeferredItem<BlockItem> DYNAMIC_CHEST = ITEMS.registerSimpleBlockItem(DBVBlocks.DYNAMIC_CHEST);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

