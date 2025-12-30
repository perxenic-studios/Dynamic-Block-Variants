package dev.perxenic.dbvariants.content.chestMaterials;

import com.mojang.serialization.MapCodec;

public abstract class ChestMaterial {
    public abstract MapCodec<? extends ChestMaterial> type();
}
