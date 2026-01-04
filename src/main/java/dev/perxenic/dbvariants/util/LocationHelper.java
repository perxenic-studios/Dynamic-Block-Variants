package dev.perxenic.dbvariants.util;

import dev.perxenic.dbvariants.DBVariants;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;

/**
 * Helper class to provide utilities for {@link ResourceLocation}s
 */
public class LocationHelper {
    public static final ResourceLocation BLOCK_SHEET = ResourceLocation.withDefaultNamespace("textures/atlas/blocks.png");

    /**
     * Create a {@link ResourceLocation} with DBVariant's Mod ID
     * @param path Path of the {@link ResourceLocation}
     * @return {@link ResourceLocation} with DBVariant namespace
     * @see DBVariants#MODID
     */
    public static ResourceLocation dbvLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(DBVariants.MODID, path);
    }

    /**
     * Create a {@link ResourceLocation} in Minecraft's namespace
     * @param path Path of the {@link ResourceLocation}
     * @return {@link ResourceLocation} with Minecraft namespace
     * @see ResourceLocation#withDefaultNamespace(String)
     */
    public static ResourceLocation mcLoc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    /**
     * Strips file extension and directories from a file path to return ID
     * @param location The file path as a {@link ResourceLocation}
     * @return The file id as a {@link ResourceLocation}
     */
    public static ResourceLocation getFileID(ResourceLocation location) {
        String path = location.getPath();
        // Remove directories
        path = Arrays.stream(path.split("/")).toList().getLast();
        // Remove file extension
        path = path.split("\\.")[0];

        return location.withPath(path);
    }
}
