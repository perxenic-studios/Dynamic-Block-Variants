package dev.perxenic.dbvariants.datagen;

import dev.perxenic.dbvariants.content.chestMaterialTypes.BlockOverlayChest;
import dev.perxenic.dbvariants.content.chestMaterialTypes.ChestMaterial;
import dev.perxenic.dbvariants.content.chestMaterialTypes.VanillaChest;
import dev.perxenic.dbvariants.registry.DBVRegistries;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;

import static dev.perxenic.dbvariants.DBVariants.*;

public class DBVChestMaterialProvider {

    public static final ResourceKey<ChestMaterial> DEFAULT_KEY = chestMaterialKey("default");
    public static final ChestMaterial DEFAULT = new VanillaChest(mcLoc("normal"));

    public static final ResourceKey<ChestMaterial> OAK_KEY = chestMaterialKey("oak");
    public static final ChestMaterial OAK = new BlockOverlayChest(mcLoc("oak_planks"));

    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<DatapackBuiltinEntriesProvider>) output -> new DatapackBuiltinEntriesProvider(
                        output,
                        event.getLookupProvider(),
                        new RegistrySetBuilder()
                                .add(DBVRegistries.CHEST_MATERIAL_REGISTRY_KEY, bootstrap -> {
                                    bootstrap.register(
                                            DEFAULT_KEY,
                                            DEFAULT
                                    );
                                    bootstrap.register(
                                            OAK_KEY,
                                            OAK
                                    );
                                }),
                        Set.of(MODID)
                )
        );
    }

    public static ResourceKey<ChestMaterial> chestMaterialKey(String path) {
        return ResourceKey.create(
                DBVRegistries.CHEST_MATERIAL_REGISTRY_KEY,
                dbvLoc(path)
        );
    }
}
