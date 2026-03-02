package net.kamkeyke.example.registry;

import net.kamkeyke.raccooncore.RaccoonCore;
import net.kamkeyke.raccooncore.util.RaccoonRegistryUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.Set;

/**
 * This class was not registered in ModEventBus and therefore will not appear in-game.
 * <p>
 * It serves only as an example.
 */
public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RaccoonCore.MODID);

    /**
     * A set of exceptions for items that should not appear in the tab!
     */
    private static final Set<RegistryObject<?>> EXCEPTIONS = Set.of(
            ModItems.THINGY
    );

    /**
     * Cache to avoid processing the Stream every time.
     * It's not mandatory, but it's good practice if you don't change the exception list at runtime
     * and your creative tab is huge...
     * <p>
     * Or maybe I'm just overthinking performance.
     */
    private static Collection<Item> cachedItems;

    public static final RegistryObject<CreativeModeTab> RACCOON_CORE_TAB = CREATIVE_MODE_TAB.register("raccoon_core_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.THINGY.get()))
                    .displayItems((params, output) -> {
                        if (cachedItems == null) {
                            cachedItems = RaccoonRegistryUtil.CreativeTabs.getFilteredItems(ModItems.ITEMS, EXCEPTIONS);
                        }
                        cachedItems.forEach(output::accept);
                    })
                    .build());

    // No use here
    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}