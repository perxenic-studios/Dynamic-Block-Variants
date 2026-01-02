package dev.perxenic.dbvariants.content.blocks;

import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.chestMaterialTypes.ChestMaterial;
import dev.perxenic.dbvariants.data.ChestMaterialStore;
import dev.perxenic.dbvariants.registry.DBVBlockEntities;
import dev.perxenic.dbvariants.registry.DBVRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DynamicChestBlockEntity extends ChestBlockEntity {

    public static final String MATERIAL_TAG = "dynamic_material";
    public ResourceLocation chestMaterialLoc;

    // Client-side only
    public ChestMaterial chestMaterial;

    public DynamicChestBlockEntity(BlockPos pos, BlockState state) {
        super(DBVBlockEntities.DYNAMIC_CHEST.get(), pos, state);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (!tag.contains(MATERIAL_TAG)) return;

        chestMaterialLoc = ResourceLocation.parse(tag.getString(MATERIAL_TAG));
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (chestMaterialLoc != null) tag.putString(MATERIAL_TAG, this.chestMaterialLoc.toString());
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(@NotNull Connection net, @NotNull ClientboundBlockEntityDataPacket pkt, HolderLookup.@NotNull Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);

        // When packet is received on client side, update the chest material that should be displayed
        if (!ChestMaterialStore.CHEST_MATERIALS.containsKey(chestMaterialLoc)) {
            DBVariants.LOGGER.warn("Material {} does not exist!", chestMaterialLoc);
            return;
        }

        chestMaterial = ChestMaterialStore.CHEST_MATERIALS.get(chestMaterialLoc);
        DBVariants.LOGGER.info("{}",chestMaterial);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }
}
