package nl.teamdiopside.expandingtechnologies.fabric;

import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import net.fabricmc.api.ModInitializer;

public class ExpandingTechnologiesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExpandingTechnologies.init();
    }
}