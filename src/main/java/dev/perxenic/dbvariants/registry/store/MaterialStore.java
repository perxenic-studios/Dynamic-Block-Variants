package dev.perxenic.dbvariants.registry.store;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import dev.perxenic.dbvariants.Config;
import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.materialTypes.BlockOverlay;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IChestMaterial;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IMaterial;
import dev.perxenic.dbvariants.datagen.DBVMaterialProvider;
import dev.perxenic.dbvariants.registry.DBVRegistries;
import dev.perxenic.dbvariants.util.LocationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

/**
 * A clientside cache for IMaterials to be rendered by dynamic blocks
 * @see MaterialStore#getMaterialSafe(ResourceLocation)
 */
public class MaterialStore implements ResourceManagerReloadListener {
    /**
     * A Hashmap to cache all chest materials loaded from resources or generated from the default
     */
    private static final HashMap<ResourceLocation, IMaterial> MATERIALS = new HashMap<>();

    /**
     * Ran on client resources reload, clears chest material cache and loads chest materials from resources
     * @param resourceManager The {@link ResourceManager} instance to load resources with
     */
    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        MATERIALS.clear();
        for (Map.Entry<ResourceLocation, Resource> entry : resourceManager.listResources(DBVMaterialProvider.DIRECTORY,
                location -> location.getPath().endsWith(".json")).entrySet()) {
            IMaterial material = parseMaterial(entry);
            if (material != null) MATERIALS.put(LocationHelper.getFileID(entry.getKey()), material);
        }
    }

    /**
     * Parses chest material from resources map
     * @param entry A map entry returned by {@link ResourceManager#listResources(String, Predicate)}
     * @return The {@link IMaterial} instance decoded from JSON
     * @see MaterialStore#getMaterialSafe(ResourceLocation)
     */
    private static @Nullable IMaterial parseMaterial(Map.Entry<ResourceLocation, Resource> entry) {
        try (BufferedReader reader = entry.getValue().openAsReader()) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            return DBVRegistries.MATERIAL_CODEC.parse(JsonOps.INSTANCE, jsonElement).getOrThrow();
        } catch (IOException | IllegalStateException e) {
            DBVariants.LOGGER.error("Failed to parse chest material {} with exception {}", entry.getKey(), e);
        }

        return null;
    }

    /**
     * Gets the resource location of the first texture for a block with given resource location
     * @param location The resource location of the block to get the texture for
     * @return The resource location of the first texture
     */
    @SuppressWarnings("deprecation") // Can be safely ignored due to being in minecraft core rendering
    public static @Nullable ResourceLocation textureFromBlock(ResourceLocation location) {
        BlockState blockState = BuiltInRegistries.BLOCK.get(location).defaultBlockState();
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState);
        // Create random with seed of 42 like vanilla renderer
        RandomSource random = RandomSource.create();
        random.setSeed(42L);
        List<BakedQuad> bakedQuads = model.getQuads(blockState, Direction.NORTH, random);

        // Try to get a name from each of the quads in the texture, return null if not found
        for (BakedQuad quad : bakedQuads) {
            try (SpriteContents contents = quad.getSprite().contents()) {
                return contents.name();
            }
        }
        return null;
    }

    /**
     * Looks up a {@link IMaterial} in the cache and creates one from the default if it is not present
     * @param materialLoc The {@link ResourceLocation} of the material to lookup
     * @return The cached {@link IMaterial}
     */
    //TODO: Look into specifying type of material needed
    public static IMaterial getMaterialSafe(ResourceLocation materialLoc) {
        if(materialLoc == null) return DBVMaterialProvider.DEFAULT_CHEST;


        if (!MATERIALS.containsKey(materialLoc)) {
            if (Config.vanillaDefaultChestTexture) {
                // Cache material for quicker lookup next time
                MATERIALS.put(materialLoc, DBVMaterialProvider.DEFAULT_CHEST);
            } else {
                ResourceLocation texture = textureFromBlock(materialLoc);
                // Do not cache if texture is null, texture should be searched for again
                if (texture == null) {
                    DBVariants.LOGGER.warn("Block {} appears to have no textures!", materialLoc);
                    return DBVMaterialProvider.DEFAULT_CHEST;
                }
                // Cache material for quicker lookup next time
                MATERIALS.put(materialLoc, new BlockOverlay(texture));
            }
        }

        return MATERIALS.get(materialLoc);
    }

    //TODO: Document
    public static IChestMaterial getDefaultChestMaterial(ResourceLocation wantedMaterial) {
        if (Config.vanillaDefaultChestTexture) {
            return DBVMaterialProvider.DEFAULT_CHEST;
        } else {
            ResourceLocation texture = textureFromBlock(wantedMaterial);
            if (texture == null) {
                DBVariants.LOGGER.warn("Block {} appears to have no textures!", wantedMaterial);
                return DBVMaterialProvider.DEFAULT_CHEST;
            }
            return new BlockOverlay(texture);
        }
    }
}
