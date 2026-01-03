package dev.perxenic.dbvariants;

import dev.perxenic.dbvariants.content.items.DBVItemExtensions;
import dev.perxenic.dbvariants.registry.store.ChestMaterialStore;
import dev.perxenic.dbvariants.registry.DBVItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber
@Mod(value = DBVariants.MODID, dist = Dist.CLIENT)
public class DBVariantsClient {
    public DBVariantsClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(
                new DBVItemExtensions(),
                DBVItems.DYNAMIC_CHEST
        );
    }

    @SubscribeEvent
    public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new ChestMaterialStore());
    }
}
