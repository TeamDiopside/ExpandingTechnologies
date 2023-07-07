package nl.teamdiopside.expandingtechnologies;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import nl.teamdiopside.expandingtechnologies.registry.ETBlockEntities;
import nl.teamdiopside.expandingtechnologies.registry.ETBlocks;
import nl.teamdiopside.expandingtechnologies.util.ETUtil;
import org.slf4j.Logger;

public class ExpandingTechnologies
{
	public static final String MODID = "expandingtechnologies";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final CreateRegistrate REGISTRATE = CreateRegistrate.create("expandingtechnologies").creativeModeTab(() -> ETUtil.ExpandingTechnologies, "Expanding Technologies");

	public static void init() {
		ETBlocks.register();
		ETBlockEntities.register();
	}

	public static CreateRegistrate registrate() {
		return REGISTRATE;
	}
}