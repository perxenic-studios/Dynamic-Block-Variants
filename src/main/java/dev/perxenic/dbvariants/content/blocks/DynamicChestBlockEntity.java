package dev.perxenic.dbvariants.content.blocks;

import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.chestMaterialTypes.ChestMaterial;
import dev.perxenic.dbvariants.registry.DBVBlockEntities;
import dev.perxenic.dbvariants.registry.DBVRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
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
    public Holder.Reference<ChestMaterial> dynamicMaterial;

    public DynamicChestBlockEntity(BlockPos pos, BlockState state) {
        super(DBVBlockEntities.DYNAMIC_CHEST.get(), pos, state);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (!tag.contains(MATERIAL_TAG)) return;

        Optional<HolderLookup.RegistryLookup<ChestMaterial>> registryOptional = registries.lookup(DBVRegistries.CHEST_MATERIAL_REGISTRY_KEY);
        if (registryOptional.isEmpty()) {
            DBVariants.LOGGER.error("Chest material registry does not exist!");
            return;
        }

        ResourceKey<ChestMaterial> materialResourceKey = ResourceKey.create(
                DBVRegistries.CHEST_MATERIAL_REGISTRY_KEY,
                ResourceLocation.parse(tag.getString(MATERIAL_TAG))
        );

        Optional<Holder.Reference<ChestMaterial>> materialOptional = registryOptional.get().get(materialResourceKey);

        if (materialOptional.isEmpty()) {
            DBVariants.LOGGER.warn("Material {} does not exist!", tag.getString(MATERIAL_TAG));
            return;
        }

        dynamicMaterial = materialOptional.get();
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (dynamicMaterial != null) tag.putString(MATERIAL_TAG, this.dynamicMaterial.key().location().toString());
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
