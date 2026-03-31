package nl.teamdiopside.expandingtechnologies;

import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import nl.teamdiopside.expandingtechnologies.registry.ETPonder;

@Mod(value = ExpandingTechnologies.MODID, dist = Dist.CLIENT)
public class ExpandingTechnologiesClient {
    public ExpandingTechnologiesClient(IEventBus modEventBus) {
        modEventBus.addListener(ExpandingTechnologiesClient::clientInit);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new ETPonder());
    }
}
