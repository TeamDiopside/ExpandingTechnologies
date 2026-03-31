package nl.teamdiopside.expandingtechnologies.events;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import nl.teamdiopside.expandingtechnologies.blocks.itemvacuum.ItemVacuumBlockEntity;

@EventBusSubscriber
public class CommonEvents {
    @net.neoforged.bus.api.SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        ItemVacuumBlockEntity.registerCapabilities(event);
    }
}
