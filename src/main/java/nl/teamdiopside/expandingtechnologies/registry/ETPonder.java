package nl.teamdiopside.expandingtechnologies.registry;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import nl.teamdiopside.expandingtechnologies.ponder.CrossingLightsPonder;
import nl.teamdiopside.expandingtechnologies.ponder.DoorControllerPonder;
import nl.teamdiopside.expandingtechnologies.ponder.ItemVacuumPonder;
import nl.teamdiopside.expandingtechnologies.ponder.SmartTrainObserverPonder;
import org.jetbrains.annotations.NotNull;

import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.*;

public class ETPonder implements PonderPlugin {

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        HELPER.forComponents(ETBlocks.RAILROAD_LIGHT_CONTROLLER)
                .addStoryBoard("railroad_lights", CrossingLightsPonder::constructing)
                .addStoryBoard("practical_example", CrossingLightsPonder::practicalExample);

        HELPER.forComponents(ETBlocks.DOOR_CONTROLLER)
                .addStoryBoard("door_controller", DoorControllerPonder::doorControlConstructing);

        HELPER.forComponents(ETBlocks.ITEM_VACUUM)
                .addStoryBoard("item_vacuum", ItemVacuumPonder::itemVacuumConstructing);

        HELPER.forComponents(ETBlocks.SMART_TRAIN_OBSERVER)
                .addStoryBoard("smart_train_observer", SmartTrainObserverPonder::constructing);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<? extends ItemLike, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        HELPER.addToTag(DISPLAY_SOURCES).add(ETBlocks.RAILROAD_LIGHT_CONTROLLER).add(ETBlocks.SMART_TRAIN_OBSERVER);
        HELPER.addToTag(KINETIC_APPLIANCES).add(ETBlocks.ITEM_VACUUM);
        HELPER.addToTag(TRAIN_RELATED).add(ETBlocks.SMART_TRAIN_OBSERVER);
    }

    @Override
    public @NotNull String getModId() {
        return ExpandingTechnologies.MODID;
    }
}
