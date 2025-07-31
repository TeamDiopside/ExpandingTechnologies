package nl.teamdiopside.expandingtechnologies.blocks.observer;

import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.observer.TrackObserver;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SmartTrainObserver extends TrackObserver {

    private Level level;

    public SmartTrainObserver() {
        super();
    }

    @Override
    public void blockEntityAdded(BlockEntity blockEntity, boolean front) {
        super.blockEntityAdded(blockEntity, front);
        this.setFilterAndNotify(blockEntity.getLevel(), ItemStack.EMPTY);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    private boolean shouldActivate(Train train) {
        if (this.level == null) return false;
        if (!(this.level.getBlockEntity(this.blockEntityPos) instanceof SmartTrainObserverBlockEntity blockEntity)) return false;
        ObserverCondition condition = blockEntity.getCondition();
        return train != null && condition != null && condition.evaluate(train);
    }

    @Override
    public void keepAlive(Train train) {
        if (!shouldActivate(train)) return;
        super.keepAlive(train);
    }
}
