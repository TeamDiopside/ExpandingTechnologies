package nl.teamdiopside.expandingtechnologies.config;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class ETServer extends ConfigBase {

    public final ETStress stressValues = nested(1, ETStress::new, Comments.stress);

    @Override
    public @NotNull String getName() {
        return "server";
    }

    private static class Comments {
        static String stress = "Fine tune the kinetic stats of individual components";
    }

}
