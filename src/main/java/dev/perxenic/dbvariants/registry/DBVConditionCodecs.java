package dev.perxenic.dbvariants.registry;

import com.mojang.serialization.MapCodec;
import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.conditions.DBVConfigCondition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class DBVConditionCodecs {
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, DBVariants.MODID);

    public static final Supplier<MapCodec<DBVConfigCondition>> DBV_CONFIG =
            CONDITION_CODECS.register("config", () -> DBVConfigCondition.CODEC);

    public static void register(IEventBus eventBus) {
        CONDITION_CODECS.register(eventBus);
    }
}
