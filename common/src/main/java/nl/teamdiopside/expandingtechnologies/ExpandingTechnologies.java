package nl.teamdiopside.expandingtechnologies;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import nl.teamdiopside.expandingtechnologies.registry.ETBlockEntities;
import nl.teamdiopside.expandingtechnologies.registry.ETBlocks;
import nl.teamdiopside.expandingtechnologies.registry.ETSounds;
import org.slf4j.Logger;

public class ExpandingTechnologies
{
	public static final String MODID = "expandingtechnologies";
	public static final Logger LOGGER = LogUtils.getLogger();
	private static final CreateRegistrate REGISTRATE = CreateRegistrate.create("expandingtechnologies");

	public static void init() {
		ETBlocks.register();
		ETBlockEntities.register();
		ETSounds.register();
	}

	public static CreateRegistrate registrate() {
		return REGISTRATE;
	}
}