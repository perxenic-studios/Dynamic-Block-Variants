package dev.perxenic.dbvariants;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashMap;

@EventBusSubscriber
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue VANILLA_DEFAULT_CHEST_TEXTURE = BUILDER
            .comment("Whether unset materials should default to vanilla chest texture (Requires client reload)")
            .define("vanillaDefaultChestTexture", false);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean vanillaDefaultChestTexture;

    public static final HashMap<String, Boolean> configDict = new HashMap<>();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        vanillaDefaultChestTexture = VANILLA_DEFAULT_CHEST_TEXTURE.get();
        configDict.put("vanillaDefaultChestTexture", vanillaDefaultChestTexture);
    }
}
