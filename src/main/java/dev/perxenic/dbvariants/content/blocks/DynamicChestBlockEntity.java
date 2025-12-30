package dev.perxenic.dbvariants.content.blocks;

import dev.perxenic.dbvariants.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DynamicChestBlockEntity extends ChestBlockEntity {

    public static final String MATERIAL_TAG = "dynamic_material";
    public ResourceLocation dynamicMaterial;

    public DynamicChestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DYNAMIC_CHEST.get(), pos, state);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains(MATERIAL_TAG)) this.dynamicMaterial = ResourceLocation.parse(tag.getString(MATERIAL_TAG));
        else dynamicMaterial = ResourceLocation.withDefaultNamespace("oak_planks");
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (dynamicMaterial != null) tag.putString(MATERIAL_TAG, this.dynamicMaterial.toString());
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }
}
