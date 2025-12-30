package dev.perxenic.dbvariants;

import dev.perxenic.dbvariants.registry.DBVBlockEntities;
import dev.perxenic.dbvariants.registry.DBVBlocks;
import dev.perxenic.dbvariants.registry.DBVItems;
import dev.perxenic.dbvariants.registry.DBVRecipeSerializers;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

@Mod(DBVariants.MODID)
public class DBVariants {
    public static final String MODID = "dbvariants";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DBVariants(IEventBus modEventBus, ModContainer modContainer) {
        DBVBlockEntities.register(modEventBus);
        DBVBlocks.register(modEventBus);
        DBVItems.register(modEventBus);
        DBVRecipeSerializers.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
