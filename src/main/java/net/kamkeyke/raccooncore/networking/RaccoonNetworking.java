package net.kamkeyke.raccooncore.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class RaccoonNetworking {
    private static final String PROTOCOL_VERSION = "1.0";
    private int packetId = 0;
    private final SimpleChannel channel;

    private int id(){ return packetId++; }

    public RaccoonNetworking(String modid) {
        this.channel = NetworkRegistry.ChannelBuilder
                .named(ResourceLocation.fromNamespaceAndPath(modid, "main"))
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .simpleChannel();
    }

    public <MSG> void registerPacket(Class<MSG> packetClass, FriendlyByteBuf.Reader<MSG> decoder, BiConsumer<MSG, FriendlyByteBuf> encoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> handler, NetworkDirection direction) {
        channel.messageBuilder(packetClass, id(), direction)
                .encoder(encoder)
                .decoder(decoder)
                .consumerMainThread(handler)
                .add();
    }

    public SimpleChannel channel(){
        return this.channel;
    }
}
