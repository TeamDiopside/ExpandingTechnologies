package nl.teamdiopside.expandingtechnologies.fabric;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;

public class ETData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();
        ExpandingTechnologies.registrate().setupDatagen(gen, helper);
        ExpandingTechnologies.LOGGER.warn("WIEJOE");
        ExpandingTechnologies.gatherData(gen);
    }
}
