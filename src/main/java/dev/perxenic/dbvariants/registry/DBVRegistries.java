package dev.perxenic.dbvariants.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.perxenic.dbvariants.content.chestMaterialTypes.ChestMaterial;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Function;

import static dev.perxenic.dbvariants.DBVariants.dbvLoc;

@EventBusSubscriber
public class DBVRegistries {
    public static final ResourceKey<Registry<MapCodec<? extends ChestMaterial>>> CHEST_MATERIAL_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(dbvLoc("chest_material_type"));
    public static final Registry<MapCodec<? extends ChestMaterial>> CHEST_MATERIAL_TYPE_REGISTRY = new RegistryBuilder<>(CHEST_MATERIAL_TYPE_REGISTRY_KEY)
            .sync(true)
            .defaultKey(dbvLoc("vanilla"))
            .create();

    public static final Codec<ChestMaterial> CHEST_MATERIAL_CODEC = CHEST_MATERIAL_TYPE_REGISTRY.byNameCodec().dispatch(
            ChestMaterial::codec, Function.identity()
    );

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(CHEST_MATERIAL_TYPE_REGISTRY);
    }
}
