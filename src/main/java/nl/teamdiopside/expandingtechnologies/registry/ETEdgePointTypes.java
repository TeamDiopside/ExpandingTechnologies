package nl.teamdiopside.expandingtechnologies.registry;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import com.simibubi.create.content.trains.track.BezierTrackPointLocation;
import com.simibubi.create.content.trains.track.ITrackBlock;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.PoseTransformStack;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.levelWrappers.SchematicLevel;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import nl.teamdiopside.expandingtechnologies.blocks.observer.SmartTrainObserver;

import java.util.HashMap;
import java.util.function.Supplier;

public class ETEdgePointTypes {
    private static final HashMap<EdgePointType<?>, PartialModel> CUSTOM_EDGE_POINT_PARTIALS = new HashMap<>();
    public static final EdgePointType<SmartTrainObserver> SMART_OBSERVER = register(ResourceLocation.fromNamespaceAndPath(ExpandingTechnologies.MODID, "smart_observer"), SmartTrainObserver::new, AllPartialModels.TRACK_OBSERVER_OVERLAY);

    private static <T extends TrackEdgePoint> EdgePointType<T> register(ResourceLocation id, Supplier<T> factory, PartialModel partialModel) {
        EdgePointType<T> edgePointType = EdgePointType.register(id, factory);
        CUSTOM_EDGE_POINT_PARTIALS.put(edgePointType, partialModel);
        return edgePointType;
    }

    public static PartialModel getCustomPartial(EdgePointType<?> edgePointType) {
        return CUSTOM_EDGE_POINT_PARTIALS.get(edgePointType);
    }

    public static boolean containsCustomPartial(EdgePointType<?> edgePointType) {
        return CUSTOM_EDGE_POINT_PARTIALS.containsKey(edgePointType);
    }

    @OnlyIn(Dist.CLIENT)
    public static void render(LevelAccessor level, BlockPos pos, Direction.AxisDirection direction, BezierTrackPointLocation bezier, PoseStack ms, MultiBufferSource buffer, EdgePointType<?> type, float scale) {
        if ((level instanceof SchematicLevel && !(level instanceof PonderLevel)) || !level.isClientSide()) return;
        BlockState trackState = level.getBlockState(pos);
        Block block = trackState.getBlock();
        if (!(block instanceof ITrackBlock track)) return;

        ms.pushPose();
        PoseTransformStack msr = TransformStack.of(ms);
        // There is no reason for the type to be there, it just returns the affiliated partial model, which we don't use.
        // So we picked station :)
        track.prepareTrackOverlay(msr, level, pos, trackState, bezier, direction, TrackTargetingBehaviour.RenderedTrackOverlayType.STATION);
        PartialModel partial = CUSTOM_EDGE_POINT_PARTIALS.getOrDefault(type, null);
        if (partial != null) {
            CachedBuffers.partial(partial, trackState).translate(0.5, 0.0, 0.5).scale(scale).translate(-0.5, 0.0, -0.5).light(LevelRenderer.getLightColor(level, pos)).renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
        }
        ms.popPose();
    }
}
