package dev.perxenic.dbvariants.registry.store;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.materialTypes.BlockOverlay;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IMaterial;
import dev.perxenic.dbvariants.datagen.DBVMaterialProvider;
import dev.perxenic.dbvariants.infra.MaterialSuffixDefinition;
import dev.perxenic.dbvariants.registry.DBVRegistries;
import dev.perxenic.dbvariants.util.CollectionHelper;
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
 * @see MaterialStore#chooseDefaultMaterial(ResourceLocation)
 */
public class MaterialStore implements ResourceManagerReloadListener {
    /**
     * A Hashmap to cache all chest materials loaded from resources or generated from the default
     */
    private static final HashMap<ResourceLocation, IMaterial> MATERIALS = new HashMap<>();

    //TODO: Document
    private static final HashMap<String, MaterialSuffixDefinition<? extends IMaterial>> MATERIAL_SUFFIXES = new HashMap<>();

    public static String registerSuffix(String suffix, MaterialSuffixDefinition<? extends IMaterial> suffixClass) {
        // Do not allow suffixes with underscores to prevent them from incorrectly being parsed from string
        if (suffix.contains("_")) {
            DBVariants.LOGGER.error("Suffix {} contains an underscore", suffix);
            return null;
        }
        if (MATERIAL_SUFFIXES.containsKey(suffix)) {
            DBVariants.LOGGER.error("Suffix {} has already been registered!", suffix);
            return null;
        }
        MATERIAL_SUFFIXES.put(suffix, suffixClass);
        return suffix;
    }

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
     * @see MaterialStore#chooseDefaultMaterial(ResourceLocation)
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
        if (location == null) return null;
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
     * Looks up a {@link IMaterial} in the cache and creates one from the default, looking at suffix to determine type
     * @param materialLoc The {@link ResourceLocation} of the material to lookup
     * @return The cached {@link IMaterial}
     */
    public static @Nullable IMaterial chooseDefaultMaterial(ResourceLocation materialLoc) {
        if(materialLoc == null) return null;

        if (MATERIALS.containsKey(materialLoc)) return MATERIALS.get(materialLoc);

        // Get default material for suffix
        String suffix = CollectionHelper.getLastArray(materialLoc.getPath().split("_"));
        if (MATERIAL_SUFFIXES.containsKey(suffix)) {
            MATERIALS.put(materialLoc, MATERIAL_SUFFIXES.get(suffix).defaultMaterialGetter.apply(materialLoc));
            return MATERIALS.get(materialLoc);
        }

        // Suffix not found, assuming no suffix
        MATERIALS.put(materialLoc, getDefaultMaterial(materialLoc));
        return MATERIALS.get(materialLoc);
    }

    /**
     * Returns the default {@link IMaterial} for a generic dynamic block, prefer to use {@link MaterialStore#chooseDefaultMaterial(ResourceLocation)}
     * @param wantedMaterial The {@link ResourceLocation} of the material that was wanted but not found in cache
     * @return The default {@link IMaterial}
     */
    public static IMaterial getDefaultMaterial(ResourceLocation wantedMaterial) {
        ResourceLocation texture = textureFromBlock(wantedMaterial);
        if (texture == null) {
            DBVariants.LOGGER.warn("Block {} appears to have no textures!", wantedMaterial);
            return DBVMaterialProvider.DEFAULT_CHEST;
        }
        return new BlockOverlay(texture, BuiltInRegistries.BLOCK.get(wantedMaterial).defaultMapColor().col);
    }
}
