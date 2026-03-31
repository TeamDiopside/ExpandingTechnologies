package nl.teamdiopside.expandingtechnologies.net.packet;

import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import nl.teamdiopside.expandingtechnologies.blocks.observer.ObserverCondition;
import nl.teamdiopside.expandingtechnologies.blocks.observer.SmartTrainObserverBlockEntity;
import nl.teamdiopside.expandingtechnologies.registry.ETPackets;

public class SmartTrainObserverConfigurePacket extends BlockEntityConfigurationPacket<SmartTrainObserverBlockEntity> {

    public static final StreamCodec<FriendlyByteBuf, SmartTrainObserverConfigurePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, packet -> packet.pos,
            ObserverCondition.STREAM_CODEC, packet -> packet.condition,
            SmartTrainObserverConfigurePacket::new
    );

    private final ObserverCondition condition;

    public SmartTrainObserverConfigurePacket(BlockPos pos, ObserverCondition condition) {
        super(pos);
        this.condition = condition;
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return ETPackets.CONFIGURE_SMART_TRAIN_OBSERVER;
    }

    @Override
    protected void applySettings(ServerPlayer player, SmartTrainObserverBlockEntity smartTrainObserverBlockEntity) {
        if (condition == null) return;
        smartTrainObserverBlockEntity.setCondition(condition);
    }
}
