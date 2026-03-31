package nl.teamdiopside.expandingtechnologies;

import com.mojang.logging.LogUtils;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import nl.teamdiopside.expandingtechnologies.registry.*;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ExpandingTechnologies.MODID)
public class ExpandingTechnologies {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "expandingtechnologies";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID)
            .setTooltipModifierFactory(item ->
            new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );

    public ExpandingTechnologies(IEventBus modEventBus, ModContainer modContainer) {
        // Prevent automatic adding in search tab
        ExpandingTechnologies.registrate().defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
        ExpandingTechnologies.registrate().setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
        ExpandingTechnologies.registrate().registerEventListeners(modEventBus);

        ETPartialModels.register();
        ETBlocks.register();
        ETObserverConditions.register();
        ETBlockEntities.register();
        ETMenuTypes.register();
        ETSounds.register();
        ETPackets.register();

        ETConfigs.register(modContainer);
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}
