package nl.teamdiopside.expandingtechnologies.blocks.observer;

import com.simibubi.create.content.trains.observer.TrackObserverVisual;
import com.simibubi.create.content.trains.track.ITrackBlock;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import nl.teamdiopside.expandingtechnologies.mixin.TrackObserverVisualAccessor;

public class SmartTrainObserverVisual extends TrackObserverVisual {

    public SmartTrainObserverVisual(VisualizationContext ctx, SmartTrainObserverBlockEntity blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);
    }

    @Override
    public void tick(Context context) {
        SmartTrainObserverBlockEntity be = (SmartTrainObserverBlockEntity)this.blockEntity;
        TrackTargetingBehaviour<SmartTrainObserver> target = be.smartEdgePoint;
        BlockPos targetPosition = target.getGlobalPosition();
        Level level = be.getLevel();
        if (level == null) return;
        BlockState trackState = level.getBlockState(targetPosition);
        Block block = trackState.getBlock();
        TrackObserverVisualAccessor superAccessor = (TrackObserverVisualAccessor)this;
        if (block instanceof ITrackBlock trackBlock) {
            superAccessor.getOverlay().setIdentityTransform().translate(targetPosition);
            trackBlock.prepareTrackOverlay(superAccessor.getOverlay(), level, targetPosition, trackState, target.getTargetBezier(), target.getTargetDirection(), TrackTargetingBehaviour.RenderedTrackOverlayType.OBSERVER);
            superAccessor.getOverlay().setChanged();
        } else {
            superAccessor.getOverlay().setZeroTransform().setChanged();
        }
    }
}
