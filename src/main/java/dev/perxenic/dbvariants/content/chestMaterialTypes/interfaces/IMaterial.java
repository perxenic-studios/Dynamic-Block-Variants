package dev.perxenic.dbvariants.content.chestMaterialTypes.interfaces;

import com.mojang.serialization.MapCodec;

public interface IMaterial {
    MapCodec<? extends IMaterial> codec();
}
