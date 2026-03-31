package net.kamkeyke.example.capability;

import net.kamkeyke.raccooncore.capability.RaccoonCapabilityProvider;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * This class was not registered in the ModEventBus and therefore will not appear in-game.
 * <p>
 * It serves only as an example.
 */
@ApiStatus.Internal
public class PlayerCapabilityProvider extends RaccoonCapabilityProvider {
    public static final Capability<CapData> CAP_DATA = CapabilityManager.get(new CapabilityToken<>() {});

    /**
     * Call me inside the {@link AttachCapabilitiesEvent}
     * Example:
     * <pre>{@code
     *     event.addCapability(ResourceLocation.fromNamespaceAndPath(MODID, "modid_capabilities"), new PlayerCapabilityProvider());
     * }</pre>
     */
    public PlayerCapabilityProvider(){
        register("capdata", CAP_DATA, CapData::new);
    }
}
