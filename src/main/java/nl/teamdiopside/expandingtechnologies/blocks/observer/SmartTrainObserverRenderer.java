/// MIT License
///
/// Copyright (c) 2019 simibubi
///
/// Permission is hereby granted, free of charge, to any person obtaining a copy
/// of this software and associated documentation files (the "Software"), to deal
/// in the Software without restriction, including without limitation the rights
/// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
/// copies of the Software, and to permit persons to whom the Software is
/// furnished to do so, subject to the following conditions:
///
/// The above copyright notice and this permission notice shall be included in all
/// copies or substantial portions of the Software.
///
/// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
/// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
/// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
/// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
/// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
/// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
/// SOFTWARE.
///
/// Observer Renderer modified to work with Expanding Technologies by curryducker.

package nl.teamdiopside.expandingtechnologies.blocks.observer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.trains.track.ITrackBlock;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import nl.teamdiopside.expandingtechnologies.registry.ETEdgePointTypes;

public class SmartTrainObserverRenderer extends SmartBlockEntityRenderer<SmartTrainObserverBlockEntity> {

    public SmartTrainObserverRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(SmartTrainObserverBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        if (VisualizationManager.supportsVisualization(be.getLevel())) return;
        BlockPos pos = be.getBlockPos();
        TrackTargetingBehaviour<SmartTrainObserver> target = be.smartEdgePoint;
        BlockPos targetPosition = target.getGlobalPosition();
        Level level = be.getLevel();
        if (level == null) return;
        BlockState trackState = level.getBlockState(targetPosition);
        if (trackState.getBlock() instanceof ITrackBlock) {
            ms.pushPose();
            TransformStack.of(ms).translate(targetPosition.subtract(pos));
            ETEdgePointTypes.render(level, targetPosition, target.getTargetDirection(), target.getTargetBezier(), ms, buffer, ETEdgePointTypes.SMART_OBSERVER, 1.0F);
            ms.popPose();
        }
    }
}
