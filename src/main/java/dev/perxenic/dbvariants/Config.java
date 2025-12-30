package dev.perxenic.dbvariants;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashMap;

@EventBusSubscriber
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLE_VANILLA_CHEST_VARIANTS = BUILDER
            .comment("Whether to enable vanilla chest variants")
            .define("enableVanillaChestVariants", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean enableVanillaChestVariants;

    public static HashMap<String, Boolean> configDict = new HashMap<>();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enableVanillaChestVariants = ENABLE_VANILLA_CHEST_VARIANTS.get();
        configDict.put("enableVanillaChestVariants", enableVanillaChestVariants);
    }
}
