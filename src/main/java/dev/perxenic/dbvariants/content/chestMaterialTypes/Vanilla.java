package dev.perxenic.dbvariants.content.chestMaterialTypes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class Vanilla extends ChestMaterial{
    public static final MapCodec<Vanilla> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                Codec.STRING.fieldOf("chest_name").forGetter(s -> s.chestName)
            ).apply(inst, Vanilla::new));

    public final String chestName;

    public Vanilla(String chestName) {
        this.chestName = chestName;
    }

    @Override
    public MapCodec<? extends ChestMaterial> codec() {
        return CODEC;
    }
}
