package dev.perxenic.dbvariants;

import dev.perxenic.dbvariants.registry.ModBlockEntities;
import dev.perxenic.dbvariants.registry.ModBlocks;
import dev.perxenic.dbvariants.registry.ModItems;
import dev.perxenic.dbvariants.registry.ModRecipeSerializers;
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
        ModBlockEntities.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
