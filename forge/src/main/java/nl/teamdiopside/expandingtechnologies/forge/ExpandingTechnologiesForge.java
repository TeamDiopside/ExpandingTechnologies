package nl.teamdiopside.expandingtechnologies.forge;

import com.simibubi.create.AllCreativeModeTabs;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;

@Mod(ExpandingTechnologies.MODID)
public class ExpandingTechnologiesForge {

    public ExpandingTechnologiesForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ExpandingTechnologies.MODID, modEventBus);

        ExpandingTechnologies.init();
        ExpandingTechnologies.registrate().setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
        ExpandingTechnologies.registrate().registerEventListeners(modEventBus);
    }
}