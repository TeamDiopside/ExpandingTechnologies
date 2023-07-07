package nl.teamdiopside.expandingtechnologies.data;

import com.simibubi.create.foundation.data.LangMerger;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nl.curryducker.expandingtechnologies.ExpandingTechnologies;

@Mod.EventBusSubscriber(modid = ExpandingTechnologies.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        gen.addProvider(true, new LangMerger(gen, ExpandingTechnologies.MODID, "Expanding Technologies", ETLangPartials.values()));
    }
}
