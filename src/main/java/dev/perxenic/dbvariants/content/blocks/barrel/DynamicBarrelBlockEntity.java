package dev.perxenic.dbvariants.content.blocks.barrel;

import dev.perxenic.dbvariants.content.materialTypes.interfaces.IBarrelMaterial;
import dev.perxenic.dbvariants.content.materialTypes.interfaces.IMaterial;
import dev.perxenic.dbvariants.registry.DBVBlockEntities;
import dev.perxenic.dbvariants.registry.store.MaterialStore;
import dev.perxenic.dbvariants.util.material.BarrelMaterialHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DynamicBarrelBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items;
    private final ContainerOpenersCounter openersCounter;

    public static final String MATERIAL_TAG = "dynamic_material";
    public ResourceLocation barrelMaterialLoc;

    // Should only be updated and checked by client
    public IBarrelMaterial barrelMaterial;

    public DynamicBarrelBlockEntity(BlockPos pos, BlockState blockState) {
        super(DBVBlockEntities.DYNAMIC_BARREL.get(), pos, blockState);
        this.items = NonNullList.withSize(27, ItemStack.EMPTY);
        this.openersCounter = new ContainerOpenersCounter() {
            protected void onOpen(Level level, BlockPos pos, BlockState state) {
                DynamicBarrelBlockEntity.playSound(level, pos, state, SoundEvents.BARREL_OPEN);
                DynamicBarrelBlockEntity.updateBlockState(level, pos, state, true);
            }

            protected void onClose(Level level, BlockPos pos, BlockState state) {
                DynamicBarrelBlockEntity.playSound(level, pos, state, SoundEvents.BARREL_CLOSE);
                DynamicBarrelBlockEntity.updateBlockState(level, pos, state, false);
            }

            protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int previousCount, int newCount) {
            }

            protected boolean isOwnContainer(Player player) {
                if (player.containerMenu instanceof ChestMenu) {
                    Container container = ((ChestMenu)player.containerMenu).getContainer();
                    return container == DynamicBarrelBlockEntity.this;
                } else {
                    return false;
                }
            }
        };
    }

    public void updateBarrelMaterial() {
        IMaterial material = MaterialStore.chooseDefaultMaterial(barrelMaterialLoc);
        if (material instanceof IBarrelMaterial barrelMaterial) this.barrelMaterial = barrelMaterial;
        else this.barrelMaterial = BarrelMaterialHelper.getDefaultMaterial(barrelMaterialLoc);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (!tag.contains(MATERIAL_TAG)) return;

        barrelMaterialLoc = ResourceLocation.parse(tag.getString(MATERIAL_TAG));
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (barrelMaterialLoc != null) tag.putString(MATERIAL_TAG, this.barrelMaterialLoc.toString());
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    protected Component getDefaultName() {
        return Component.translatable("container.barrel");
    }

    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return ChestMenu.threeRows(id, player, this);
    }

    public void startOpen(Player player) {
        if (remove || level == null || player.isSpectator()) return;

        openersCounter.incrementOpeners(player, level, getBlockPos(), getBlockState());
    }

    public void stopOpen(Player player) {
        if (remove || level == null || player.isSpectator()) return;

        openersCounter.decrementOpeners(player, level, getBlockPos(), getBlockState());
    }

    public void recheckOpen() {
        if (remove || level == null) return;

        openersCounter.recheckOpeners(level, getBlockPos(), getBlockState());

    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);

        if (level == null) return;
        if (level.isClientSide) updateBarrelMaterial();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (level == null) return;
        if (level.isClientSide) updateBarrelMaterial();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("id", DBVBlockEntities.DYNAMIC_BARREL.getKey().location().toString());
        compoundTag.putString(DynamicBarrelBlockEntity.MATERIAL_TAG, barrelMaterialLoc.toString());

        components.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(compoundTag));
    }

    static void updateBlockState(Level level, BlockPos pos, BlockState state, boolean open) {
        level.setBlock(pos, state.setValue(BarrelBlock.OPEN, open), 3);
    }

    static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent sound) {
        Vec3i vec3i = (state.getValue(BarrelBlock.FACING)).getNormal();
        float x = pos.getX() + 0.5f + vec3i.getX() / 2.0f;
        float y = pos.getY() + 0.5f + vec3i.getY() / 2.0f;
        float z = pos.getZ() + 0.5f + vec3i.getZ() / 2.0f;
        level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5f, level.random.nextFloat() * 0.1f + 0.9f);
    }
}
