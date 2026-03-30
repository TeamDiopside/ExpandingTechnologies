// Slightly modified version of TrackObserverVisual
// MIT License
//
// Copyright (c) The Create Team / The Creators of Create
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
// IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// Modified for SmartTrainObserverBlockEntity by curryducker.

package nl.teamdiopside.expandingtechnologies.blocks.observer;

import com.simibubi.create.content.trains.track.ITrackBlock;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleTickableVisual;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import nl.teamdiopside.expandingtechnologies.registry.ETEdgePointTypes;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SmartTrainObserverVisual extends AbstractBlockEntityVisual<SmartTrainObserverBlockEntity> implements SimpleTickableVisual {
    private final TransformedInstance overlay;
    private BlockPos oldTargetPos;

    public SmartTrainObserverVisual(VisualizationContext ctx, SmartTrainObserverBlockEntity blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);

        overlay = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(ETEdgePointTypes.getCustomPartial(ETEdgePointTypes.SMART_OBSERVER)))
                .createInstance();

        setupVisual();
    }

    @Override
    public void tick(Context context) {
        setupVisual();
    }

    @Override
    public void updateLight(float partialTick) {
        relight(overlay);
    }

    @Override
    protected void _delete() {
        overlay.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(overlay);
    }

    private void setupVisual() {
        TrackTargetingBehaviour<SmartTrainObserver> target = blockEntity.smartEdgePoint;
        BlockPos targetPosition = target.getGlobalPosition();
        Level level = blockEntity.getLevel();
        if (level == null) return;
        BlockState trackState = level.getBlockState(targetPosition);
        Block block = trackState.getBlock();

        if (!(block instanceof ITrackBlock trackBlock)) {
            overlay.setZeroTransform()
                    .setChanged();
            return;
        }

        if (!targetPosition.equals(oldTargetPos)) {
            oldTargetPos = targetPosition;

            overlay.setIdentityTransform()
                    .translate(targetPosition.subtract(renderOrigin()));

            TrackTargetingBehaviour.RenderedTrackOverlayType type = TrackTargetingBehaviour.RenderedTrackOverlayType.OBSERVER;
            trackBlock.prepareTrackOverlay(overlay, level, targetPosition, trackState, target.getTargetBezier(),
                    target.getTargetDirection(), type);

            overlay.setChanged();
        }
    }
}
