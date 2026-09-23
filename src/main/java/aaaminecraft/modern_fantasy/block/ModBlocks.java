package aaaminecraft.modern_fantasy.block;

import aaaminecraft.modern_fantasy.MainEntrance;
import aaaminecraft.modern_fantasy.block.customs.*;
import aaaminecraft.modern_fantasy.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MainEntrance.MODID);

    // 現代家具
    public static final RegistryObject<Block> MODERN_CHAIR = registerBlock("modern_chair",
            () -> new ModernChairBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> MODERN_DINING_TABLE = registerBlock("modern_dining_table",
            () -> new ModernDiningTableBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> MODERN_SOFA = registerBlock("modern_sofa",
            () -> new ModernSofaBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> MODERN_SHELF = registerBlock("modern_shelf",
            () -> new ModernShelfBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> MODERN_STORAGE_CABINET = registerBlock("modern_storage_cabinet",
            () -> new ModernStorageCabinetBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    // 機械系
    public static final RegistryObject<Block> ELECTRICAL_CONTROL_PANEL = registerBlock("electric_control_panel",
            () -> new ElectricControlPanelBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> MODERN_SMALL_GENERATOR = registerBlock("modern_small_generator",
            () -> new ModernSmallGeneratorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> MECHANICAL_CRAFTER = registerBlock("mechanical_crafter",
            () -> new MechanicalCrafterBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> ITEM_CONVEYOR = registerBlock("item_conveyor",
            () -> new ItemConveyorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> UNIVERSAL_DIGITAL_STORAGE = registerBlock("universal_digital_storage",
            () -> new UniversalDigitalStorageBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    // 魔術・ファンタジー系
    public static final RegistryObject<Block> MAGIC_REACTOR = registerBlock("magic_reactor",
            () -> new MagicReactorBlock(BlockBehaviour.Properties.copy(Blocks.FURNACE))); // マジカニウム魔力炉
    public static final RegistryObject<Block> MAGIC_CONDUIT = registerBlock("magic_conduit",
            () -> new MagicConduitBlock(BlockBehaviour.Properties.copy(Blocks.STONE))); // マジカニウム魔力導管
    public static final RegistryObject<Block> MAGIC_CRYSTAL = registerBlock("magic_crystal",
            () -> new MagiccrystalBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK))); // 魔力の結晶
    public static final RegistryObject<Block> MAGIC_CIRCLE_STAND = registerBlock("magic_circle_stand",
            () -> new MagicCircleStandBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> ALCHEMY_CAULDRON = registerBlock("alchemy_cauldron",
            () -> new AlchemyCauldronBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON)));
    public static final RegistryObject<Block> MAGIC_BOOK_SHELF = registerBlock("magic_book_shelf",
            () -> new MagicBookShelfBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> SUMMONING_ALTAR = registerBlock("summoning_altar",
            () -> new SummoningAltarBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> SPIRIT_LANTERN = registerBlock("spirit_lantern",
            () -> new SpiritLanternBlock(BlockBehaviour.Properties.copy(Blocks.LANTERN)));
    public static final RegistryObject<Block> ANCIENT_MAGIC_DEVICE = registerBlock("ancient_magic_device",
            () -> new AncientMagicDeviceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> ALTAR_WORLD_TREE = registerBlock("altar_world_tree",
            () -> new AlterWorldTreeBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
