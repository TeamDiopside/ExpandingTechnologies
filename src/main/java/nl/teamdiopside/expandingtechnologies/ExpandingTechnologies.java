package nl.teamdiopside.expandingtechnologies;

import com.mojang.logging.LogUtils;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.architectury.platform.forge.EventBuses;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nl.teamdiopside.expandingtechnologies.registry.ETBlockEntities;
import nl.teamdiopside.expandingtechnologies.registry.ETBlocks;
import nl.teamdiopside.expandingtechnologies.registry.ETPonder;
import nl.teamdiopside.expandingtechnologies.registry.ETSounds;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExpandingTechnologies.MODID)
public class ExpandingTechnologies {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "expandingtechnologies";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    public ExpandingTechnologies() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(ExpandingTechnologies.MODID, modEventBus);

        ETBlocks.register();
        ETBlockEntities.register();
        ETSounds.register();

        ExpandingTechnologies.registrate().setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
        ExpandingTechnologies.registrate().registerEventListeners(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> registerClient(modEventBus, modEventBus));
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static void registerClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        PonderIndex.addPlugin(new ETPonder());
    }
}
