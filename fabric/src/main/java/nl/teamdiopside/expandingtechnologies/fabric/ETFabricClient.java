package nl.teamdiopside.expandingtechnologies.fabric;

import net.fabricmc.api.ClientModInitializer;
import nl.teamdiopside.expandingtechnologies.registry.ETPonder;

public class ETFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ETPonder.register();
    }
}
