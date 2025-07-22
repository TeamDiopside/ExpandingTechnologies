package nl.teamdiopside.expandingtechnologies.config;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class ETCommon extends ConfigBase {

    public final ConfigBool allowSelfAddress = b(true, "allowSelfAddress", Comments.allowSelfAddress);
    public final ConfigBool betterContraptionDoorPosition = b(true, "betterContraptionDoorPosition", Comments.betterContraptionDoorPosition);

    @Override
    public @NotNull String getName() {
        return "common";
    }

    private static class Comments {
        static String allowSelfAddress = "Whether '@s' should be substituted for the player's name when ordering a package at a shopkeeper";
        static String betterContraptionDoorPosition = "Whether the position of doors should be determined relative to the actual center of the contraption";
    }
}
