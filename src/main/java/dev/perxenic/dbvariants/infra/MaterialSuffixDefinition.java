package dev.perxenic.dbvariants.infra;

import dev.perxenic.dbvariants.content.materialTypes.interfaces.IMaterial;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class MaterialSuffixDefinition<T extends IMaterial> {
    public final Function<ResourceLocation, T> defaultMaterialGetter;

    public MaterialSuffixDefinition(Function<ResourceLocation, T> defaultMaterialGetter) {
        this.defaultMaterialGetter = defaultMaterialGetter;
    }
}
