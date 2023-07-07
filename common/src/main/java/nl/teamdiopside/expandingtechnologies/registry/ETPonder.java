package nl.teamdiopside.expandingtechnologies.registry;

import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.infrastructure.ponder.AllPonderTags;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import nl.teamdiopside.expandingtechnologies.util.PonderScenes;

public class ETPonder {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(ExpandingTechnologies.MODID);

    public ETPonder() {
    }

    public static void register() {
        HELPER.forComponents(ETBlocks.RAILROAD_LIGHT_CONTROLLER)
                .addStoryBoard("railroad_lights", PonderScenes::constructing)
                .addStoryBoard("practical_example", PonderScenes::practicalExample);
        PonderRegistry.TAGS.forTag(AllPonderTags.DISPLAY_SOURCES).add(ETBlocks.RAILROAD_LIGHT_CONTROLLER);
    }
}
