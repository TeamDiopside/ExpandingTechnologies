package nl.teamdiopside.expandingtechnologies.mixin;

import com.simibubi.create.content.trains.observer.TrackObserverVisual;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = TrackObserverVisual.class, remap = false)
public interface TrackObserverVisualAccessor {

    @Accessor("overlay")
    TransformedInstance getOverlay();
}
