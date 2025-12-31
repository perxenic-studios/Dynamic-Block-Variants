package dev.perxenic.dbvariants.content.chestMaterialTypes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

import static dev.perxenic.dbvariants.DBVariants.mcLoc;

public class VanillaChest extends ChestMaterial{
    public static final MapCodec<VanillaChest> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                Codec.STRING.fieldOf("chest_name").forGetter(s -> s.chestName)
            ).apply(inst, VanillaChest::new));

    public static final ResourceLocation CHEST_SHEET = ResourceLocation.withDefaultNamespace("textures/atlas/chest.png");

    public static Material mainMaterial;
    public static Material leftMaterial;
    public static Material rightMaterial;

    public final String chestName;

    public VanillaChest(String chestName) {
        this.chestName = chestName;
        mainMaterial = new Material(CHEST_SHEET, mcLoc("entity/chest/" + chestName));
        leftMaterial = new Material(CHEST_SHEET, mcLoc("entity/chest/" + chestName + "_left"));
        rightMaterial = new Material(CHEST_SHEET, mcLoc("entity/chest/" + chestName + "_right"));
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
