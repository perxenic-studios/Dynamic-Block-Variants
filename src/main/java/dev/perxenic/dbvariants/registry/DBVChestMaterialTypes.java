package dev.perxenic.dbvariants.registry;

import com.mojang.serialization.MapCodec;
import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.chestMaterialTypes.BlockOverlayChest;
import dev.perxenic.dbvariants.content.chestMaterialTypes.ChestMaterial;
import dev.perxenic.dbvariants.content.chestMaterialTypes.VanillaChest;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DBVChestMaterialTypes {
    public static final DeferredRegister<MapCodec<? extends ChestMaterial>> CHEST_MATERIAL_TYPES =
            DeferredRegister.create(DBVRegistries.CHEST_MATERIAL_TYPE_REGISTRY, DBVariants.MODID);

    public static final Supplier<MapCodec<VanillaChest>> VANILLA =
            CHEST_MATERIAL_TYPES.register("vanilla", () -> VanillaChest.CODEC);

    public static final Supplier<MapCodec<BlockOverlayChest>> BLOCK_OVERLAY =
            CHEST_MATERIAL_TYPES.register("block_overlay", () -> BlockOverlayChest.CODEC);

    public static void register(IEventBus eventBus) {
        CHEST_MATERIAL_TYPES.register(eventBus);
    }
}
