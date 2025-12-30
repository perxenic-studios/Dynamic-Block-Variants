package dev.perxenic.dbvariants.content.chestMaterialTypes;

import com.mojang.serialization.MapCodec;

public abstract class ChestMaterial {
    public abstract MapCodec<? extends ChestMaterial> codec();
}
