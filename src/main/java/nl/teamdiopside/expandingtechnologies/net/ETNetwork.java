package nl.teamdiopside.expandingtechnologies.net;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import nl.teamdiopside.expandingtechnologies.net.packet.SmartTrainObserverConfigurePacket;

public class ETNetwork {
    private static final int VERSION = 1;
    private static SimpleChannel CHANNEL;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        CHANNEL = NetworkRegistry.ChannelBuilder.named(ResourceLocation.fromNamespaceAndPath(ExpandingTechnologies.MODID, "messages"))
                .serverAcceptedVersions(String.valueOf(VERSION)::equals)
                .clientAcceptedVersions(String.valueOf(VERSION)::equals)
                .networkProtocolVersion(() -> String.valueOf(VERSION))
                .simpleChannel();
        CHANNEL.messageBuilder(SmartTrainObserverConfigurePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SmartTrainObserverConfigurePacket::new)
                .encoder(SmartTrainObserverConfigurePacket::write)
                .consumerMainThread((p, contextSupplier) -> {
                    NetworkEvent.Context context = contextSupplier.get();
                    if (p.handle(context)) context.setPacketHandled(true);
                })
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        if (CHANNEL == null) throw new IllegalStateException("Channel Not Configured");
        CHANNEL.sendToServer(message);
    }
}
