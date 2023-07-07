package nl.curryducker.expandingtechnologies.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import nl.curryducker.expandingtechnologies.ExpandingTechnologies;
import nl.curryducker.expandingtechnologies.blocks.crossinglights.CrossingLightsBlockEntity;

public class ETBlockEntities {
    private static final CreateRegistrate REGISTRATE = ExpandingTechnologies.registrate();

    public static final BlockEntityEntry<CrossingLightsBlockEntity> RAILROAD_LIGHT_CONTROLLER = REGISTRATE
            .blockEntity("railroad_light_controller", CrossingLightsBlockEntity::new)
            .validBlocks(ETBlocks.RAILROAD_LIGHT_CONTROLLER)
            .register();

    public static void register() {}
}
