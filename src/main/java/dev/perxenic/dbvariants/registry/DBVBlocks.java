package dev.perxenic.dbvariants.registry;

import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.blocks.barrel.DynamicBarrel;
import dev.perxenic.dbvariants.content.blocks.chest.DynamicChest;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DBVBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DBVariants.MODID);

    public static final DeferredBlock<DynamicBarrel> DYNAMIC_BARREL = BLOCKS.register("dynamic_barrel", () -> new DynamicBarrel(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
                    .noOcclusion()
    ));

    public static final DeferredBlock<DynamicChest> DYNAMIC_CHEST = BLOCKS.register("dynamic_chest", () -> new DynamicChest(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava(),
            DBVBlockEntities.DYNAMIC_CHEST::get
    ));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
