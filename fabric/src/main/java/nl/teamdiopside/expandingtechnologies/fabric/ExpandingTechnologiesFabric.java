package nl.teamdiopside.expandingtechnologies.fabric;

import com.simibubi.create.AllCreativeModeTabs;
import net.fabricmc.api.ModInitializer;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;

public class ExpandingTechnologiesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExpandingTechnologies.init();
        ExpandingTechnologies.registrate().useCreativeTab(AllCreativeModeTabs.MAIN_TAB.key());
        ExpandingTechnologies.registrate().register();
    }
}