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
        // Initialization of the Observer.
        super.blockEntityAdded(blockEntity, front);
        // Notify Trains that this Observer exists.
        this.setFilterAndNotify(blockEntity.getLevel(), ItemStack.EMPTY);
    }

    public void setLevel(Level level) {
        // Set level in which the Block Entity is present so we can obtain it later.
        // Should be set by the Train just before activating the Observer.
        this.level = level;
    }

    private boolean shouldActivate(Train train) {
        // Evaluate the Observer's condition.
        if (this.level == null) return false;
        if (!(this.level.getBlockEntity(this.blockEntityPos) instanceof SmartTrainObserverBlockEntity blockEntity)) return false;
        ObserverCondition condition = blockEntity.getCondition();
        return train != null && condition != null && condition.evaluate(train);
    }

    @Override
    public void keepAlive(Train train) {
        // Triggered by the Train driving over the observer.
        // Evaluate the condition before activating.
        if (!shouldActivate(train)) return;
        super.keepAlive(train);
    }
}
