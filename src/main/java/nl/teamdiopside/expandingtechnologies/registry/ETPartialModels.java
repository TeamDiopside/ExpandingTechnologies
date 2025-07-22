package nl.teamdiopside.expandingtechnologies.registry;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;

public class ETPartialModels {

    public static final PartialModel VACUUM_FAN = block("item_vacuum/fan");

    private static PartialModel block(String path) {
        return PartialModel.of(ResourceLocation.fromNamespaceAndPath(ExpandingTechnologies.MODID, "block/" + path));
    }

    public static void register() {
        ExpandingTechnologies.LOGGER.info("Registering Partial Models for ET.");
    }
}
