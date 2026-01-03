package dev.perxenic.dbvariants.util;

import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;

public class PathHelper {

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
