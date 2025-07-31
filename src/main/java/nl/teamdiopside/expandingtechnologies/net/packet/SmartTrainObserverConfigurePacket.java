package nl.teamdiopside.expandingtechnologies.net.packet;

import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import nl.teamdiopside.expandingtechnologies.blocks.observer.ObserverCondition;
import nl.teamdiopside.expandingtechnologies.blocks.observer.SmartTrainObserverBlockEntity;

public class SmartTrainObserverConfigurePacket extends BlockEntityConfigurationPacket<SmartTrainObserverBlockEntity> {

    private ObserverCondition condition;

    public SmartTrainObserverConfigurePacket(BlockPos pos, ObserverCondition condition) {
        super(pos);
        this.condition = condition;
    }

    public SmartTrainObserverConfigurePacket(FriendlyByteBuf friendlyByteBuf) {
        super(friendlyByteBuf);
    }

    @Override
    protected void writeSettings(FriendlyByteBuf friendlyByteBuf) {
        condition.toBuf(friendlyByteBuf);
    }

    @Override
    protected void readSettings(FriendlyByteBuf friendlyByteBuf) {
        this.condition = ObserverCondition.fromBuf(friendlyByteBuf);
    }

    @Override
    protected void applySettings(SmartTrainObserverBlockEntity smartTrainObserverBlockEntity) {
        if (condition == null) return;
        smartTrainObserverBlockEntity.setCondition(condition);
    }
}
