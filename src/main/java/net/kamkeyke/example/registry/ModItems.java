package net.kamkeyke.example.registry;

import net.kamkeyke.raccooncore.RaccoonCore;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * This class was not registered in ModEventBus and therefore will not appear in-game.
 * <p>
 * It serves only as an example.
 */
public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RaccoonCore.MODID);

    public static final RegistryObject<Item> THINGY = ITEMS.register("thingy",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1)
                    .saturationMod(10f)
                    .fast()
                    .effect(() -> new MobEffectInstance(MobEffects.LEVITATION, 20, 100), 1f)
                    .effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 600, 0), 1f)
                    .alwaysEat()
                    .build())));

    // No use here
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
