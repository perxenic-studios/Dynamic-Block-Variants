package dev.perxenic.dbvariants.content.blocks.chest;

import dev.perxenic.dbvariants.content.chestMaterialTypes.interfaces.IChestMaterial;
import dev.perxenic.dbvariants.registry.store.ChestMaterialStore;
import dev.perxenic.dbvariants.registry.DBVBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DynamicChestBlockEntity extends ChestBlockEntity {

    public static final String MATERIAL_TAG = "dynamic_material";
    public ResourceLocation chestMaterialLoc;

    public IChestMaterial chestMaterial;

    public DynamicChestBlockEntity(BlockPos pos, BlockState state) {
        super(DBVBlockEntities.DYNAMIC_CHEST.get(), pos, state);
    }

    public void updateChestMaterial() {
        chestMaterial = ChestMaterialStore.getChestMaterialSafe(chestMaterialLoc);
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

        if (level == null) return;
        if (level.isClientSide) updateChestMaterial();
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (level == null) return;
        if (level.isClientSide) updateChestMaterial();
    }

    @Override
    protected void collectImplicitComponents(@NotNull DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("id", DBVBlockEntities.DYNAMIC_CHEST.getKey().location().toString());
        compoundTag.putString(DynamicChestBlockEntity.MATERIAL_TAG, chestMaterialLoc.toString());

        components.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(compoundTag));
    }
}
