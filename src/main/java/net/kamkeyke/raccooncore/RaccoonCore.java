package net.kamkeyke.raccooncore;

import com.mojang.logging.LogUtils;
import net.kamkeyke.raccooncore.registry.ArgumentTypes;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

/**
 * The main entry point for the RaccoonCore library.
 * <p>RaccoonCore is a small framework that I created to help me build cleaner codes, reducing
 * boilerplate code. It provides specialized wrappers and providers for common modding tasks.
 * <p><strong>Core Features:</strong>
 * <ul>
 * <li><b>Networking:</b> Automatic packet ID management and simplified channel registration.</li>
 * <li><b>Data Generation:</b> Abstract providers for Recipes, Loot Tables, and BlockStates.</li>
 * <li><b>Commands:</b> Custom Brigadier ArgumentTypes (e.g., Player Lists).</li>
 * <li><b>Scheduling:</b> Thread-safe, tick-based task execution on the server.</li>
 * <li><b>Utilities:</b> Time formatting and tick conversion tools.</li>
 * </ul>
 * @author kamkeyke
 * @since 1.20.1
 */
@Mod(RaccoonCore.MODID)
public class RaccoonCore
{
    public static final String MODID = "raccooncore";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RaccoonCore(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ArgumentTypes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Greetings to confirm the library
     * is correctly loaded in the environment.
     */
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        if (System.getProperty("raccoon.greeted") == null) {
            LOGGER.info("-------------------------------");
            LOGGER.info("  Greetings from the raccoon!  ");
            LOGGER.info("-------------------------------");
            System.setProperty("raccoon.greeted", "true");
        }
    }

}