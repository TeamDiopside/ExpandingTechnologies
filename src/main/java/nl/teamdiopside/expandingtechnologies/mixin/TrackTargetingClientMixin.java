package nl.teamdiopside.expandingtechnologies.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.track.BezierTrackPointLocation;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.content.trains.track.TrackTargetingClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import nl.teamdiopside.expandingtechnologies.registry.ETEdgePointTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TrackTargetingClient.class, remap = false)
public class TrackTargetingClientMixin {

    @Shadow static EdgePointType<?> lastType;

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lcom/simibubi/create/content/trains/track/TrackTargetingBehaviour;render(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction$AxisDirection;Lcom/simibubi/create/content/trains/track/BezierTrackPointLocation;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILcom/simibubi/create/content/trains/track/TrackTargetingBehaviour$RenderedTrackOverlayType;F)V"
            )
    )
    private static void inject(LevelAccessor level, BlockPos pos, Direction.AxisDirection direction, BezierTrackPointLocation bezier, PoseStack ms, MultiBufferSource buffer, int light, int overlay, TrackTargetingBehaviour.RenderedTrackOverlayType type, float scale) {
        if (ETEdgePointTypes.containsCustomPartial(lastType)) {
            ETEdgePointTypes.render(level, pos, direction, bezier, ms, buffer, lastType, scale);
            return;
        }
        TrackTargetingBehaviour.render(level, pos, direction, bezier, ms, buffer, light, overlay, type, scale);
    }
}
