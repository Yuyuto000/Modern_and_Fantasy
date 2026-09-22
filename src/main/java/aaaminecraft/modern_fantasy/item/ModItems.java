package aaaminecraft.modern_fantasy.item;

import aaaminecraft.modern_fantasy.MainEntrance;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MainEntrance.MODID);

    // 現代アイテム系
    public static final RegistryObject<Item> REFINED_ALLOW_PLATE = ITEMS.register("refined_allow_plate",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FORGED_ALLOW_PLATE = ITEMS.register("forged_allow_plate",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ELECTRONIC_CONTROL_BOARD = ITEMS.register("electronic_control_board",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SMALL_POWER_CELL = ITEMS.register("small_power_cell",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> INDUSTRIAL_CENSOR = ITEMS.register("industrial_censor",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HIGH_VOLTAGE_CAPACITOR = ITEMS.register("high_voltage_capacitor",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CARBON_FIBER_BUNDLES = ITEMS.register("carbon_fiber_bundles",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRECISION_MACHINED_PARTS = ITEMS.register("precision_machined_parts",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REINFORCED_POLYMER = ITEMS.register("reinforced_polymer",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GENERAL_PURPOSE_ELECTRIC_MOTORS = ITEMS.register("general_motors",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> QUANTUM_COMPUTATION_CORE = ITEMS.register("quantum_core",
            () -> new Item(new Item.Properties()));
    // 魔術アイテム系
    public static final RegistryObject<Item> MAGICANIUM =  ITEMS.register("magicanium",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPIRIT_STONE = ITEMS.register("spirit_stone",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DRAGON_BLOOD_ORE = ITEMS.register("dragon_blood_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MOONLIGHT_GRASS = ITEMS.register("moonlight_grass",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STAR_SILVER_ORE = ITEMS.register("star_silver_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGIC_CORE = ITEMS.register("magic_core",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FAIRY_FEATHERS = ITEMS.register("fairy_feathers",
            ()  -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_MAGIC_STONE = ITEMS.register("obsidian_magic_stone",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WORLD_TREE_SAP = ITEMS.register("world_tree_sap",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PHILOSOPHER_STONE_FRAGMENT = ITEMS.register("philosopher_stone_fragment",
            () -> new Item(new Item.Properties()));
    // 特殊素材

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
