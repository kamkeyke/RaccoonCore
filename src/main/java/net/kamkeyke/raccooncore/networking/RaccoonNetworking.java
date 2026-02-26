package net.kamkeyke.raccooncore.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class RaccoonNetworking {
    private int packetId = 0;
    private final SimpleChannel channel;

    private int id(){ return packetId++; }

    public RaccoonNetworking(String modid, String protocolVersion) {
        this.channel = NetworkRegistry.ChannelBuilder
                .named(ResourceLocation.fromNamespaceAndPath(modid, "main"))
                .serverAcceptedVersions(protocolVersion::equals)
                .clientAcceptedVersions(protocolVersion::equals)
                .networkProtocolVersion(() -> protocolVersion)
                .simpleChannel();
    }

    public SimpleChannel channel(){
        return this.channel;
    }

    public <MSG> void registerPacket(Class<MSG> packetClass, FriendlyByteBuf.Reader<MSG> decoder, BiConsumer<MSG, FriendlyByteBuf> encoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> handler) {
        channel.messageBuilder(packetClass, id())
                .encoder(encoder)
                .decoder(decoder)
                .consumerMainThread(handler)
                .add();
    }
    public <MSG> void registerPacket(Class<MSG> packetClass, FriendlyByteBuf.Reader<MSG> decoder, BiConsumer<MSG, FriendlyByteBuf> encoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> handler, NetworkDirection direction) {
        channel.messageBuilder(packetClass, id(), direction)
                .encoder(encoder)
                .decoder(decoder)
                .consumerMainThread(handler)
                .add();
    }

    public <MSG> void sendToServer(MSG message){
        channel.sendToServer(message);
    }
    public <MSG> void sendToClient(MSG message, ServerPlayer player){
        channel.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
    public <MSG> void sendToAllPlayers(MSG message){
        channel.send(PacketDistributor.ALL.noArg(), message);
    }
    public <MSG> void sendNearPlayer(MSG message, ServerPlayer player, double radius){
        channel.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
                        player.getX(), player.getY(), player.getZ(),
                        radius, player.level().dimension()
                )
        ), message);
    }
}
