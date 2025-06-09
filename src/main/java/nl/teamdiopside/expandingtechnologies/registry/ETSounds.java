package nl.teamdiopside.expandingtechnologies.registry;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import nl.teamdiopside.expandingtechnologies.util.ETUtil;

public class ETSounds {

    public static final RegistryEntry<SoundEvent> CROSSING_BELL = ExpandingTechnologies.registrate()
            .simple("crossing_bell", Registries.SOUND_EVENT, () -> SoundEvent.createVariableRangeEvent(ETUtil.resourceLocation("crossing_bell")));

    public static void register() {}
}
