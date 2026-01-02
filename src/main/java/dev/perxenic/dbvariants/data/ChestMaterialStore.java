package dev.perxenic.dbvariants.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.chestMaterialTypes.ChestMaterial;
import dev.perxenic.dbvariants.datagen.DBVChestMaterialProvider;
import dev.perxenic.dbvariants.registry.DBVRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChestMaterialStore implements ResourceManagerReloadListener {
    public static final HashMap<ResourceLocation, ChestMaterial> CHEST_MATERIALS = new HashMap<>();

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        CHEST_MATERIALS.clear();
        DBVariants.LOGGER.info("Chest Material List: {}", resourceManager.listResources(DBVChestMaterialProvider.DIRECTORY,
                location -> location.getPath().endsWith(".json")));
        for (Map.Entry<ResourceLocation, Resource> entry : resourceManager.listResources(DBVChestMaterialProvider.DIRECTORY,
                location -> location.getPath().endsWith(".json")).entrySet()) {
            ChestMaterial material = parseChestMaterial(entry);
            if (material != null) CHEST_MATERIALS.put(parseLocation(entry.getKey()), material);
        }
    }

    @Nullable
    private static ChestMaterial parseChestMaterial(Map.Entry<ResourceLocation, Resource> entry) {
        try (BufferedReader reader = entry.getValue().openAsReader()) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            return DBVRegistries.CHEST_MATERIAL_CODEC.parse(JsonOps.INSTANCE, jsonElement).getOrThrow();
        } catch (IOException | IllegalStateException e) {
            DBVariants.LOGGER.error("Failed to parse chest material {} with exception {}", entry.getKey(), e);
        }

        return null;
    }

    private static ResourceLocation parseLocation(ResourceLocation location) {
        String path = location.getPath();
        // Remove the start of the path
        path = Arrays.stream(path.split("/")).toList().getLast();
        // Remove file extension
        path = path.split("\\.")[0];

        return location.withPath(path);
    }
}
