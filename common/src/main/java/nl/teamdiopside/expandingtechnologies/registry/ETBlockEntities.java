package nl.teamdiopside.expandingtechnologies.registry;

import com.simibubi.create.content.kinetics.base.HalfShaftInstance;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import nl.teamdiopside.expandingtechnologies.blocks.crossinglights.CrossingLightsBlockEntity;
import nl.teamdiopside.expandingtechnologies.blocks.kinetic_battery.KineticBatteryBlockEntity;
import nl.teamdiopside.expandingtechnologies.blocks.kinetic_battery.KineticBatteryRenderer;

public class ETBlockEntities {
    private static final CreateRegistrate REGISTRATE = ExpandingTechnologies.registrate();

    public static final BlockEntityEntry<CrossingLightsBlockEntity> RAILROAD_LIGHT_CONTROLLER = REGISTRATE
            .blockEntity("railroad_light_controller", CrossingLightsBlockEntity::new)
            .validBlocks(ETBlocks.RAILROAD_LIGHT_CONTROLLER)
            .register();

    public static final BlockEntityEntry<KineticBatteryBlockEntity> KINETIC_BATTERY = REGISTRATE
            .blockEntity("kinetic_battery", KineticBatteryBlockEntity::new)
            .instance(() -> HalfShaftInstance::new)
            .validBlocks(ETBlocks.KINETIC_BATTERY::get)
            .renderer(() -> KineticBatteryRenderer::new)
            .register();

    public static void register() {}
}
