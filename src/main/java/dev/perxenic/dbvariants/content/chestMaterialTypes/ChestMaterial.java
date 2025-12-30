package dev.perxenic.dbvariants.content.chestMaterialTypes;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.resources.model.Material;

public abstract class ChestMaterial {
    public abstract MapCodec<? extends ChestMaterial> codec();

    public abstract Material getMaterial();
    public abstract Material getLeftMaterial();
    public abstract Material getRightMaterial();
}
