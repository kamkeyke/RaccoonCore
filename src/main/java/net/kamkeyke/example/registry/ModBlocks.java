package net.kamkeyke.example.registry;

import net.kamkeyke.raccooncore.RaccoonCore;
import net.kamkeyke.raccooncore.util.RaccoonRegistryUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * This class was not registered in ModEventBus and therefore will not appear in-game.
 * <p>
 * It serves only as an example.
 */
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RaccoonCore.MODID);

    public static final RegistryObject<Block> EXAMPLE_BLOCK = registerBlock("example_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    // No use here
    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }

    /**
     * If you're using RaccoonRegistryUtil, to avoid making the block registration code line too long,
     * I recommend creating these two methods in your own "ModBlocks" class:
     */
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return RaccoonRegistryUtil.Blocks.registerBlock(name, block, BLOCKS, ModItems.ITEMS);
    }
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, int burnTime) {
        return RaccoonRegistryUtil.Blocks.registerBlock(name, block, burnTime, BLOCKS, ModItems.ITEMS);
    }
}
