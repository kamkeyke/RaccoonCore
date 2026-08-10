package net.kamkeyke.example.datagen;

import net.kamkeyke.raccooncore.datagen.RaccoonData;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * This class was not registered in the ModEventBus and therefore will not appear in-game.
 * <p>
 * It serves only as an example.
 */
@ApiStatus.Internal
public class ExampleDataGenerators {
    public static void gatherData(GatherDataEvent event){
        RaccoonData data = new RaccoonData(event);

        // Manual sound provider:
        data.client(new ExampleManualModSoundProvider(data.output()));
        // Auto sound provider:
        data.client(new ExampleAutoModSoundProvider(data.output()));
        // Choose only ONE sound provider.
        // Both generate assets/<modid>/sounds.json, so registering both
        // will cause one to overwrite the other.
    }
}
