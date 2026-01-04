package dev.perxenic.dbvariants;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashMap;

@EventBusSubscriber
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue VANILLA_DEFAULT_BARREL_TEXTURE = BUILDER
            .comment("Whether unset materials should default to vanilla barrel texture (Requires client reload)")
            .define("vanillaDefaultBarrelTexture", false);

    public static final ModConfigSpec.BooleanValue VANILLA_DEFAULT_CHEST_TEXTURE = BUILDER
            .comment("Whether unset materials should default to vanilla chest texture (Requires client reload)")
            .define("vanillaDefaultChestTexture", false);

    public static final ModConfigSpec.BooleanValue XMAS_CHEST_RECIPE = BUILDER
            .comment("Whether a recipe should be added for christmas chests (Requires server reload)")
            .define("xmasChestRecipe", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean vanillaDefaultBarrelTexture;
    public static boolean vanillaDefaultChestTexture;
    public static boolean xmasChestRecipe;

    public static final HashMap<String, Boolean> configDict = new HashMap<>();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        vanillaDefaultBarrelTexture = VANILLA_DEFAULT_BARREL_TEXTURE.get();
        configDict.put("vanillaDefaultBarrelTexture", vanillaDefaultBarrelTexture);

        vanillaDefaultChestTexture = VANILLA_DEFAULT_CHEST_TEXTURE.get();
        configDict.put("vanillaDefaultChestTexture", vanillaDefaultChestTexture);

        xmasChestRecipe = XMAS_CHEST_RECIPE.get();
        configDict.put("xmasChestRecipe", xmasChestRecipe);
    }
}
