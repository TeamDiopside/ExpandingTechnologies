package nl.teamdiopside.expandingtechnologies.fabric.data;

import com.simibubi.create.foundation.data.LangMerger;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;

public class ETData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();
        ExpandingTechnologies.registrate().setupDatagen(gen, helper);
        gen.addProvider(true, new LangMerger(gen, ExpandingTechnologies.MODID, "Expanding Technologies", ETLangPartials.values()));
        gen.addProvider(true, ETRecipes::new);
    }
}
