package net.kamkeyke.raccooncore.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class RaccoonLootTableProvider {
    public static LootTableProvider createBlockOnly(PackOutput output, Supplier<LootTableSubProvider> provider) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(provider, LootContextParamSets.BLOCK)
        ));
    }
}
