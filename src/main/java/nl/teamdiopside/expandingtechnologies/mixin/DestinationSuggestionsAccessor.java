package nl.teamdiopside.expandingtechnologies.mixin;

import com.mojang.brigadier.suggestion.Suggestion;
import com.simibubi.create.content.trains.schedule.DestinationSuggestions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = DestinationSuggestions.class, remap = false)
public interface DestinationSuggestionsAccessor {

    @Accessor("active")
    boolean isActive();

    @Accessor("currentSuggestions")
    void setCurrentSuggestions(List<Suggestion> currentSuggestions);

    @Accessor("previous")
    void setPrevious(String previous);
}
