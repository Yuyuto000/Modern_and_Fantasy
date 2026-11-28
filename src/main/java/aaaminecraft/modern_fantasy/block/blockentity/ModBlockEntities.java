
package aaaminecraft.modern_fantasy.block.blockentity;

import aaaminecraft.modern_fantasy.MainEntrance;
import aaaminecraft.modern_fantasy.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * <h1>ModBlockEntities</h1>
 *
 * MODで使用するBlockEntityTypeの登録管理クラス。
 *
 * <p>
 *      各BlockEntityと対応するBlockを紐付け、
 *      Minecraftのレジストリに登録する。
 * </p>
 */
public class ModBlockEntities {

    /**
     * BlockEntityTypeのDeferredRegister。
     */
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(
                    ForgeRegistries.BLOCK_ENTITY_TYPES,
                    MainEntrance.MODID
            );

    public static final RegistryObject<BlockEntityType<SolarGeneratorBlockEntity>> SOLAR_GENERATOR =
            BLOCK_ENTITIES.register("solar_generator",
                    () -> BlockEntityType.Builder.of(
                            SolarGeneratorBlockEntity::new,
                            ModBlocks.SOLAR_GENERATOR.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<MechanicalCrafterBlockEntity>> MECHANICAL_CRAFTER =
            BLOCK_ENTITIES.register("mechanical_crafter",
                    () -> BlockEntityType.Builder.of(
                            MechanicalCrafterBlockEntity::new,
                            ModBlocks.MECHANICAL_CRAFTER.get()
                    ).build(null)
            );
    /**
     * BlockEntityTypeをイベントバスに登録する。
     *
     * @param bus MODイベントバス
     */
    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
