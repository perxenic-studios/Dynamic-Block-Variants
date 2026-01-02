package dev.perxenic.dbvariants.content.blocks;

import dev.perxenic.dbvariants.Config;
import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.chestMaterialTypes.BlockOverlayChest;
import dev.perxenic.dbvariants.content.chestMaterialTypes.ChestMaterial;
import dev.perxenic.dbvariants.content.chestMaterialTypes.VanillaChest;
import dev.perxenic.dbvariants.data.ChestMaterialStore;
import dev.perxenic.dbvariants.datagen.DBVChestMaterialProvider;
import dev.perxenic.dbvariants.registry.DBVBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class DynamicChestBlockEntity extends ChestBlockEntity {

    public static final String MATERIAL_TAG = "dynamic_material";
    public ResourceLocation chestMaterialLoc;

    @OnlyIn(Dist.CLIENT)
    public ChestMaterial chestMaterial;

    public DynamicChestBlockEntity(BlockPos pos, BlockState state) {
        super(DBVBlockEntities.DYNAMIC_CHEST.get(), pos, state);
    }

    @OnlyIn(Dist.CLIENT)
    public void updateChestMaterial() {
        if(chestMaterialLoc == null) {
            // No matter what config is, if location is null use vanilla texture as server hasn't sent data yet
            chestMaterial = DBVChestMaterialProvider.DEFAULT;
            return;
        }

        if (!ChestMaterialStore.CHEST_MATERIALS.containsKey(chestMaterialLoc)) {
            if (Config.vanillaDefaultChestTexture) {
                chestMaterial = DBVChestMaterialProvider.DEFAULT;
            } else {
                chestMaterial = new BlockOverlayChest(chestMaterialLoc);
            }
            return;
        }

        chestMaterial = ChestMaterialStore.CHEST_MATERIALS.get(chestMaterialLoc);
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
}
