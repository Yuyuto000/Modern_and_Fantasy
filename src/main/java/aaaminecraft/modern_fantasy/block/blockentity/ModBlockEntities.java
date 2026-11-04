package aaaminecraft.modern_fantasy.block.blockentity;

import aaaminecraft.modern_fantasy.MainEntrance;
import aaaminecraft.modern_fantasy.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(
                    ForgeRegistries.BLOCK_ENTITY_TYPES,
                    MainEntrance.MODID
            );

    public static final RegistryObject<BlockEntityType<ModernSofaBlockEntity>> MODERN_SOFA =
            BLOCK_ENTITIES.register(
                    "modern_sofa",
                    () -> BlockEntityType.Builder.of(
                            ModernSofaBlockEntity::new,
                            ModBlocks.MODERN_SOFA.get()
                    ).build(null)
            );

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}