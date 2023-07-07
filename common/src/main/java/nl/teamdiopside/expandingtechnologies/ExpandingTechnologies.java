package nl.teamdiopside.expandingtechnologies;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.LangMerger;
import net.minecraft.data.DataGenerator;
import nl.teamdiopside.expandingtechnologies.data.ETLangPartials;
import nl.teamdiopside.expandingtechnologies.registry.ETBlockEntities;
import nl.teamdiopside.expandingtechnologies.registry.ETBlocks;
import nl.teamdiopside.expandingtechnologies.registry.ETSounds;
import nl.teamdiopside.expandingtechnologies.util.ETUtil;
import org.slf4j.Logger;

public class ExpandingTechnologies
{
	public static final String MODID = "expandingtechnologies";
	public static final Logger LOGGER = LogUtils.getLogger();
	private static final CreateRegistrate REGISTRATE = CreateRegistrate.create("expandingtechnologies").creativeModeTab(() -> ETUtil.ExpandingTechnologiesTab, "Expanding Technologies");

	public static void init() {
		ETBlocks.register();
		ETBlockEntities.register();
		ETSounds.register();
	}

	public static void gatherData(DataGenerator gen) {
		gen.addProvider(true, new LangMerger(gen, ExpandingTechnologies.MODID, "Expanding Technologies", ETLangPartials.values()));
	}

	public static CreateRegistrate registrate() {
		return REGISTRATE;
	}
}