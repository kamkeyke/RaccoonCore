package net.kamkeyke.raccooncore;

import com.mojang.logging.LogUtils;
import net.kamkeyke.raccooncore.registry.ModArgumentTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
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

        ModArgumentTypes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("-------------------------------");
        LOGGER.info("  Greetings from the raccoon!  ");
        LOGGER.info("-------------------------------");
    }

    // ----------   ----------
    public static @Nullable Item simulateItem(String modId, String item){
        return ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(modId, item));
    }

    public static @Nullable Block simulateBlock(String modId, String item){
        return ForgeRegistries.BLOCKS.getValue(ResourceLocation.fromNamespaceAndPath(modId, item));
    }
}