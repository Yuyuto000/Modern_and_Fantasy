package aaaminecraft.modern_fantasy;

import aaaminecraft.modern_fantasy.block.ModBlocks;
import aaaminecraft.modern_fantasy.entity.ModEntities;
import aaaminecraft.modern_fantasy.item.ModCreativeTabs;
import aaaminecraft.modern_fantasy.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MainEntrance.MODID)
public class MainEntrance {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "modern_fantasy";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public MainEntrance() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModCreativeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.CARBON_FIBER_BUNDLES);
            event.accept(ModItems.DRAGON_BLOOD_ORE);
            event.accept(ModItems.ELECTRONIC_CONTROL_BOARD);
            event.accept(ModItems.FAIRY_FEATHERS);
            event.accept(ModItems.FORGED_ALLOW_PLATE);
            event.accept(ModItems.GENERAL_PURPOSE_ELECTRIC_MOTORS);
            event.accept(ModItems.HIGH_VOLTAGE_CAPACITOR);
            event.accept(ModItems.INDUSTRIAL_CENSOR);
            event.accept(ModItems.MAGIC_CORE);
            event.accept(ModItems.MAGICANIUM);
            event.accept(ModItems.MOONLIGHT_GRASS);
            event.accept(ModItems.OBSIDIAN_MAGIC_STONE);
            event.accept(ModItems.PHILOSOPHER_STONE_FRAGMENT);
            event.accept(ModItems.PRECISION_MACHINED_PARTS);
            event.accept(ModItems.QUANTUM_COMPUTATION_CORE);
            event.accept(ModItems.REFINED_ALLOW_PLATE);
            event.accept(ModItems.REINFORCED_POLYMER);
            event.accept(ModItems.SMALL_POWER_CELL);
            event.accept(ModItems.SPIRIT_STONE);
            event.accept(ModItems.STAR_SILVER_ORE);
            event.accept(ModItems.WORLD_TREE_SAP);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
