package dev.perxenic.dbvariants.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import dev.perxenic.dbvariants.Config;
import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.chestMaterialTypes.BlockOverlayChest;
import dev.perxenic.dbvariants.content.chestMaterialTypes.ChestMaterial;
import dev.perxenic.dbvariants.datagen.DBVChestMaterialProvider;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

/**
 * A clientside cache for ChestMaterials to be rendered by the Dynamic Chest
 * @see ChestMaterialStore#getChestMaterialSafe(ResourceLocation)
 */
@OnlyIn(Dist.CLIENT)
public class ChestMaterialStore implements ResourceManagerReloadListener {
    /**
     * A Hashmap to cache all chest materials loaded from resources or generated from the default
     */
    private static final HashMap<ResourceLocation, ChestMaterial> CHEST_MATERIALS = new HashMap<>();

    /**
     * Ran on client resources reload, clears chest material cache and loads chest materials from resources
     * @param resourceManager The {@link ResourceManager} instance to load resources with
     */
    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        CHEST_MATERIALS.clear();
        for (Map.Entry<ResourceLocation, Resource> entry : resourceManager.listResources(DBVChestMaterialProvider.DIRECTORY,
                location -> location.getPath().endsWith(".json")).entrySet()) {
            ChestMaterial material = parseChestMaterial(entry);
            if (material != null) CHEST_MATERIALS.put(LocationHelper.getFileID(entry.getKey()), material);
        }
    }

    /**
     * Parses chest material from resources map
     * @param entry A map entry returned by {@link ResourceManager#listResources(String, Predicate)}
     * @return The {@link ChestMaterial} instance decoded from JSON
     * @see ChestMaterialStore#getChestMaterialSafe(ResourceLocation)
     */
    private static @Nullable ChestMaterial parseChestMaterial(Map.Entry<ResourceLocation, Resource> entry) {
        try (BufferedReader reader = entry.getValue().openAsReader()) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            return DBVRegistries.CHEST_MATERIAL_CODEC.parse(JsonOps.INSTANCE, jsonElement).getOrThrow();
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
     * Looks up a {@link ChestMaterial} in the cache and creates one from the default if it is not present
     * @param chestMaterialLoc The {@link ResourceLocation} of the chest material to lookup
     * @return The cached {@link ChestMaterial}
     */
    public static ChestMaterial getChestMaterialSafe(ResourceLocation chestMaterialLoc) {
        if(chestMaterialLoc == null) return DBVChestMaterialProvider.DEFAULT;

        if (!CHEST_MATERIALS.containsKey(chestMaterialLoc)) {
            if (Config.vanillaDefaultChestTexture) {
                // Cache chest material for quicker lookup next time
                CHEST_MATERIALS.put(chestMaterialLoc, DBVChestMaterialProvider.DEFAULT);
            } else {
                ResourceLocation texture = textureFromBlock(chestMaterialLoc);
                // Do not cache if texture is null, texture should be searched for again
                if (texture == null) {
                    DBVariants.LOGGER.warn("Block {} appears to have no textures!", chestMaterialLoc);
                    return DBVChestMaterialProvider.DEFAULT;
                }
                // Cache chest material for quicker lookup next time
                CHEST_MATERIALS.put(chestMaterialLoc, new BlockOverlayChest(texture));
            }
        }

        return CHEST_MATERIALS.get(chestMaterialLoc);
    }
}
