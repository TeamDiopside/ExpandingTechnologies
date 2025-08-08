package nl.teamdiopside.expandingtechnologies.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import net.minecraft.world.level.Level;
import nl.teamdiopside.expandingtechnologies.blocks.observer.SmartTrainObserver;
import nl.teamdiopside.expandingtechnologies.registry.ETEdgePointTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(value = Train.class, remap = false)
public class TrainMixin {

    @Redirect(method = "tickOccupiedObservers",
            at = @At(value = "INVOKE",
                    target = "Lcom/simibubi/create/content/trains/graph/TrackGraph;getPoint(Lcom/simibubi/create/content/trains/graph/EdgePointType;Ljava/util/UUID;)Lcom/simibubi/create/content/trains/signal/TrackEdgePoint;"
            )
    )
    private TrackEdgePoint inject(TrackGraph instance, EdgePointType<? extends TrackEdgePoint> type, UUID id, @Local(argsOnly = true) Level level) {
        TrackEdgePoint observer = instance.getPoint(type, id);
        if (observer != null) return observer;
        SmartTrainObserver smartTrainObserver = instance.getPoint(ETEdgePointTypes.SMART_OBSERVER, id);
        smartTrainObserver.setLevel(level);
        return smartTrainObserver;
    }
}
