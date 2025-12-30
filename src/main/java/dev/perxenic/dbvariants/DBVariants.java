package dev.perxenic.dbvariants;

import dev.perxenic.dbvariants.registry.*;
import net.minecraft.resources.ResourceLocation;
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
        DBVChestMaterialTypes.register(modEventBus);
        DBVConditionCodecs.register(modEventBus);
        DBVItems.register(modEventBus);
        DBVRecipeSerializers.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static ResourceLocation dbvLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ResourceLocation mcLoc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }
}
