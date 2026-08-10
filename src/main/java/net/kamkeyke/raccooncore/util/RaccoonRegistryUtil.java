package net.kamkeyke.raccooncore.util;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RaccoonRegistryUtil {

    public static class Blocks{
        /**
         * Registers a Block and automatically creates its corresponding {@link BlockItem}.
         *
         * @param name  The registry name (Registry ID).
         * @param block A {@link Supplier} providing the Block instance.
         * @return The {@link RegistryObject} of the registered Block.
         */
        public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, DeferredRegister<Block> blocks, DeferredRegister<Item> items) {
            RegistryObject<T> toReturn = blocks.register(name, block);
            Items.registerBlockItem(name, toReturn, items);
            return toReturn;
        }

        /**
         * Registers a Block and automatically creates its corresponding {@link BlockItem}
         * with a burn time.
         *
         * @param name      The registry name (Registry ID).
         * @param block     A {@link Supplier} providing the Block instance.
         * @param burnTime  The fuel duration in ticks for the item (e.g., 200 ticks = 1 item smelted).
         * @return The {@link RegistryObject} of the registered Block.
         */
        public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, int burnTime, DeferredRegister<Block> blocks, DeferredRegister<Item> items) {
            RegistryObject<T> toReturn = blocks.register(name, block);
            Items.registerBlockItem(name, toReturn, burnTime, items);
            return toReturn;
        }
    }

    public static class Items{
        /**
         * Registers a {@link BlockItem} for a previously registered Block.
         *
         * @param name  The registry name (must match the block's registry name).
         * @param block The {@link RegistryObject} of the parent block.
         * @return The {@link RegistryObject} of the registered Item.
         */
        @SuppressWarnings("UnusedReturnValue")
        public static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, DeferredRegister<Item> items){
            return items.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        }

        /**
         * Registers a {@link BlockItem} with fuel properties for a previously registered Block.
         *
         * @param name      The registry name (must match the block's registry name).
         * @param block     The {@link RegistryObject} of the parent block.
         * @param burnTime  The fuel duration in ticks (e.g., 200 ticks = 1 item smelted).
         * @return The {@link RegistryObject} of the registered Item.
         */
        @SuppressWarnings("UnusedReturnValue")
        public static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, int burnTime, DeferredRegister<Item> items){
            return items.register(name, () -> new BlockItem(block.get(), new Item.Properties()){
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                    return burnTime;
                }
            });
        }
    }

    public static class CreativeTabs{
        /**
         * Filters a DeferredRegister of items based on a set of exceptions.
         * Use this during Tab initialization to cache the items and avoid
         * re-filtering every time the tab is opened.
         *
         * @param register  The mod's Item DeferredRegister.
         * @param excluded  A Set of RegistryObjects to be skipped.
         * @return A collection of items ready to be added to a tab.
         */
        public static Collection<Item> getFilteredItems(DeferredRegister<Item> register, Set<RegistryObject<?>> excluded) {
            return register.getEntries().stream()
                    .filter(entry -> !excluded.contains(entry))
                    .map(RegistryObject::get)
                    .collect(Collectors.toList());
        }
    }
}
