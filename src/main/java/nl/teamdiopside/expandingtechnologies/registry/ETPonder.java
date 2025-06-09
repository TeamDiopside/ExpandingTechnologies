package nl.teamdiopside.expandingtechnologies.registry;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import nl.teamdiopside.expandingtechnologies.util.PonderScenes;
import org.jetbrains.annotations.NotNull;

import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.DISPLAY_SOURCES;

public class ETPonder implements PonderPlugin {

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        HELPER.forComponents(ETBlocks.RAILROAD_LIGHT_CONTROLLER)
                .addStoryBoard("railroad_lights", PonderScenes::constructing)
                .addStoryBoard("practical_example", PonderScenes::practicalExample);

        HELPER.forComponents(ETBlocks.DOOR_CONTROLLER)
                .addStoryBoard("door_controller", PonderScenes::doorControlConstructing);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        HELPER.addToTag(DISPLAY_SOURCES).add(ETBlocks.RAILROAD_LIGHT_CONTROLLER);
    }

    @Override
    public @NotNull String getModId() {
        return ExpandingTechnologies.MODID;
    }
}
