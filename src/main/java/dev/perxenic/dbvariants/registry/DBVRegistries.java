package dev.perxenic.dbvariants.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IMaterial;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Function;

import static dev.perxenic.dbvariants.util.LocationHelper.dbvLoc;

@EventBusSubscriber
public class DBVRegistries {
    public static final ResourceKey<Registry<MapCodec<? extends IMaterial>>> MATERIAL_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(dbvLoc("material_type"));
    public static final Registry<MapCodec<? extends IMaterial>> MATERIAL_TYPE_REGISTRY = new RegistryBuilder<>(MATERIAL_TYPE_REGISTRY_KEY)
            .sync(true)
            .defaultKey(dbvLoc("vanilla"))
            .create();

    public static final Codec<IMaterial> MATERIAL_CODEC = MATERIAL_TYPE_REGISTRY.byNameCodec().dispatch(
            IMaterial::codec, Function.identity()
    );

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(MATERIAL_TYPE_REGISTRY);
    }
}
