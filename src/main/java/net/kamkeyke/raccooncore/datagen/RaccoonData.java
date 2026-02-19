package net.kamkeyke.raccooncore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class RaccoonData {
    private final GatherDataEvent event;
    private final DataGenerator generator;
    private final PackOutput output;
    private final ExistingFileHelper helper;
    private final CompletableFuture<HolderLookup.Provider> lookup;

    public RaccoonData(GatherDataEvent event) {
        this.event = event;
        this.generator = event.getGenerator();
        this.output = generator.getPackOutput();
        this.helper = event.getExistingFileHelper();
        this.lookup = event.getLookupProvider();
    }

    public PackOutput output() {
        return output;
    }

    public ExistingFileHelper helper() {
        return helper;
    }

    public CompletableFuture<HolderLookup.Provider> lookup() {
        return lookup;
    }

    public <T extends DataProvider> T server(T provider) {
        generator.addProvider(event.includeServer(), provider);
        return provider;
    }

    public <T extends DataProvider> T client(T provider) {
        generator.addProvider(event.includeClient(), provider);
        return provider;
    }
}
