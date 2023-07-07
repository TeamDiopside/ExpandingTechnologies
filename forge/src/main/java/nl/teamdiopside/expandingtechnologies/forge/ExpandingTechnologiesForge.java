package nl.teamdiopside.expandingtechnologies.forge;

import dev.architectury.platform.forge.EventBuses;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExpandingTechnologies.MOD_ID)
public class ExpandingTechnologiesForge {
    public ExpandingTechnologiesForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ExpandingTechnologies.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
            ExpandingTechnologies.init();
    }
}