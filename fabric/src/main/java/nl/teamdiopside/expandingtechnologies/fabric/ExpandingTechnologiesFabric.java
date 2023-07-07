package nl.teamdiopside.expandingtechnologies.fabric;

import net.fabricmc.api.ModInitializer;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;

public class ExpandingTechnologiesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExpandingTechnologies.init();
    }
}