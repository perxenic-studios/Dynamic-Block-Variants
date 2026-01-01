package dev.perxenic.dbvariants.registry;

import dev.perxenic.dbvariants.DBVariants;
import dev.perxenic.dbvariants.content.blocks.client.DynamicChestBER;
import dev.perxenic.dbvariants.content.blocks.DynamicChestBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber
public class DBVBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DBVariants.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DynamicChestBlockEntity>> DYNAMIC_CHEST =
            BLOCK_ENTITY_TYPES.register("dynamic_chest", () -> BlockEntityType.Builder.of(
                    DynamicChestBlockEntity::new,
                    DBVBlocks.DYNAMIC_CHEST.get()
            ).build(null));

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                DBVBlockEntities.DYNAMIC_CHEST.get(),
                DynamicChestBER::new
        );
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
