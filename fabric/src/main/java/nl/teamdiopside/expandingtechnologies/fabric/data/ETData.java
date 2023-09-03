package nl.teamdiopside.expandingtechnologies.fabric.data;

import com.simibubi.create.foundation.data.AllLangPartials;
import com.simibubi.create.foundation.data.LangMerger;
import com.simibubi.create.foundation.data.LangPartial;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import nl.teamdiopside.expandingtechnologies.ExpandingTechnologies;
import nl.teamdiopside.expandingtechnologies.mixin.AccessorLangMerger;

public class ETData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();
        FabricDataGenerator.Pack pack = gen.createPack();
        ExpandingTechnologies.registrate().setupDatagen(pack, helper);
        pack.addProvider((FabricDataOutput output) -> createMerger(output, ExpandingTechnologies.MODID, "Expanding Technologies", ETLangPartials.values()));
        pack.addProvider(ETRecipes::new);
    }

    public static LangMerger createMerger(FabricDataOutput output, String modid, String displayName, LangPartial[] partials) {
        LangMerger merger = new LangMerger(output, modid, displayName, new AllLangPartials[0]);
        ((AccessorLangMerger) merger).setLangPartials(partials);
        return merger;
    }
}
