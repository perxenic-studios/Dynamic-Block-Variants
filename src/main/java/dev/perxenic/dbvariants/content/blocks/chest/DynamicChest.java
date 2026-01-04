package dev.perxenic.dbvariants.content.blocks.chest;

import com.mojang.serialization.MapCodec;
import dev.perxenic.dbvariants.registry.DBVBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class DynamicChest extends ChestBlock {
    public static final MapCodec<DynamicChest> CODEC = simpleCodec(properties -> new DynamicChest(properties, DBVBlockEntities.DYNAMIC_CHEST::get));

    public DynamicChest(Properties properties, Supplier<BlockEntityType<? extends ChestBlockEntity>> blockEntityType) {
        super(properties, blockEntityType);
    }

    @Override
    public @NotNull MapCodec<? extends DynamicChest> codec() {
        return CODEC;
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new DynamicChestBlockEntity(pos, state);
    }
}
