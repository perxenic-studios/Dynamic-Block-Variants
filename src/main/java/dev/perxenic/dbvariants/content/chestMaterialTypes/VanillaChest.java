package dev.perxenic.dbvariants.content.chestMaterialTypes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class VanillaChest extends ChestMaterial{
    public static final MapCodec<VanillaChest> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                ResourceLocation.CODEC.fieldOf("chest_name").forGetter(s -> s.chestName)
            ).apply(inst, VanillaChest::new));

    public Material mainMaterial;
    public Material leftMaterial;
    public Material rightMaterial;

    // Change to a resource location or something to allow rendering chests from different namespaces
    public final ResourceLocation chestName;

    public VanillaChest(ResourceLocation chestName) {
        this.chestName = chestName;
        mainMaterial = newMainMaterial(chestName);
        leftMaterial = newLeftMaterial(chestName);
        rightMaterial = newRightMaterial(chestName);
    }

    @Override
    public MapCodec<? extends ChestMaterial> codec() {
        return CODEC;
    }

    @Override
    public Material getMainMaterial() {
        return mainMaterial;
    }

    @Override
    public Material getLeftMaterial() {
        return leftMaterial;
    }

    @Override
    public Material getRightMaterial() {
        return rightMaterial;
    }
}
