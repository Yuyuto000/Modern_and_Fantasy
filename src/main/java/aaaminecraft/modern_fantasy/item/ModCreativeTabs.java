package aaaminecraft.modern_fantasy.item;

import aaaminecraft.modern_fantasy.MainEntrance;
import aaaminecraft.modern_fantasy.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MainEntrance.MODID);

    public static final RegistryObject<CreativeModeTab> MODERN_OBJECT_TABS = CREATIVE_MODE_TABS.register("modern_object_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.FORGED_ALLOW_PLATE.get()))
                    .title(Component.translatable("creativetab.modern_object_tab"))
                    .displayItems((parameters, output) -> {
                        // Materials
                        output.accept(ModItems.CARBON_FIBER_BUNDLES.get());
                        output.accept(ModItems.REFINED_ALLOW_PLATE.get());
                        output.accept(ModItems.FORGED_ALLOW_PLATE.get());
                        output.accept(ModItems.ELECTRONIC_CONTROL_BOARD.get());
                        output.accept(ModItems.SMALL_POWER_CELL.get());
                        output.accept(ModItems.INDUSTRIAL_CENSOR.get());
                        output.accept(ModItems.HIGH_VOLTAGE_CAPACITOR.get());
                        output.accept(ModItems.CARBON_FIBER_BUNDLES.get());
                        output.accept(ModItems.PRECISION_MACHINED_PARTS.get());
                        output.accept(ModItems.REINFORCED_POLYMER.get());
                        output.accept(ModItems.GENERAL_PURPOSE_ELECTRIC_MOTORS.get());
                        output.accept(ModItems.QUANTUM_COMPUTATION_CORE.get());
                        // Blocks
                        output.accept(ModBlocks.MODERN_CHAIR.get());
                        output.accept(ModBlocks.MODERN_DINING_TABLE.get());
                        output.accept(ModBlocks.MODERN_SOFA.get());
                        output.accept(ModBlocks.MODERN_SHELF.get());
                        output.accept(ModBlocks.MODERN_STORAGE_CABINET.get());
                        output.accept(ModBlocks.ELECTRICAL_CONTROL_PANEL.get());
                        output.accept(ModBlocks.MODERN_SMALL_GENERATOR.get());
                        output.accept(ModBlocks.MECHANICAL_CRAFTER.get());
                        output.accept(ModBlocks.ITEM_CONVEYOR.get());
                        output.accept(ModBlocks.UNIVERSAL_DIGITAL_STORAGE.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> FANTASY_OBJECT_TABS = CREATIVE_MODE_TABS.register("fantasy_object_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.MAGICANIUM.get()))
                    .title(Component.translatable("creativetab.fantasy_object_tab"))
                    .displayItems((parameters, output) -> {
                        // Materials
                        output.accept(ModItems.MAGICANIUM.get());
                        output.accept(ModItems.SPIRIT_STONE.get());
                        output.accept(ModItems.DRAGON_BLOOD_ORE.get());
                        output.accept(ModItems.MOONLIGHT_GRASS.get());
                        output.accept(ModItems.STAR_SILVER_ORE.get());
                        output.accept(ModItems.MAGIC_CORE.get());
                        output.accept(ModItems.FAIRY_FEATHERS.get());
                        output.accept(ModItems.OBSIDIAN_MAGIC_STONE.get());
                        output.accept(ModItems.WORLD_TREE_SAP.get());
                        output.accept(ModItems.PHILOSOPHER_STONE_FRAGMENT.get());
                        // Blocks
                        output.accept(ModBlocks.MAGIC_REACTOR.get());
                        output.accept(ModBlocks.MAGIC_CONDUIT.get());
                        output.accept(ModBlocks.MAGIC_CRYSTAL.get());
                        output.accept(ModBlocks.MAGIC_CIRCLE_STAND.get());
                        output.accept(ModBlocks.ALCHEMY_CAULDRON.get());
                        output.accept(ModBlocks.MAGIC_BOOK_SHELF.get());
                        output.accept(ModBlocks.SUMMONING_ALTAR.get());
                        output.accept(ModBlocks.SPIRIT_LANTERN.get());
                        output.accept(ModBlocks.ANCIENT_MAGIC_DEVICE.get());
                        output.accept(ModBlocks.ALTAR_WORLD_TREE.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
