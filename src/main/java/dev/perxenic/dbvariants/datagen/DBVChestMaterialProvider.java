package dev.perxenic.dbvariants.datagen;

import dev.perxenic.dbvariants.content.chestMaterialTypes.ChestMaterial;
import dev.perxenic.dbvariants.content.chestMaterialTypes.VanillaChest;
import dev.perxenic.dbvariants.registry.DBVRegistries;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;

import static dev.perxenic.dbvariants.DBVariants.MODID;
import static dev.perxenic.dbvariants.DBVariants.dbvLoc;

public class DBVChestMaterialProvider {

    public static final ResourceKey<ChestMaterial> DEFAULT = chestMaterialKey("default");

    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<DatapackBuiltinEntriesProvider>) output -> new DatapackBuiltinEntriesProvider(
                        output,
                        event.getLookupProvider(),
                        new RegistrySetBuilder()
                                .add(DBVRegistries.CHEST_MATERIAL_REGISTRY_KEY, bootstrap -> {
                                    bootstrap.register(
                                            DEFAULT,
                                            new VanillaChest("default")
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
