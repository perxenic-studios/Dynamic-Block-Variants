package dev.perxenic.dbvariants.utils;

import com.mojang.serialization.Codec;
import dev.perxenic.dbvariants.registry.DBVRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import static dev.perxenic.dbvariants.DBVariants.dbvLoc;

@EventBusSubscriber
public class DBVDataMaps {
    public static final DataMapType<Item, ResourceLocation> CHEST_MATERIAL = DataMapType.builder(
            dbvLoc("chest_material"),
            Registries.ITEM,
            ResourceLocation.CODEC
    ).build();

    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(CHEST_MATERIAL);
    }
}
