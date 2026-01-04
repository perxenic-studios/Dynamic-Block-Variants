package dev.perxenic.dbvariants.registry;

import com.mojang.serialization.MapCodec;
import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.materialTypes.BlockOverlay;
import dev.perxenic.dbvariants.content.materialTypes.VanillaBarrel;
import dev.perxenic.dbvariants.content.materialTypes.VanillaChest;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IMaterial;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DBVMaterialTypes {
    public static final DeferredRegister<MapCodec<? extends IMaterial>> MATERIAL_TYPES =
            DeferredRegister.create(DBVRegistries.MATERIAL_TYPE_REGISTRY, DBVariants.MODID);

    public static final Supplier<MapCodec<VanillaBarrel>> VANILLA_BARREL =
            MATERIAL_TYPES.register("vanilla_barrel", () -> VanillaBarrel.CODEC);

    public static final Supplier<MapCodec<VanillaChest>> VANILLA_CHEST =
            MATERIAL_TYPES.register("vanilla_chest", () -> VanillaChest.CODEC);

    public static final Supplier<MapCodec<BlockOverlay>> BLOCK_OVERLAY =
            MATERIAL_TYPES.register("block_overlay", () -> BlockOverlay.CODEC);

    public static void register(IEventBus eventBus) {
        MATERIAL_TYPES.register(eventBus);
    }
}
