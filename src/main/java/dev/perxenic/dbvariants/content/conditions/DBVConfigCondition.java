package dev.perxenic.dbvariants.content.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.perxenic.dbvariants.Config;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

public record DBVConfigCondition(String configKey) implements ICondition {
    public static final MapCodec<DBVConfigCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.fieldOf("config_key").forGetter(DBVConfigCondition::configKey)
    ).apply(inst, DBVConfigCondition::new));

    @Override
    public boolean test(@NotNull IContext iContext) {
        if (!Config.configDict.containsKey(configKey)) return false;
        return Config.configDict.get(configKey);
    }

    @Override
    public @NotNull MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
